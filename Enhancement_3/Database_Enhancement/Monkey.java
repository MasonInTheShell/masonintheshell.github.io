import java.time.LocalDate;

public class Monkey extends RescueAnimal {
	
	
	//instance variables
	private double tailLength;
	private double height;
	private double bodyLength;
	private String species;
	
	//constructor
    public Monkey(String name, String gender, int age,
    double weight, LocalDate acquisitionDate, String acquisitionCountry,
	String trainingStatus, boolean reserved, String inServiceCountry,
	double tailLength, double height, double bodyLength, String species) {
        setName(name);
        setAnimalType("monkey");
        setGender(gender);
        setAge(age);
        setWeight(weight);
        setAcquisitionDate(acquisitionDate);
        setAcquisitionLocation(acquisitionCountry);
        setTrainingStatus(trainingStatus);
        setReserved(reserved);
        setInServiceCountry(inServiceCountry);
        setTailLength(tailLength);
        setHeight(height);
        setBodyLength(bodyLength);
        setSpecies(species);

    }
	
    // Accessor Method
    public double getTailLength() {
        return tailLength;
    }

    // Mutator Method
    public void setTailLength(double tailLength) {
        this.tailLength = tailLength;
    }
	
    //Accessor
    public double getHeight() {
    	return height;
    }
    
    //mutator
    public void setHeight(double height) {
    	this.height = height;
    }
   
    //accessor
    public double getBodyLength() {
    	return bodyLength;
    }
    
    //mutator
    public void setBodyLength(double bodyLength) {
    	this.bodyLength = bodyLength;
    }
    
    //accessor
    public String getSpecies() {
    	return species;
    }
    
    //mutator
    public void setSpecies(String species) {
    	this.species = species;
    }
    
    
}
