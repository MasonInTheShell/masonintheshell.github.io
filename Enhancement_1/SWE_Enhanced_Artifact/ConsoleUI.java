import java.util.List;
import java.util.Scanner;


/*
 * Console interface
 * 
 * Displays menus, reads inputs, and prints results
 * 
 * Logic rules are all inside AnimalService, so console never interacts with repository directly
 * 
 * 
 */


public class ConsoleUI {
	
	
	private final AnimalService service;
	private final Scanner scanner;
	
	public ConsoleUI(AnimalService service, Scanner scanner) {
		this.service = service;
		this.scanner = scanner;
	}
	
	
	public void run() {
		
		
		String userInput = "";
        
        //This loops the menu until the user enters q to quit
        while (!userInput.equals("q")) {
        	displayMenu();
        	userInput = scanner.nextLine();
        	
        	if(userInput.equals("1")) {
        		intakeNewDog();
        	}
        	else if (userInput.equals("2")) {
        		intakeNewMonkey();
        	}
        	else if (userInput.equals("3")) {
        		reserveAnimal();
        	}
        	else if(userInput.equals("4")) {
        		printDogs();
        	}
        	else if(userInput.equals("5")) {
        		printMonkeys();
        	}
        	else if(userInput.equals("6")) {
        		printAvailableAnimals();
        	}
        	else if (!userInput.equals("q")){
        		System.out.println("Enter a valid command (ex. 1-6 or q)");
        	}
        }
		
	}
	
    // This method prints the menu options
    public void displayMenu() {
        System.out.println("\n\n");
        System.out.println("\t\t\t\tRescue Animal System Menu");
        System.out.println("[1] Intake a new dog");
        System.out.println("[2] Intake a new monkey");
        System.out.println("[3] Reserve an animal");
        System.out.println("[4] Print a list of all dogs");
        System.out.println("[5] Print a list of all monkeys");
        System.out.println("[6] Print a list of all animals that are not reserved");
        System.out.println("[q] Quit application");
        System.out.println();
        System.out.println("Enter a menu selection");
    }
    
    
    public void intakeNewDog() {
        String name = prompt("What is the dog's name?");
        String breed = prompt("What is the dog's breed?");
        String gender = prompt("What is the dog's gender?");
        String age = prompt("What is the dog's age?");
        String weight = prompt("What is the dog's weight?");
        String acquisitionDate = prompt("What is the dog's acquisition date? (ex.XX-XX-XXXX)");
        String acquisitionCountry = prompt("What is the dog's acquisition country?");
        String trainingStatus = prompt("What is the dog's training status");
        boolean reserved = promptYesNo("Is this dog reserved? (Y/N)");
        String inServiceCountry = prompt("What country is the dog currently in service?");

    					
        Dog newDog = new Dog(name, breed, gender, age, weight, acquisitionDate, acquisitionCountry, trainingStatus,
            		reserved, inServiceCountry);
            	
        if (service.intakeAnimal(newDog)) {
        	System.out.println("Added new dog to the list");
        }
        else {
        	System.out.println("This name is already in use. Returning to menu");
        }
    }
    
    
    
