package simulation;

import exceptions.MailAlreadyDeliveredException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import automail.Automail;
import automail.IMailDelivery;
import automail.MailItem;
import strategies.BigSimpleRobotBehaviour;
import strategies.SimpleRobotBehaviour;
import strategies.SmartRobotBehaviour;

/**
 * This class simulates the behaviour of AutoMail
 */
public class Simulation {

    
    private static final String FILENAME = "automail.Properties";
    private static ArrayList<MailItem> mailDelivered;
    private static double total_score = 0;
    
    private static Properties automailProperties = new Properties();

    public static void main(String[] args) throws IOException{
        loadProperties();

        mailDelivered = new ArrayList<MailItem>();
        
        // 19/9 Jason: set the last time based on Automail.properties
        int lastTime = Integer.parseInt(
                automailProperties.getProperty("Last_Delivery_Time"));
        Clock.setLastDeliveryTime(lastTime);
        
        Automail automail = makeAutomail();
        MailGenerator generator = makeGenerator(automail, args);
        
        
        /** Initiate all the mail */
        generator.generateAllMail();
        
        // run the simulation until all mail is delivered
        int priority;
        while(mailDelivered.size() != generator.getMailToCreate()) {
            priority = generator.step();
            automail.step(priority);
        
        }
        printResults();
    }

    private static Automail makeAutomail(){
        
        // get the building properties to give to Automail
        int buildingFloors = Integer.parseInt(automailProperties.getProperty("Number_of_Floors"));
        int mRFloor = Integer.parseInt(automailProperties.getProperty("Location_of_MailRoom"));
        int lowFloor = Integer.parseInt(automailProperties.getProperty("Bottom_Floor"));
        String behaviourName = automailProperties.getProperty("Robot_Type");
        
        // all robot behaviours that Automail knows about (for option a)
        Automail.addBehaviour("Small_Comms_Simple", new SimpleRobotBehaviour());
        Automail.addBehaviour("Small_Comms_Smart", new SmartRobotBehaviour());
        Automail.addBehaviour("Big_Smart", new BigSimpleRobotBehaviour());
        
        Automail automail = new Automail(new ReportDelivery(), buildingFloors, lowFloor, mRFloor, behaviourName);
        return automail;
    }

    
    
    
    private static void loadProperties() throws IOException {
        FileReader inStream = new FileReader(FILENAME);
        automailProperties.load(inStream);
        inStream.close();
    }
    /**
     * 
     * @param automail 
     * @param args command line args. Used to check for seed
     * @return
     */
    
    private static MailGenerator makeGenerator(Automail automail, String[] args) {
        int mailToCreate = Integer.parseInt(automailProperties.getProperty("Mail_to_Create"));
        int mailVariance = Integer.parseInt(automailProperties.getProperty("Mail_Count_Percentage_Variation"));
        int seed;
        // prioritize properties seed over command line
        if(automailProperties.containsKey("Seed")){
            seed = Integer.parseInt(automailProperties.getProperty("Seed"));
            return new MailGenerator(mailToCreate, mailVariance, 
                    automail.getMailPool(), automail.getBuilding(), seed);
        }else if(args.length != 0){
            seed = Integer.parseInt(args[0]);
            return new MailGenerator(mailToCreate, mailVariance, 
                    automail.getMailPool(), automail.getBuilding(), seed);
        }else{
            return new MailGenerator(mailToCreate, mailVariance, 
                    automail.getMailPool(), automail.getBuilding());
        }
    }

    // Will ************ This is weird and should be put somewhere else.......
    // would it be to much to move this and below into a new class 
    static class ReportDelivery implements IMailDelivery {
    	
	    	/** Confirm the delivery and calculate the total score */
	    	public void deliver(MailItem deliveryItem){
	    		if(!mailDelivered.contains(deliveryItem)){
	    			System.out.println("T: "+Clock.Time()+" | Delivered " + deliveryItem.toString());
	    			mailDelivered.add(deliveryItem);
	    			// Calculate delivery score
	    			total_score += calculateDeliveryScore(deliveryItem, 
	                                Double.parseDouble(automailProperties.getProperty("Delivery_Penalty")));
	    		}
	    		else{
	    			try {
	    				throw new MailAlreadyDeliveredException();
	    			} catch (MailAlreadyDeliveredException e) {
	    				e.printStackTrace();
	    			}
	    		}
	    	}

    }
    
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
