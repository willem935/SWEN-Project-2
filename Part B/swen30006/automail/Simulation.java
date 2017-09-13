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
            loadDefaultProperties();
        } finally {
                if (inStream != null) {
                    inStream.close();
            }
        }

        MAIL_DELIVERED = new ArrayList<MailItem>();
        
        
        Automail automail = new Automail(new ReportDelivery());
        // Will ************ new method for this shit - maybe not a hash map as well?
        // Jason: done :)
        
        MailGenerator generator = makeGenerator(automail, args);
        
        
        /** Initiate all the mail */
        generator.generateAllMail();
        int priority;
        while(MAIL_DELIVERED.size() != generator.MAIL_TO_CREATE) {
        	//System.out.println("-- Step: "+Clock.Time());
            priority = generator.step();
            if (priority > 0) {
            	automail.robot.behaviour.priorityArrival(priority);
            	//LEE : added print statement here, removed from robot behaviors.
            	// seems that it makes more sense to be here instead of having
            	// each robot do it individually.
            	System.out.println("T: "+Clock.Time()+" | Priority arrived");
            }
            
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
    /**
     * 
     * @param automail 
     * @param args command line args. Used to check for seed
     * @return
     */
    private static MailGenerator makeGenerator(Automail automail, String[] args) {
        int mailToCreate = Integer.parseInt(automailProperties.getProperty("Mail_to_Create"));
        int mailVariance = Integer.parseInt(automailProperties.getProperty("Mail_Count_Percentage_Variation"));
        long seed;
        // prioritize properties seed over command line
        if(automailProperties.containsKey("Seed")){
            seed = Long.parseLong(automailProperties.getProperty("Seed"));
            return new MailGenerator(mailToCreate, mailVariance, automail.mailPool, seed);
        }else if(args.length != 0){
            seed = Long.parseLong(args[0]);
            return new MailGenerator(mailToCreate, mailVariance, automail.mailPool, seed);
        }else{
            return new MailGenerator(mailToCreate, mailVariance, automail.mailPool);
        }
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
    
    /**
     * use to set properties if no file found
     */
    private static void loadDefaultProperties() {
        automailProperties.setProperty("Number_of_Floors", "9");
        automailProperties.setProperty("Lowest_Floor", "1");
        automailProperties.setProperty("Location_of_MailRoom", "1");
        automailProperties.setProperty("Delivery_Penalty", "1.1");
        automailProperties.setProperty("Last_Delivery_Time", "100");
        automailProperties.setProperty("Mail_to_Create", "60");
        automailProperties.setProperty("Mail_Count_Percentage_Variation", "20");
        automailProperties.setProperty("Priority_Mail_is_One_in", "6");
        automailProperties.setProperty("Robot_Type", "Small_Comms_Simple");
    }
}
