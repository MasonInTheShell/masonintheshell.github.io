import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalDate;
import java.util.ArrayList;

/*
 * Stores animals in a MySQL database using JDBC.
 *
 * Implements the same AnimalRepository interface as InMemoryRepository, so AnimalService
 * and ConsoleUI do not need to know which storage is being used.
 *
 * All SQL values are passed through PreparedStatement parameters which are the ? place holders
 * which prevents SQL injection. The SELECT statements below are built from constant strings only so
 * no user input is ever part of the SQL text
 *
 * Every method opens its own connection in a try-with-resources block, which guarantees the
 * connection is closed even if an error occurs.
 *
 * Each animal is one row in "animals" plus one row in "dogs" or "monkeys".
 * 
 * 
 */

public class JdbcAnimalRepository implements AnimalRepository {

	private static final Logger LOGGER = Logger.getLogger(JdbcAnimalRepository.class.getName());
	
	private static final String USER_MESSAGE = "A database error occurred. Please try again later.";
	
	// -- SQL --
	
	private static final String EXISTS_BY_NAME_SQL = "SELECT 1 FROM animals WHERE name = ?";
	
	private static final String INSERT_ANIMAL_SQL = 
			"INSERT INTO animals (name, animal_type, gender, age, weight, acquisition_date, "
			+ "acquisition_country, training_status, reserved, in_service_country) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static final String INSERT_DOG_SQL = "INSERT INTO dogs (animal_id, breed) VALUES (?, ?)";
	
	private static final String INSERT_MONKEY_SQL = 
			"INSERT INTO monkeys (animal_id, tail_length, height, body_length, species) "
			+ "VALUES (?, ?, ?, ?, ?)";
	
	private static final String ANIMAL_COLUMNS =
			"a.animal_id, a.name, a.animal_type, a.gender, a.age, a.weight, a.acquisition_date, "
			+ "a.acquisition_country, a.training_status, a.reserved, a.in_service_country";
	
	private static final String FIND_ALL_SQL = 
			"SELECT " + ANIMAL_COLUMNS + ", d.breed, m.tail_length, m.height, m.body_length, m.species "
			+ "FROM animals a "
			+ "LEFT JOIN dogs d on d.animal_id = a.animal_id "
			+ "LEFT JOIN monkeys m ON m.animal_id = a.animal_id "
			+ "ORDER BY a.animal_id";
	
	private static final String FIND_DOGS_SQL =
			"SELECT " + ANIMAL_COLUMNS + ", d.breed "
			+ "FROM animals a JOIN dogs d ON d.animal_id = a.animal_id "
			+ "ORDER BY a.animal_id";
	
	private static final String FIND_MONKEYS_SQL =
			"SELECT " + ANIMAL_COLUMNS + ", m.tail_length, m.height, m.body_length, m.species "
			+ "FROM animals a JOIN monkeys m ON m.animal_id = a.animal_id "
			+ "ORDER BY a.animal_id";
	
	private static final String UPDATE_ANIMAL_SQL = 
			"UPDATE animals SET gender = ?, age = ?, weight = ?, acquisition_date = ?, "
			+ "acquisition_country = ?, training_status = ?, reserved = ?, in_service_country = ? "
			+ "WHERE name = ?";
	
	private static final String UPDATE_DOG_SQL = 
			"UPDATE dogs SET breed = ? "
			+ "WHERE animal_id = (SELECT animal_id FROM animals WHERE name = ?)";
	
	private static final String UPDATE_MONKEY_SQL = 
			"UPDATE monkeys SET tail_length = ?, height = ?, body_length = ?, species = ? "
			+ "WHERE animal_id = (SELECT animal_id FROM animals WHERE name = ?)";
	
	private static final String DELETE_ANIMAL_SQL =
			"DELETE FROM animals WHERE name = ?";

	private final String url;
	private final String user;
	private final String password;

	
	public JdbcAnimalRepository(String url, String user, String password) {
		this.url = url;
		this.user = user;
		this.password = password;
	}
	
