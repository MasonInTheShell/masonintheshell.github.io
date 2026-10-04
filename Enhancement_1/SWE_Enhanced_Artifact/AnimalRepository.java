import java.util.List;

/*
 * Abstraction for animal storage
 * 
 * The service layer will depend on this interface rather than a specific storage mechanism.
 * 
 * Currently the only implementation stores animal data in memory, but later jdbc animalrepo can implement the same operations against MySQL
 * without changing the service layer
 * 
 * 
 */



public interface AnimalRepository {
	
	
	//Stores new animal
	void add(RescueAnimal animal);
	
	//Returns all animals
	List<RescueAnimal> findAll();
	
	//Returns all dogs
	List<Dog> findAllDogs();
	
	//Returns all monkeys
	List<Monkey> findAllMonkeys();
	
	//True if name is already taken by animal
	boolean existsByName(String name);
	
	
	//This will be used for the future JDBC/MySql repo
	void update(RescueAnimal animal);
	
	

}
