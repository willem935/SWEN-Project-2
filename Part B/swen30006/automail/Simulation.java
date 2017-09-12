package automail;

import exceptions.ExcessiveDeliveryException;
import exceptions.MailAlreadyDeliveredException;
import java.io.FileNotFoundException;
import strategies.Automail;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

/**
 * This class simulates the behaviour of AutoMail
 */
public class Simulation {

    

    private static ArrayList<MailItem> MAIL_DELIVERED;
    private static double total_score = 0;
    
    private static Properties automailProperties = new Properties();

    public static void main(String[] args) throws IOException{
        FileReader inStream = null;

        try {
                inStream = new FileReader("automail.Properties");
                automailProperties.load(inStream);
        } catch(FileNotFoundException e){
            // load defaults
            System.out.println("Couln't find automail.Properties, using default values");
        } finally {
                if (inStream != null) {
                    inStream.close();
            }
        }

        for (String key:automailProperties.stringPropertyNames()){
            System.out.println(key + " is " + automailProperties.getProperty(key));
        }

        MAIL_DELIVERED = new ArrayList<MailItem>();
                
        /** Used to see whether a seed is initialized or not */
        HashMap<Boolean, Integer> seedMap = new HashMap<>();
        
        // Will ************ new method for this shit - maybe not a hash map as well?
        /** Read the first argument and save it as a seed if it exists */
        if(args.length != 0){
	        	int seed = Integer.parseInt(args[0]);
	        	seedMap.put(true, seed);
        } else{
	        	seedMap.put(false, 0);
        }
        Automail automail = new Automail(new ReportDelivery());
        MailGenerator generator = new MailGenerator( Integer.parseInt(automailProperties.getProperty("Mail_to_Create")), automail.mailPool, seedMap);
        
        /** Initiate all the mail */
        generator.generateAllMail();
        int priority;
        while(MAIL_DELIVERED.size() != generator.MAIL_TO_CREATE) {
        	//System.out.println("-- Step: "+Clock.Time());
            priority = generator.step();
            if (priority > 0) automail.robot.behaviour.priorityArrival(priority);
            
            try {
				automail.robot.step();
			} catch (ExcessiveDeliveryException e) {
				e.printStackTrace();
				System.out.println("Simulation unable to complete..");
				System.exit(0);
			}
            Clock.Tick();
        }
        printResults();
    }
    // Will ************ This is weird and should be put somewhere else.......
    // would it be to much to move this and below into a new class 
    static class ReportDelivery implements IMailDelivery {
    	
    	/** Confirm the delivery and calculate the total score */
    	public void deliver(MailItem deliveryItem){
    		if(!MAIL_DELIVERED.contains(deliveryItem)){
    			System.out.println("T: "+Clock.Time()+" | Delivered " + deliveryItem.toString());
    			MAIL_DELIVERED.add(deliveryItem);
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
    	double priority_weight = 0;
        // Take (delivery time - arrivalTime)**penalty * (1+sqrt(priority_weight))
    	if(deliveryItem instanceof PriorityMailItem){
    		priority_weight = ((PriorityMailItem) deliveryItem).getPriorityLevel();
    	}
        return Math.pow(Clock.Time() - deliveryItem.getArrivalTime(),penalty)*(1+Math.sqrt(priority_weight));
    }

    public static void printResults(){
        System.out.println("T: "+Clock.Time()+" | Simulation complete!");
        System.out.println("Final Delivery time: "+Clock.Time());
        System.out.printf("Final Score: %.2f%n", total_score);
    }
}