	//opens a new connection to MySQL. Callers must close it try-with resources
	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}
	
	//logs the technical details and returns an exception with a safe message
	private RepositoryException fail(String operation, SQLException e) {
		LOGGER.log(Level.SEVERE, "Database error during " + operation, e);
		return new RepositoryException(USER_MESSAGE, e);
	}
	
	//used once at startup
	public void verifyConnection() {
		try (Connection conn = getConnection()) {
			
		}
		catch (SQLException e) {
			throw fail("verifyConnection", e);
		}
	}
	
	// ---------------- CREATE ----------------
	
	@Override
	public boolean existsByName(String name) {
		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement(EXISTS_BY_NAME_SQL)) {
			
			ps.setString(1, name);
			
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
		catch (SQLException e) {
			throw fail("existsByName", e);
		}
		
	}
	
	// a dog or monkey is stored as two rows(animals + dogs/monkeys)
	//both inserts run in one go. setAutoCommit false groups them,
	// commit() saves both and rollback() undoes both if something fails
	@Override
	public void add(RescueAnimal animal) {
		if (!(animal instanceof Dog) && !(animal instanceof Monkey)) {
			throw new IllegalArgumentException("Animal type not supported: " + animal.getClass().getName());
		}
		
		try (Connection conn = getConnection()) {
			conn.setAutoCommit(false);
			
			try {
				int animalId = insertAnimal(conn, animal);
				
				if (animal instanceof Dog) {
					insertDog(conn, animalId, (Dog) animal);
				}
				else {
					insertMonkey(conn, animalId, (Monkey) animal);
				}
				
				conn.commit();
			}
			catch (SQLException e) {
				conn.rollback();
				throw e;
			}
		}
		catch (SQLException e) {
			throw fail("add", e);
		}
	}
	
	
	private int insertAnimal(Connection conn, RescueAnimal animal) throws SQLException {
		try (PreparedStatement ps = conn.prepareStatement(INSERT_ANIMAL_SQL, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, animal.getName());
			ps.setString(2, animal.getAnimalType());
			ps.setString(3, animal.getGender());
			ps.setInt(4, animal.getAge());
			ps.setBigDecimal(5, BigDecimal.valueOf(animal.getWeight()));
			ps.setObject(6, animal.getAcquisitionDate());
			ps.setString(7, animal.getAcquisitionLocation());
			ps.setString(8, animal.getTrainingStatus());
			ps.setBoolean(9, animal.getReserved());
			ps.setString(10, animal.getInServiceLocation());
			
			ps.executeUpdate();
			
			try (ResultSet keys = ps.getGeneratedKeys()) {
				if (keys.next()) {
					return keys.getInt(1);
				}
				throw new SQLException("Insert succeeded but not animal_id was returned");
			}
		}
	}
	
	
	private void insertDog(Connection conn, int animalId, Dog dog) throws SQLException {
		try (PreparedStatement ps = conn.prepareStatement(INSERT_DOG_SQL)) {
			ps.setInt(1, animalId);
			ps.setString(2, dog.getBreed());
			ps.executeUpdate();
		}
	}
	
	private void insertMonkey(Connection conn, int animalId, Monkey monkey) throws SQLException {
		try (PreparedStatement ps = conn.prepareStatement(INSERT_MONKEY_SQL)) {
			ps.setInt(1, animalId);
			ps.setBigDecimal(2, BigDecimal.valueOf(monkey.getTailLength()));
			ps.setBigDecimal(3, BigDecimal.valueOf(monkey.getHeight()));
			ps.setBigDecimal(4, BigDecimal.valueOf(monkey.getBodyLength()));
			ps.setString(5, monkey.getSpecies());
			ps.executeUpdate();
			
		}
	}
	
	// ----------READ-----------------
	
	@Override
	public List<RescueAnimal> findAll() {
		List<RescueAnimal> animals = new ArrayList<>();
		
		try (Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(FIND_ALL_SQL);
			ResultSet rs = ps.executeQuery()) {
			
			while (rs.next()) {
				String type = rs.getString("animal_type");
				
				if ("dog".equalsIgnoreCase(type) && rs.getString("breed") != null) {
					animals.add(mapDog(rs));
				}
				else if ("monkey".equalsIgnoreCase(type) && rs.getString("species") != null) {
					animals.add(mapMonkey(rs));
				}
				else {
					LOGGER.warning("Animal has missing or has askew data, skipping: " + rs.getString("name"));
				}
			}
			
		}
		catch (SQLException e) {
			throw fail("findAll", e);
		}
		
		return animals;
	}
	
	@Override
	public List<Dog> findAllDogs() {
		List<Dog> dogs = new ArrayList<>();
		
		try (Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(FIND_DOGS_SQL);
			ResultSet rs = ps.executeQuery()) {
			
			while (rs.next()) {
				dogs.add(mapDog(rs));
			}
		}
		catch (SQLException e) {
			throw fail("findAllDogs", e);
		}
		
		return dogs;
	}
	
	@Override
	public List<Monkey> findAllMonkeys() {
		List<Monkey> monkeys = new ArrayList<>();
		
		try (Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(FIND_MONKEYS_SQL);
			ResultSet rs = ps.executeQuery()) {
			
			while (rs.next()) {
				monkeys.add(mapMonkey(rs));
			}
		}
		catch (SQLException e) {
			throw fail("findAllMonkeys", e);
		}
		
		return monkeys;
	}
	
	
	private Dog mapDog(ResultSet rs) throws SQLException {
		return new Dog(
				rs.getString("name"),
				rs.getString("breed"),
				rs.getString("gender"),
				rs.getInt("age"),
				rs.getDouble("weight"),
				rs.getObject("acquisition_date", LocalDate.class),
				rs.getString("acquisition_country"),
				rs.getString("training_status"),
				rs.getBoolean("reserved"),
				rs.getString("in_service_country"));				
	}
	
	private Monkey mapMonkey(ResultSet rs) throws SQLException {
		return new Monkey(
				rs.getString("name"),
				rs.getString("gender"),
				rs.getInt("age"),
				rs.getDouble("weight"),
				rs.getObject("acquisition_date", LocalDate.class),
				rs.getString("acquisition_country"),
				rs.getString("training_status"),
				rs.getBoolean("reserved"),
				rs.getString("in_service_country"),
				rs.getDouble("tail_length"),
				rs.getDouble("height"),
				rs.getDouble("body_length"),
				rs.getString("species"));			
	}
	
	
	
	// ------------UPDATE-----------------
	
	//writes the animal's current values back to the database, needed because objects returned 
	//by the find methods are copies of rows so changing in Java does nothing until update is called
	@Override
	public void update(RescueAnimal animal) {
		requireSupportedType(animal);
		
		try (Connection conn = getConnection()) {
			conn.setAutoCommit(false);
			
			try {
				updateAnimal(conn, animal);
				
				if(animal instanceof Dog) {
					updateDog(conn, (Dog) animal);
				}
				else {
					updateMonkey(conn, (Monkey) animal);
				}
				
				conn.commit();
			}
			catch (SQLException e) {
				conn.rollback();
				throw e;
			}
		}
		catch (SQLException e) {
			throw fail("update", e);
		}
	}
	
	private void updateAnimal(Connection conn, RescueAnimal animal) throws SQLException {
		try (PreparedStatement ps = conn.prepareStatement(UPDATE_ANIMAL_SQL)) {
			ps.setString(1, animal.getGender());
			ps.setInt(2, animal.getAge());
			ps.setBigDecimal(3, BigDecimal.valueOf(animal.getWeight()));
			ps.setObject(4, animal.getAcquisitionDate());
			ps.setString(5, animal.getAcquisitionLocation());
			ps.setString(6, animal.getTrainingStatus());
			ps.setBoolean(7, animal.getReserved());
			ps.setString(8, animal.getInServiceLocation());
			ps.setString(9, animal.getName());
			
			
			if (ps.executeUpdate() == 0) {
				throw new SQLException("No animal found with name: " + animal.getName());
			}
		}
	}
	
	
	private void updateDog(Connection conn, Dog dog) throws SQLException {
		try (PreparedStatement ps = conn.prepareStatement(UPDATE_DOG_SQL)) {
			ps.setString(1, dog.getBreed());
			ps.setString(2, dog.getName());
			ps.executeUpdate();
		}
	}
	
	private void updateMonkey(Connection conn, Monkey monkey) throws SQLException {
		try (PreparedStatement ps = conn.prepareStatement(UPDATE_MONKEY_SQL)) {
			ps.setBigDecimal(1, BigDecimal.valueOf(monkey.getTailLength()));
			ps.setBigDecimal(2, BigDecimal.valueOf(monkey.getHeight()));
			ps.setBigDecimal(3, BigDecimal.valueOf(monkey.getBodyLength()));
			ps.setString(4, monkey.getSpecies());
			ps.setString(5, monkey.getName());
			ps.executeUpdate();
		}
	}
	
	// ------------DELETE-----------
	@Override
	public boolean deleteByName(String name) {
		try (Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(DELETE_ANIMAL_SQL)) {
			
			ps.setString(1, name);
			
			return ps.executeUpdate() > 0;
		}
		catch (SQLException e) {
			throw fail("deleteByName", e);
		}
	}
		
		
		
	//only dogs and monkeys have tables, anything else is an error
	private void requireSupportedType(RescueAnimal animal) {
		if (!(animal instanceof Dog) && !(animal instanceof Monkey)) {
			throw new IllegalArgumentException("Unsupported animal type: " + animal.getClass().getName());
		}
	}
	
	
	
}