import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Business logic for the application
 * 
 * Depends upon the AnimalRepository interface and not on a concrete storage class.
 * Swapping to alternative repository's such as a database requires no change to this class
 * 
 * findAvailableAnimals() & findTrainedUnreserved() are similar but we need both to have one for listing all animals
 * for general inquiries and the other for retrieval with a specific country
 * 
 * 
 */

public class AnimalService {
	
	//Training status for animal that is ready for reservation
	private static final String FULLY_TRAINED = "in service";
	
	//List of valid monkey species
	private static final List<String> ALLOWED_SPECIES = Arrays.asList("Capuchin", "Guenon", "Macaque", "Marmoset", "Squirrel Monkey", "Tamarin");
	
	private final AnimalRepository repository;
	
	public AnimalService(AnimalRepository repository) {
		this.repository = repository;
	}
	
	
	//add animal to system if name is not taken
	public boolean intakeAnimal(RescueAnimal animal) {
		if (repository.existsByName(animal.getName())) {
			return false;
		}
		repository.add(animal);
		return true;
	}
	
	//reserves an animal
	public void reserve(RescueAnimal animal) {
		animal.setReserved(true);
		repository.update(animal);
	}
	
	//Get animals of selected type and service country that are not reserved
	public List<RescueAnimal> findAvailableAnimals(String animalType, String serviceCountry) {
		List<RescueAnimal> matches = new ArrayList<>();
		for (RescueAnimal animal : repository.findAll()) {
			if (animal.getAnimalType().equalsIgnoreCase(animalType) && animal.getInServiceLocation().equalsIgnoreCase(serviceCountry) && !animal.getReserved()) {
				matches.add(animal);
			}
		}
		return matches;
	}
	
	//get animals that are trained and not reserved
	public List<RescueAnimal> findTrainedUnreserved(String animalType) {
		List<RescueAnimal> matches = new ArrayList<>();
		for (RescueAnimal animal : repository.findAll()) {
			if (animal.getAnimalType().equalsIgnoreCase(animalType) && animal.getTrainingStatus().equalsIgnoreCase(FULLY_TRAINED) && !animal.getReserved()) {
				matches.add(animal);
			}
		}
		return matches;
	}
	
	public List<Dog> getAllDogs() {
		return repository.findAllDogs();
	}
	
	public List<Monkey> getAllMonkeys() {
		return repository.findAllMonkeys();
	}
	
	
	//true if string is a supported animal type
	public boolean isValidAnimalType(String animalType) {
		return "dog".equalsIgnoreCase(animalType) || "monkey".equalsIgnoreCase(animalType);
	}
	
	//true is monkey species is supported
	public boolean isValidSpecies(String species) {
		for (String allowed : ALLOWED_SPECIES) {
			if (allowed.equalsIgnoreCase(species)) {
				return true;
			}
		}
		return false;
	}
	
	//retrieves list of valid monkey species for displaying in prompts
	public List<String> getAllowedSpecies() {
		return new ArrayList<>(ALLOWED_SPECIES);
	}
	



}
