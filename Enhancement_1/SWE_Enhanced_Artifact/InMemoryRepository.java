import java.util.ArrayList;
import java.util.List;

/*
 * Stores animals in Java collection in memory
 * 
 */

public class InMemoryRepository implements AnimalRepository{
	
	private final List<RescueAnimal> animals = new ArrayList<>();
	
	
	@Override
	public void add(RescueAnimal animal) {
		animals.add(animal);
	}
	
	@Override
	public List<RescueAnimal> findAll() {
		
		return new ArrayList<>(animals);
	}
	
	
	@Override
	public List<Dog> findAllDogs() {
		List<Dog> dogs = new ArrayList<>();
		for (RescueAnimal animal : animals) {
			if (animal instanceof Dog) {
				dogs.add((Dog) animal);
			}
		}
		return dogs;
	}
	
	@Override
	public List<Monkey> findAllMonkeys() {
		List<Monkey> monkeys = new ArrayList<>();
		for (RescueAnimal animal : animals) {
			if (animal instanceof Monkey) {
				monkeys.add((Monkey) animal);
			}
		}
		return monkeys;
	}
	
	
	@Override
	public boolean existsByName(String name) {
		for (RescueAnimal animal : animals) {
			if (animal.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
	
	//In-memory repository does not require this method because the animal object is already stored in the list
	//and changes are made directly, but it required by the Animal Repository
	@Override
	public void update(RescueAnimal animal) {
		
	}
	
	

}
