import java.util.Scanner;
import java.util.List;
import java.time.LocalDate;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/*
 * Entry point for application where each layer is created and put together.
 * Chooses the storage implementation, builds service and UI and starts the UI.
 * Database settings are read from db.properties to keep credentials outside of source code
 * 
 */


public class Driver {

	// true - MySQL database
	// false - in-memory storage
	private static final boolean USE_DATABASE = true;
	
	//located in root. can be changed if alternative location is preferred
	private static final String CONFIG_FILE = "db.properties";


    public static void main(String[] args) {
    	
    	
    	// This is where the repository is created and can easily be swapped for alternative repository's in the future
    	// because AnimalRepository acts as an interface
    	AnimalRepository repository;
    	
    	if (USE_DATABASE) {
    		repository = createJdbcRepository();
    		if (repository == null) {
    			return;
    		}
    	}
    	else {
    		repository = new InMemoryRepository();
    		System.out.println("Using in-memory storage. Data will be lost on exiting application!");
    	}
    	
    	
    	AnimalService service = new AnimalService(repository);
    	
    	//uncomment to use with in-memory storage otherwise does not run
    	//seedTestData(service);
    	
    	//try block to close scanner
    	try (Scanner scanner = new Scanner(System.in)){
    		ConsoleUI ui = new ConsoleUI(service, scanner);
    		ui.run();
    	}
    }
    
    //reads db.properties, builds the jdbc repo and checks that mysql is reachable.
    private static AnimalRepository createJdbcRepository() {
    	Properties props = new Properties();
    	
    	try (FileInputStream in = new FileInputStream(CONFIG_FILE)) {
    		props.load(in);
    	}
    	catch (IOException e) {
    		System.out.println("Could not read " + CONFIG_FILE + ". Make sure file exists in project folder.");
    		return null;
    	}
    	
    	String url = props.getProperty("db.url");
    	String user = props.getProperty("db.user");
    	String password = props.getProperty("db.password");
    	
    	if (url == null || user == null || password == null) {
    		System.out.println(CONFIG_FILE + " must contain db.url, db.user and db.password.");
    		return null;
    	}
    	
    	
    	JdbcAnimalRepository jdbcRepository = new JdbcAnimalRepository(url, user, password);
    	
    	try {
    		jdbcRepository.verifyConnection();
    	}
    	catch (RepositoryException e) {
    		System.out.println("Could not connect to the database. Check that MySQL is running "
    				+ "and that the setting in " + CONFIG_FILE + " are correct.");
    		return null;
    	}
    	
    	return jdbcRepository;
    	
    	
    }

    //sample data for testing purposes
    private static void seedTestData(AnimalService service) {
        service.intakeAnimal(new Dog("Spot", "German Shepherd", "male", 1, 25.6, LocalDate.of(2019, 5, 12), "United States", "intake", false, "United States"));
        service.intakeAnimal(new Dog("Rex", "Great Dane", "male", 3, 35.2, LocalDate.of(2020, 2, 3), "United States", "Phase I", false, "United States"));
        service.intakeAnimal(new Dog("Bella", "Chihuahua", "female", 4, 25.6, LocalDate.of(2019, 12, 12), "Canada", "in service", true, "Canada"));
        service.intakeAnimal(new Dog("Cupcake", "Pitbull", "male", 3, 30.0, LocalDate.of(2018, 12, 25), "Mexico", "in service", false, "Brazil"));
        
        service.intakeAnimal(new Monkey("Coco", "male", 3, 140, LocalDate.of(2018, 1, 22), "Brazil", "in service", false, "Mexico", 6, 4, 3, "Tamarin"));
    }
}