    public void intakeNewMonkey() {  
    	String name = prompt("What is the monkey's name?");
    	String gender = prompt("What is the monkey's gender?");
    	String age = prompt("What is the monkey's age?");
    	String weight = prompt("What is the monkey's weight?");
    	String acquisitionDate = prompt("What is the monkey's acquisition date? (ex.XX-XX-XXXX)");
    	String acquisitionCountry = prompt("What is the monkey's acquisition country?");
    	String trainingStatus = prompt("What is the monkey's training status");
    	boolean reserved = promptYesNo("Is this monkey reserved? (Y/N)");
    	String inServiceCountry = prompt("What country is the monkey currently in service?");
    	String tailLength = prompt("What is the monkey's tail length?");
    	String height = prompt("What is the monkey's height?");
    	String bodyLength = prompt("What is the monkey's body length?");
    	String species = promptSpecies();
        
            				
        //creating a new instance of the monkey class with the entered info and adding it to the array    	
        Monkey newMonkey = new Monkey(name, gender, age, weight, acquisitionDate, acquisitionCountry, trainingStatus,
            		reserved, inServiceCountry, tailLength, height, bodyLength, species);
            	
        if (service.intakeAnimal(newMonkey)) {
        	System.out.println("Added new Monkey to the list");
        }
        else {
        	System.out.println("This name is already in use. Returning to menu");
        }
        
    }
    
    
    // Find the animal by animal type and in service country
    public void reserveAnimal() {

    	String animalType = prompt("Do you want to reserve a dog or a monkey? (enter 'dog' or 'monkey')");
    	
    	//only allowing user to enter dog or monkey
    	if (!service.isValidAnimalType(animalType)) {
    		System.out.println("Invalid animal type. Returning to menu");
    		return;
    	}
    	
    	String serviceCountry = prompt("Enter service country");
    	
    	List<RescueAnimal> available = service.findAvailableAnimals(animalType, serviceCountry);
    	
    	if (available.isEmpty()) {
    		System.out.println("No available " + animalType + "s in " + serviceCountry);
    		return;
    	}
    	
    	// offer each available animal until one is reserved or the list runs out
    	for (RescueAnimal animal : available) {
    		System.out.println(animal.getName() + " is available in " + animal.getInServiceLocation());
    		
    		if (promptYesNo("Would you like to reserve " + animal.getName() + "? (Y?N)")) {
    			service.reserve(animal);
    			System.out.println(animal.getName() + " reserved");
    			return;
    		}
    	}
    	
    	System.out.println("No reservation made. Returning to menu");
    	
    }
    
    //print all dogs
    public void printDogs() {
    	for (Dog dog : service.getAllDogs()) {
			System.out.print("Name: " + dog.getName());
			System.out.print(" Breed: " + dog.getBreed());
			System.out.println();
    		}
    	}
    
    //print all monkeys
    public void printMonkeys() {
		for (Monkey monkey : service.getAllMonkeys()) {
			System.out.print("Name: " + monkey.getName());
			System.out.print(" Species: " + monkey.getSpecies());
			System.out.println();
    		}
    	}
    
    //print all animals that are fully trained and not reserved
    public void printAvailableAnimals() {
    		System.out.println("Printing list of fully trained animals that are not reserved:");
    		
    		System.out.println("Dogs:");
    		printNamesOrNone(service.findTrainedUnreserved("dog"));
    		
    		System.out.println("Monkeys:");
    		printNamesOrNone(service.findTrainedUnreserved("monkey"));
    }
    
    
    //print all animal names or none if list is empty
	private static void printNamesOrNone(List<RescueAnimal> animals) {
		if (animals.isEmpty()) {
			System.out.println("None");
			return;
		}
		for (RescueAnimal animal : animals) {
			System.out.println(animal.getName() + " is available and not reserved");
		}
	}
    
	
    //prompts the question and returns user's input
    private String prompt(String question) {
    	System.out.println(question);
    	return scanner.nextLine();
    }
    
    
    //prompts until 'Y' or 'N' is entered
    private boolean promptYesNo(String question) {
    	while (true) {
    		String answer = prompt(question).trim();
    		
    		if (answer.equalsIgnoreCase("Y")) {
    			return true;
    		}
    		if (answer.equalsIgnoreCase("N")) {
    			return false;
    		}
    		
    		System.out.println("Please enter Y or N");
    	}
    }
    
    //Asks for species until valid species is accepted
    private String promptSpecies() {
    	while(true) {
    		String species = prompt("What is the monkey's species?");
    		
    		if (service.isValidSpecies(species)) {
    			return species;
    		}
    		
    		System.out.println("Invalid species. The allowed species are: " + String.join(", ", service.getAllowedSpecies()));
    	}
    }
	
	

}
