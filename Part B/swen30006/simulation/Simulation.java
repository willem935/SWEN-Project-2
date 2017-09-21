/* SWEN30006 Software Modelling and Design 
 * Semester 2, 2017
 * Simulation Class - Provides Controller functionality for mail delivery of 
 * of mail.
 * 
 * Authors: <Willem Stewart> <Lee Mintz> <Jason Pursey>
 * Student Numbers: <695852> <932739> <637551>
 */

package simulation;

import exceptions.MailAlreadyDeliveredException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import automail.Automail;
import automail.MailItem;

/**
 * This class simulates the behaviour of AutoMail
 */
public class Simulation {

	/** File used to set variables of the simulation and set required specs */
    private static final String FILENAME = "automail.Properties";
    
    /** stores delivered mail */
    private static ArrayList<MailItem> mailDelivered;
    
	/** scores the simulation (low score is better) */
    private static double total_score = 0;
    
    /** creates properties that are required for the simulation */
    private static Properties automailProperties = new Properties();

    
    /**
     * main for Simulation. Used to run all associated aspects of mail delivery simulation
     */
    public static void main(String[] args) throws IOException{
        
    		loadProperties();
        mailDelivered = new ArrayList<MailItem>();

        // determines and sets termination clock time
        int lastTime = Integer.parseInt(
                automailProperties.getProperty("Last_Delivery_Time"));
        Clock.setLastDeliveryTime(lastTime);
        
        // creates automail
        Automail automail = makeAutomail();
        
        // initiate all the mail
        MailGenerator generator = makeGenerator(automail, args);      
        generator.generateAllMail();
        
        // run the simulation until all mail is delivered
        int priority;
        while(mailDelivered.size() != generator.getMailToCreate()) {
            priority = generator.step();
            automail.step(priority);
        }

        // print final results 
        printResults();
    }


    /**
     * uses file to load properties for simulation
     */
    private static void loadProperties() throws IOException {
        FileReader inStream = new FileReader(FILENAME);
        automailProperties.load(inStream);
        inStream.close();
    }
    
    
    /**
     * generates a new Automail class using the properties for the input file
     * @return new Automail class
     */
    private static Automail makeAutomail(){
        
        // get the building properties to give to Automail
        int buildingFloors = Integer.parseInt(automailProperties.getProperty("Number_of_Floors"));
        int mRFloor = Integer.parseInt(automailProperties.getProperty("Location_of_MailRoom"));
        int lowFloor = Integer.parseInt(automailProperties.getProperty("Bottom_Floor"));
        String behaviourName = automailProperties.getProperty("Robot_Type");
      
        
        Automail automail = new Automail(new ReportDelivery(), buildingFloors, lowFloor, mRFloor, behaviourName);
        return automail;
    }

    /**
     * generates the mail required for the simulation
     * @param automail 
     * @param args command line args. Used to check for seed
     * @return MailGenerator with correct specs
     */
    private static MailGenerator makeGenerator(Automail automail, String[] args) {
        
    		int mailToCreate = Integer.parseInt(automailProperties.getProperty("Mail_to_Create"));
        int mailVariance = Integer.parseInt(automailProperties.getProperty("Mail_Count_Percentage_Variation"));
        int priorityChance = Integer.parseInt(automailProperties.getProperty("Priority_Mail_is_One_in"));
        int seed;
        
        // prioritize properties seed over command line
        if(automailProperties.containsKey("Seed")){
            seed = Integer.parseInt(automailProperties.getProperty("Seed"));
            return new MailGenerator(mailToCreate, mailVariance, priorityChance, 
                    automail.getMailPool(), automail.getBuilding(), seed);
        // uses command line seed
        }else if(args.length != 0){
            seed = Integer.parseInt(args[0]);
            return new MailGenerator(mailToCreate, mailVariance, priorityChance,
                    automail.getMailPool(), automail.getBuilding(), seed);
        // uses random seed
        }else{
            return new MailGenerator(mailToCreate, mailVariance, priorityChance,
                    automail.getMailPool(), automail.getBuilding());
        }
    }

    
    
    // DONT REALLY KNOW WHAT TO COMMENT ON THIS ONE **************
    static class ReportDelivery implements IMailDelivery {
    	
	    	/** Confirm the delivery and calculate the total score */
	    	public void deliver(MailItem deliveryItem){

	    		// checks that deliveryItem has not already been delivered
	    		if(!mailDelivered.contains(deliveryItem)){
		    		// prints relevant information and adds deliveryItem to mailDelivered.
	    			System.out.println("T: "+Clock.Time()+" | Delivered " + deliveryItem.toString());
	    			mailDelivered.add(deliveryItem);
	    			// Calculate delivery score
	    			total_score += calculateDeliveryScore(deliveryItem, 
	                                Double.parseDouble(automailProperties.getProperty("Delivery_Penalty")));
	    		}
	    		// if deliveryItem already delivered throw exception
	    		else {
	    			try {
	    				throw new MailAlreadyDeliveredException();
	    			} catch (MailAlreadyDeliveredException e) {
	    				e.printStackTrace();
	    			}
	    		}
	    	}

    }
    
    /**
     * calculates score to add to total after delivering a mailItem
     * @param deliveryItem the items to be delivered 
     * @param penalty power multiple 
     * @return score to add to total
     */
    private static double calculateDeliveryScore(MailItem deliveryItem, double penalty) {
    	// Penalty for longer delivery times
    	double priority_weight = deliveryItem.getPriorityLevel();
        // Take (delivery time - arrivalTime)**penalty * (1+sqrt(priority_weight))
    	
        return Math.pow(Clock.Time() - deliveryItem.getArrivalTime(),penalty)*(1+Math.sqrt(priority_weight));
    }

    public static void printResults(){
        System.out.println("T: "+Clock.Time()+" | Simulation complete!");
        System.out.println("Final Delivery time: "+Clock.Time());
        System.out.printf("Final Score: %.2f%n", total_score);
    }
}
