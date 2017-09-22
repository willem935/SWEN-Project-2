/* SWEN30006 Software Modelling and Design 
 * Semester 2, 2017
 * MailGenerator Class - Generates the required mail for the simulation in accordance with
 * the specified parameters.
 *  
 * Authors: <Willem Stewart> <Lee Mintz> <Jason Pursey>
 * Student Numbers: <695852> <932739> <637551>
 */


package simulation;

import java.util.*;
import automail.Building;
import automail.IMailPool;
import automail.MailItem;
import automail.PriorityMailItem;

/**
 * This class generates the mail
 */
public class MailGenerator {
	
	/** the number of mail that is to be created */
    private int mailToCreate;
    
    /** chance of a priority item arriving */
    private int priorityChance;
    
    /** number of mail items created */
    private int mailCreated;

    /** used for the randomness of mail items */
    private Random random;
    
    /** used to determine if all mail has been generated */
    private boolean complete;
    
    /** the mail pool to which the newly created mail is added */
    private IMailPool mailPool;
    
    /** the building where the mail is being delivered (only one) */
    private Building building;

    /** list of all the mail */
    private HashMap<Integer,ArrayList<MailItem>> allMail;

    /**
     * Constructor for mail generation
     * @param mailToCreate roughly how many mail items to create
     * @param variance variance for mail creation
     * @param priorityChance likelihood of priority mail
     * @param mailPool where mail items go on arrival
     * @param building building to determine delivery floors etc
     */
    public MailGenerator(int mailToCreate, int variance, int priorityChance, IMailPool mailPool, Building building){

        this.random = new Random();

        // Vary arriving mail by +/-variance%
        float variance_fraction = ((float) variance)/100;
        int lower_bound = Math.round(mailToCreate*(1-variance_fraction));
        int upper_bound = Math.round(mailToCreate*variance_fraction);
        this.mailToCreate = lower_bound+ random.nextInt(upper_bound);
        
        this.priorityChance = priorityChance;
        mailCreated = 0;
        complete = false;
        allMail = new HashMap<Integer,ArrayList<MailItem>>();
        this.mailPool = mailPool;
        this.building = building;
    }
    
    /**
     * Constructor for mail generation
     * @param mailToCreate roughly how many mail items to create
     * @param variance variance for mail creation
     * @param priorityChance likelihood of priority mail
     * @param mailPool where mail items go on arrival
     * @param building building to determine delivery floors etc
     * @param seed seed for randomness, used for testing
     */
    public MailGenerator(int mailToCreate, int variance, int priorityChance, IMailPool mailPool, Building building, int seed){
        
    		this(mailToCreate,variance, priorityChance, mailPool, building);
    		// overrides seed with input value
        random.setSeed(seed);
                
        // Vary arriving mail by +/-variance%
        float variance_fraction = ((float) variance)/100;
        int lower_bound = Math.round(mailToCreate*(1-variance_fraction));
        int upper_bound = Math.round(mailToCreate*variance_fraction);
        this.mailToCreate = lower_bound+ random.nextInt(upper_bound);
    }
    
    /**
     * This class initializes all mail and sets their corresponding values,
     */
    public void generateAllMail(){
        while(!complete){
            MailItem newMail =  generateMail();
            int timeToDeliver = newMail.getArrivalTime();
            // check if key exists for this time
            if(allMail.containsKey(timeToDeliver)){
                // add to existing array 
                allMail.get(timeToDeliver).add(newMail);
            }
            else{
            	
                // if the key doesn't exist then set a new key along with the array of MailItems to add during
                // that time step.
                ArrayList<MailItem> newMailList = new ArrayList<MailItem>();
                newMailList.add(newMail);
                allMail.put(timeToDeliver,newMailList);
            }
            
            // mark the mail as created 
            mailCreated++;

            // once we have satisfied the amount of mail to create, we're done
            if(mailCreated == mailToCreate){
                complete = true;
            }
        }

    }

    /**
     * Generates a new mail item
     * @return a new mail item that needs to be delivered
     */
    private MailItem generateMail(){
        int dest_floor = generateDestinationFloor();
        int priority_level = generatePriorityLevel();
        int arrival_time = generateArrivalTime();
        // Check if arrival time has a priority mail
        if(	(random.nextInt(priorityChance) > 0) ||  // Skew towards non priority mail
        	(allMail.containsKey(arrival_time) &&
        	allMail.get(arrival_time).stream().anyMatch(e -> PriorityMailItem.class.isInstance(e)))){
        	return new MailItem(dest_floor,arrival_time);      	
        }
        else{
        	return new PriorityMailItem(dest_floor,priority_level,arrival_time);
        }   
    }
    
    /**
     * While there are steps left, create a new mail item to deliver
     * @return Priority, used to notify Robot
     */
    public int step(){
    		int priority = 0;
    	
    		// Check if there are any mail to create
        if(this.allMail.containsKey(Clock.Time())){
            for(MailItem mailItem : allMail.get(Clock.Time())){
                mailPool.addToPool(mailItem);
                
                if (mailItem instanceof PriorityMailItem) priority = mailItem.getPriorityLevel();
                		System.out.println("T: "+Clock.Time()+" | Arrive    " + mailItem.toString());
            }
        }
        return priority;
    }

    /**
     * @return a destination floor between the ranges of GROUND_FLOOR to FLOOR
     */
    private int generateDestinationFloor(){
        return building.getLowestFloor() + random.nextInt(building.getFloors());
    }

    /**
     * @return a random priority level selected from 1 - 100
     */
    private int generatePriorityLevel(){
        return 1 + random.nextInt(100);
    }
    
    /**
     * @return a random arrival time before the last delivery time
     */
    private int generateArrivalTime(){
        // 19/9 Jason: changed LAST_DELIVERY_TIME -> getLastDeliveryTime()
        return 1 + random.nextInt(Clock.getLastDeliveryTime());
    }
    
    /**
     * @return the number of mail items to be created
     */
    public int getMailToCreate() {
        return mailToCreate;
    }
    
}
