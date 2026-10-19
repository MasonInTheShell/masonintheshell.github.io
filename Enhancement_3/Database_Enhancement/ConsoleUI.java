import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;



/*
 * Console interface
 * 
 * Displays menus, reads inputs, and prints results
 * 
 * Logic rules are all inside AnimalService, so console never interacts with repository directly
 * 
 * Text, numeric and date inputs are validated here (re-prompting until valid) so that only
 * correctly typed values ever reach the model and the database.
 * 
 */


public class ConsoleUI {
	
	//text character limits to match VARCHAR in the schema.sql
	private static final int MAX_NAME = 50;
	private static final int MAX_GENDER = 20;
	private static final int MAX_BREED = 50;
	private static final int MAX_COUNTRY = 50;
	private static final int MAX_TRAINING_STATUS = 30;
	
	//numeric limits to match in the schema.sql
	private static final int MAX_AGE = 100;
	private static final double MAX_WEIGHT = 349.99;
	private static final double MAX_TAIL_LENGTH = 39.99;
	private static final double MAX_HEIGHT = 39.99;
	private static final double MAX_BODY_LENGTH = 39.99;
	
	//dates entered as MM-dd-yyyy. STRICT prevents impossible dates
	private static final DateTimeFormatter DATE_FORMAT = 
			DateTimeFormatter.ofPattern("MM-dd-uuuu").withResolverStyle(ResolverStyle.STRICT);
	
	
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
        	else if(userInput.equals("7")) {
        		removeAnimal();
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
        System.out.println("[7] Remove an animal");
        System.out.println("[q] Quit application");
        System.out.println();
        System.out.println("Enter a menu selection");
    }
    
    
    public void intakeNewDog() {
        String name = promptText("What is the dog's name?", MAX_NAME);
        String breed = promptText("What is the dog's breed?", MAX_BREED);
        String gender = promptText("What is the dog's gender?", MAX_GENDER);
        int age = promptInt("What is the dog's age?", 0, MAX_AGE);
        double weight = promptDecimal("What is the dog's weight?", MAX_WEIGHT);
        LocalDate acquisitionDate = promptDate("What is the dog's acquisition date? (MM-dd-yyy, ex. 01-29-2026)");
        String acquisitionCountry = promptText("What is the dog's acquisition country?", MAX_COUNTRY);
        String trainingStatus = promptText("What is the dog's training status", MAX_TRAINING_STATUS);
        boolean reserved = promptYesNo("Is this dog reserved? (Y/N)");
        String inServiceCountry = promptText("What country is the dog currently in service?", MAX_COUNTRY);

        //creating a new instance of the dog class with the entered info and adding it to the array    	
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
    	String name = promptText("What is the monkey's name?", MAX_NAME);
    	String gender = promptText("What is the monkey's gender?", MAX_GENDER);
    	int age = promptInt("What is the monkey's age?", 0, MAX_AGE);
    	double weight = promptDecimal("What is the monkey's weight?", MAX_WEIGHT);
    	LocalDate acquisitionDate = promptDate("What is the monkey's acquisition date? (MM-dd-yyy, ex. 01-29-2026)");
    	String acquisitionCountry = promptText("What is the monkey's acquisition country?", MAX_COUNTRY);
    	String trainingStatus = promptText("What is the monkey's training status", MAX_TRAINING_STATUS);
    	boolean reserved = promptYesNo("Is this monkey reserved? (Y/N)");
    	String inServiceCountry = promptText("What country is the monkey currently in service?", MAX_COUNTRY);
    	double tailLength = promptDecimal("What is the monkey's tail length?", MAX_TAIL_LENGTH);
    	double height = promptDecimal("What is the monkey's height?", MAX_HEIGHT);
    	double bodyLength = promptDecimal("What is the monkey's body length?", MAX_BODY_LENGTH);
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
    		
    		if (promptYesNo("Would you like to reserve " + animal.getName() + "? (Y/N)")) {
    			service.reserve(animal);
    			System.out.println(animal.getName() + " reserved");
    			return;
    		}
    	}
    	
    	System.out.println("No reservation made. Returning to menu");
    	
    }
    
    //permanently removes an animal after user confirmation
    public void removeAnimal() {
    	String name = prompt("Enter the name of the animal to remove").trim();
    	
    	if (name.isEmpty() || !service.animalExists(name)) {
    		System.out.println("No animal named \"" + name +"\" was found. Returning to menu");
    		return;
    	}
    	
    	if (!promptYesNo("Are you sure you want to remove " + name + ". Are you sure? (Y/N)")) {
    		System.out.println("Cancelled. Returning to menu");
    		return;
    	}
    	
    	if(service.removeAnimal(name)) {
    		System.out.println(name + " has been removed");
    	}
    	else {
    		System.out.println("Could not remove " + name + ". Returning to menu");
    	}
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
    
    
    //prompts until non-empty text no longer than maxLength is entered
    private String promptText(String question, int maxLength) {
    	while(true) {
    		String input = prompt(question).trim();
    		
    		if(input.isEmpty()) {
    			System.out.println("This field cannot be empty");
    		}
    		else if (input.length() > maxLength) {
    			System.out.println("Too long. Field cannot be longer than " + maxLength + " characters");
    		}
    		else {
    			return input;
    		}
    	}
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
    
    //prompts until a whole number between min and max is entered
    private int promptInt(String question, int min, int max) {
    	while (true) {
    		String input = prompt(question).trim();
    		
    		try {
    			int value = Integer.parseInt(input);
    			if (value >= min && value <= max) {
    				return value;
    			}
    			System.out.println("Please enter a number between " + min + " and " + max);
    		}
    		catch (NumberFormatException e) {
    			System.out.println("Please enter a whole number (ex. 4)");
    		}
    	}
    }
    
    
    //prompts until a positive number with at most 2 decimal places is entered
    private double promptDecimal(String question, double max) {
    	while (true) {
    		String input = prompt(question).trim();
    		
    		//rejects letters, signs NaN and the like
    		if (!input.matches("\\d+(\\.\\d{1,2})?")) {
    			System.out.println("Please enter a positive number with up to 2 decimal places (ex. 5.60)");
    			continue;
    		}
    		
    		
    		double value = Double.parseDouble(input);
    		if (value <= 0 || value > max) {
    			System.out.println("Please enter a number greater than 0 and no larger than " + max);
    			continue;
    		}
    		
    		return value;
    	}
    }
    
    //prompts until real calender date is entered
    private LocalDate promptDate(String question) {
    	while (true) {
    		String input = prompt(question).trim();
    		
    		try {
    			return LocalDate.parse(input, DATE_FORMAT);
    		}
    		catch (DateTimeParseException e) {
    			System.out.println("Please enter a valid date as MM-dd-yyy, ex. 01-29-2026");
    		}
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
