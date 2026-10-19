import java.util.List;

/*
 * Abstraction for animal storage
 * 
 * The service layer will depend on this interface rather than a specific storage mechanism.
 * 
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
	
	
	//Saves the animal's current values (only used for jdbc repo)
	void update(RescueAnimal animal);
	
	//removes animal with specified name. True if removed, false if none match
	boolean deleteByName(String name);
	
	

}
