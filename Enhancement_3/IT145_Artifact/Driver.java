import java.util.ArrayList;
import java.util.Scanner;

public class Driver {
    private static ArrayList<Dog> dogList = new ArrayList<Dog>();
    private static ArrayList<Monkey> monkeyList = new ArrayList<Monkey>();
    // Instance variables (if needed)

    public static void main(String[] args) {

    	//populating the array with data for testing
        initializeDogList();
        initializeMonkeyList();


        
        Scanner scanner = new Scanner(System.in);
        String userInput = "";
        
        //This loops the menu until the user enters q to quit
        while (!userInput.equals("q")) {
        	displayMenu();
        	userInput = scanner.nextLine();
        	if(userInput.equals("1")) {
        		intakeNewDog(scanner);
        	}
        	else if (userInput.equals("2")) {
        		intakeNewMonkey(scanner);
        	}
        	else if (userInput.equals("3")) {
        		reserveAnimal(scanner);
        	}
        	else if(userInput.equals("4")) {
        		printAnimals(userInput);
        	}
        	else if(userInput.equals("5")) {
        		printAnimals(userInput);
        	}
        	else if(userInput.equals("6")) {
        		printAnimals(userInput);
        	}
        	else if (!userInput.equals("q")){
        		System.out.println("Enter a valid command (ex. 1-6 or q)");
        	}
        }
        
            
        
        
        
        
        
    }

