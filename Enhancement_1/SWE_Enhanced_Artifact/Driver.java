import java.util.Scanner;
import java.util.List;

/*
 * Entry point for application where each layer is created and put together.
 * Starts user interface and also creates the static data for testing until a permanent database is implemented
 * 
 * 
 */


public class Driver {

    // Instance variables (if needed)

    public static void main(String[] args) {
    	
    	
    	// This is where the repository is created and can easily be swapped for alternative repository's in the future
    	// because AnimalRepository acts as an interface
    	AnimalRepository repository = new InMemoryRepository();
    	AnimalService service = new AnimalService(repository);
    	
    	seedTestData(service);
    	
    	//try block to close scanner
    	try (Scanner scanner = new Scanner(System.in)){
    		ConsoleUI ui = new ConsoleUI(service, scanner);
    		ui.run();
    	}
    }

    //sample data for testing purposes
    private static void seedTestData(AnimalService service) {
        service.intakeAnimal(new Dog("Spot", "German Shepherd", "male", "1", "25.6", "05-12-2019", "United States", "intake", false, "United States"));
        service.intakeAnimal(new Dog("Rex", "Great Dane", "male", "3", "35.2", "02-03-2020", "United States", "Phase I", false, "United States"));
        service.intakeAnimal(new Dog("Bella", "Chihuahua", "female", "4", "25.6", "12-12-2019", "Canada", "in service", true, "Canada"));
        service.intakeAnimal(new Dog("Cupcake", "Pitbull", "male", "3", "30.0", "12-25-2018", "Mexico", "in service", false, "Brazil"));
        
        service.intakeAnimal(new Monkey("Coco", "male", "3", "140", "01-22-2018", "Brazil", "in service", false, "Mexico", "6", "4", "3", "Tamarin"));
    }
}
