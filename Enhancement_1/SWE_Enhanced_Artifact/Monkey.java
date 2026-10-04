
public class Monkey extends RescueAnimal {
	
	
	//instance variables
	private String tailLength;
	private String height;
	private String bodyLength;
	private String species;
	
	//constructor
    public Monkey(String name, String gender, String age,
    String weight, String acquisitionDate, String acquisitionCountry,
	String trainingStatus, boolean reserved, String inServiceCountry,
	String tailLength, String height, String bodyLength, String species) {
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
    public String getTailLength() {
        return tailLength;
    }

    // Mutator Method
    public void setTailLength(String tailLength) {
        this.tailLength = tailLength;
    }
	
    //Accessor
    public String getHeight() {
    	return height;
    }
    
    //mutator
    public void setHeight(String height) {
    	this.height = height;
    }
   
    //accessor
    public String getBodyLength() {
    	return bodyLength;
    }
    
    //mutator
    public void setBodyLength(String bodyLength) {
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