    // This method prints the menu options
    public static void displayMenu() {
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


    // Adds dogs to a list for testing
    public static void initializeDogList() {
        Dog dog1 = new Dog("Spot", "German Shepherd", "male", "1", "25.6", "05-12-2019", "United States", "intake", false, "United States");
        Dog dog2 = new Dog("Rex", "Great Dane", "male", "3", "35.2", "02-03-2020", "United States", "Phase I", false, "United States");
        Dog dog3 = new Dog("Bella", "Chihuahua", "female", "4", "25.6", "12-12-2019", "Canada", "in service", true, "Canada");
        Dog dog4 = new Dog("Cupcake", "Pitbull", "male", "3", "30.0", "12-25-2018", "Mexico", "in service", false, "Brazil");

        dogList.add(dog1);
        dogList.add(dog2);
        dogList.add(dog3);
        dogList.add(dog4);
    }


    // Adds monkeys to a list for testing
    //Optional for testing
    public static void initializeMonkeyList() {
    	Monkey monkey1 = new Monkey("Coco", "male", "3", "140", "01-22-2018", "Brazil", "in service", false, "Mexico", "6", "4", "3", "Tamarin");
    	
    	monkeyList.add(monkey1);
    	
    }


    public static void intakeNewDog(Scanner scanner) {
        System.out.println("What is the dog's name?");
        String name = scanner.nextLine();
        for(Dog dog: dogList) {
            if(dog.getName().equalsIgnoreCase(name)) {
                System.out.println("\n\nThis dog is already in our system. Returning to menu.\n\n");
                return; //returns to menu
            }
        }
        //prompt for dog details
        System.out.println("What is the dog's breed?");
        String breed = scanner.nextLine();
            	
        System.out.println("What is the dog's gender?");
        String gender = scanner.nextLine();
            	
        System.out.println("What is the dog's age?");
        String age = scanner.nextLine();
            	
        System.out.println("What is the dog's weight?");
        String weight = scanner.nextLine();
            	
        System.out.println("What is the dog's acquisition date? (ex.XX-XX-XXXX)");
        String acquisitionDate = scanner.nextLine();
            	
        System.out.println("What is the dog's acquisition country?");
        String acquisitionCountry = scanner.nextLine();
            	
        System.out.println("What is the dog's training status");
        String trainingStatus = scanner.nextLine();
            	
        System.out.println("Is this dog reserved? (Y/N)");
        String reservedInput = scanner.nextLine();
        boolean reserved;
        //sets the reserved status to true or false based on user user input and defaults to false if
        //user enters invalid input
        if (reservedInput.equalsIgnoreCase("Y")) {
        	reserved = true;
            }
            else if (reservedInput.equalsIgnoreCase("N")) {
            	reserved = false;
            }
            else {
            	reserved = false;
            }
            	
        System.out.println("What country is the dog currently in service?");
        String inServiceCountry = scanner.nextLine();
            	
            				
        //creates a new instance of the Dog class with the information the user just entered and adds it to the array    	
        Dog newDog = new Dog(name, breed, gender, age, weight, acquisitionDate, acquisitionCountry, trainingStatus,
            		reserved, inServiceCountry);
            	
        dogList.add(newDog);
        System.out.print("Added new dog to the list");
        
        }


 



    public static void intakeNewMonkey(Scanner scanner) {  
    	
    	//List of allowed monkey species
    	String[] allowedSpecies = {"Capuchin", "Guenon", "Macaque", "Marmoset", "Squirrel Monkey", "Tamarin"};
    	
        System.out.println("What is the monkey's name?");
        String name = scanner.nextLine();
        		//checking if the monkey already exists
                for(Monkey monkey: monkeyList) {
                    if(monkey.getName().equalsIgnoreCase(name)) {
                        System.out.println("\n\nThis monkey is already in our system. Returning to menu.\n\n");
                        return; //returns to menu
                    }
                }
                //prompt for monkey details
                System.out.println("What is the monkey's gender?");
                String gender = scanner.nextLine();
                    	
                System.out.println("What is the monkey's age?");
                String age = scanner.nextLine();
                    	
                System.out.println("What is the monkey's weight?");
                String weight = scanner.nextLine();
                    	
                System.out.println("What is the monkey's acquisition date? (ex.XX-XX-XXXX)");
                String acquisitionDate = scanner.nextLine();
                    	
                System.out.println("What is the monkey's acquisition country?");
                String acquisitionCountry = scanner.nextLine();
                    	
                System.out.println("What is the monkey's training status");
                String trainingStatus = scanner.nextLine();
                    	
                System.out.println("Is this monkey reserved? (Y/N)");
                String reservedInput = scanner.nextLine();
                boolean reserved;
                //sets the reserved status based on user input and defaults to false if
                //user enters invalid input
                if (reservedInput.equalsIgnoreCase("Y")) {
                	reserved = true;
                    }
                    else if (reservedInput.equalsIgnoreCase("N")) {
                    	reserved = false;
                    }
                    else {
                    	reserved = false;
                    }
                    	
                System.out.println("What country is the monkey currently in service?");
                String inServiceCountry = scanner.nextLine();
                
                System.out.println("What is the monkey's tail length?");
                String tailLength = scanner.nextLine();
                
                System.out.println("What is the monkey's height?");
                String height = scanner.nextLine();
                
                System.out.println("What is the monkey's body length?");
                String bodyLength = scanner.nextLine();
                
                //Validate monkey species
                String species;
                while (true) {
                	System.out.println("What is the monkey's species?");
                	species = scanner.nextLine();
                	
                	//check the list of allowed monkeys
                	boolean validSpecies = false;
                	for (String allowed : allowedSpecies) {
                		if (allowed.equalsIgnoreCase(species)) {
                			validSpecies = true;
                			break;
                		}
                	}
                	
                	if (validSpecies) {
                		break;
                	}
                	else {
                		System.out.println("Invalid species. The allowed species are: Capuchin, Guenon, Macaque,"
                				+ " Marmoset, Squirrel Monkey or Tamarin");
                	}
                }
                
                    	
                    				
                //creating a new instance of the monkey class with the entered info and adding it to the array    	
                Monkey newMonkey = new Monkey(name, gender, age, weight, acquisitionDate, acquisitionCountry, trainingStatus,
                    		reserved, inServiceCountry, tailLength, height, bodyLength, species);
                    	
                monkeyList.add(newMonkey);
                System.out.print("Added new Monkey to the list");
                
                }
        

        // Complete reserveAnimal
        // You will need to find the animal by animal type and in service country
        public static void reserveAnimal(Scanner scanner) {
        	System.out.println("Do you want to reserve a dog or a monkey? (enter 'dog' or 'monkey')");
        	String animalType = scanner.nextLine();
        	
        	//only allowing user to enter dog or monkey
        	if (!animalType.equalsIgnoreCase("dog") && !animalType.equalsIgnoreCase("monkey")) {
        		System.out.println("Invalid animal type. Returning to menu");
        		return;
        	}
        	
        	System.out.println("Enter service country");
        	String serviceCountry = scanner.nextLine();
        	
        	boolean available = false;
        	
        	//looping through the list of dogs to find which ones match the desired country and are not reserved
        	if (animalType.equalsIgnoreCase("dog")) {
        		for (Dog dog : dogList) {
        			if (dog.getInServiceLocation().equalsIgnoreCase(serviceCountry) && !dog.getReserved()) {
        				System.out.println(dog.getName() + " is available in " + dog.getInServiceLocation());
        				System.out.println("Would you like to reserve this dog? (Y/N)");
        				String choice = scanner.nextLine();
        				
        				//updating the dog's reserved status to true if the user chooses yes
        				if (choice.equalsIgnoreCase("Y")) {
        					dog.setReserved(true);
        					System.out.println("Dog reserved");
        				}
        				//returns to menu if user does not enter yes
        				else {
        					System.out.print("Cancelling reservation. Returning to menu.");
        				}
        				
        				available = true;
        				break;
        			}
        		}
        	}
        	//looping through list of monkeys to find which ones match the desired country and are not reserved
        	else if (animalType.equalsIgnoreCase("monkey")) {
        		for (Monkey monkey : monkeyList) {
        			if (monkey.getInServiceLocation().equalsIgnoreCase(serviceCountry) && !monkey.getReserved()) {
        				System.out.println(monkey.getName() + " is available in " + monkey.getInServiceLocation());
        				System.out.println("Would you like to reserve this monkey? (Y/N)");
        				String choice = scanner.nextLine();
        				
        				//updates the monkey's reserved status to true if the user chooses yes
        				if (choice.equalsIgnoreCase("Y")) {
        					monkey.setReserved(true);
        					System.out.println("Monkey reserved");
        				}
        				//returns to the menu if the user does not enter yes
        				else {
        					System.out.println("Cancelling reservation. Returning to menu.");
        				}
        				
        				available = true;
        				break;
        			}
        		}
        	}

        	//prints statement to user if the desired animal is not available in the desired country
        	if (!available) {
        		System.out.println("No available " + animalType + "s in " + serviceCountry);
        	}
        	
        }


        
        public static void printAnimals(String userInput) {
        	if (userInput.equals("4")) {
        		System.out.println("The method printAnimals needs to be fully implemented");
        		for (Dog dog : dogList) {
        			System.out.print("Name: " + dog.getName());
        			System.out.print(" Breed: " + dog.getBreed());
        			System.out.println();
        		}
        	}
        	
        	
        	if (userInput.equals("5")) {
        		System.out.println("The method printAnimals needs to be fully implemented");
        		for (Monkey monkey : monkeyList) {
        			System.out.print("Name: " + monkey.getName());
        			System.out.print(" Species: " + monkey.getSpecies());
        			System.out.println();
        		}
        	}
        	
        	//This is the method I chose to fully implement
        	if (userInput.equals("6")) {
        		System.out.println("Printing list of fully trained animals that are not reserved:");
        		System.out.println("Dogs:");
        		
        		boolean dogFound = false;
        		
        		//loops through the array of dogs to find which ones are fully trained and not reserved
        		//and prints those to the user
        		for (Dog dog : dogList) {
        			if (dog.getTrainingStatus().equalsIgnoreCase("in service") && !dog.getReserved()) {
        				System.out.println(dog.getName() + " is available and not reserved");
        				dogFound = true;
        			}
        		}
        		
        		//if no dogs are found meeting the conditions prints "none"
        		if (!dogFound) {
        			System.out.println("None");
        		}
        		
        		//separating statement
        		System.out.println("Monkeys:");
        		
        		boolean monkeyFound = false;
        		
        		//loops through the array of monkeys to find which ones are fully trained and no reserved
        		//and prints those to the user
        		for (Monkey monkey : monkeyList) {
        			if (monkey.getTrainingStatus().equalsIgnoreCase("in service") && !monkey.getReserved()) {
        				System.out.println(monkey.getName() + " is available and not reserved");
        				monkeyFound = true;
        			}
        		}
        		
        		//if no monkeys are found meeting the conditions prints "none"
        		if (!monkeyFound) {
        			System.out.println("None");
        		}

        	}

        }
}

