package automail;

import java.util.*;

import strategies.IMailPool;

/**
 * This class generates the mail
 */
public class MailGenerator {

    public final int MAIL_TO_CREATE;

    private int mailCreated;

    private final Random random;
    /** This seed is used to make the behaviour deterministic */
    
    private boolean complete;
    private IMailPool mailPool;
    private Building building;

    private HashMap<Integer,ArrayList<MailItem>> allMail;

    /**
     * Constructor for mail generation
     * @param mailToCreate roughly how many mail items to create
     * @param mailPool where mail items go on arrival
     * @param seed random seed for generating mail
     */
    
    public MailGenerator(int mailToCreate, int variance, IMailPool mailPool, Building building){

        this.random = new Random();

        // Vary arriving mail by +/-variance%
        float variance_fraction = ((float) 20)/100;
        int lower_bound = Math.round(mailToCreate*(1-variance_fraction));
        int upper_bound = Math.round(mailToCreate*variance_fraction);
        MAIL_TO_CREATE = lower_bound+ random.nextInt(upper_bound);
        // System.out.println("Num Mail Items: "+MAIL_TO_CREATE);
        mailCreated = 0;
        complete = false;
        allMail = new HashMap<Integer,ArrayList<MailItem>>();
        this.mailPool = mailPool;
        this.building = building;
    }

    public MailGenerator(int mailToCreate, int variance, IMailPool mailPool, Building building, long seed){
            this(mailToCreate,variance, mailPool, building);
            random.setSeed(seed);
    }
    

    /**
     * @return a new mail item that needs to be delivered
     */
    private MailItem generateMail(){
        int dest_floor = generateDestinationFloor();
        int priority_level = generatePriorityLevel();
        int arrival_time = generateArrivalTime();
        // Check if arrival time has a priority mail
        if(	(random.nextInt(6) > 0) ||  // Skew towards non priority mail
        	(allMail.containsKey(arrival_time) &&
        	allMail.get(arrival_time).stream().anyMatch(e -> PriorityMailItem.class.isInstance(e)))){
        	return new MailItem(dest_floor,arrival_time);      	
        }
        else{
        	return new PriorityMailItem(dest_floor,priority_level,arrival_time);
        }   
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
        return 1 + random.nextInt(Clock.LAST_DELIVERY_TIME);
    }


    /**
     * This class initializes all mail and sets their corresponding values,
     */
    public void generateAllMail(){
        while(!complete){
            MailItem newMail =  generateMail();
            int timeToDeliver = newMail.getArrivalTime();
            /** Check if key exists for this time **/
            if(allMail.containsKey(timeToDeliver)){
                /** Add to existing array */
                allMail.get(timeToDeliver).add(newMail);
            }
            else{
                /** If the key doesn't exist then set a new key along with the array of MailItems to add during
                 * that time step.
                 */
                ArrayList<MailItem> newMailList = new ArrayList<MailItem>();
                newMailList.add(newMail);
                allMail.put(timeToDeliver,newMailList);
            }
            /** Mark the mail as created */
            mailCreated++;

            /** Once we have satisfied the amount of mail to create, we're done!*/
            if(mailCreated == MAIL_TO_CREATE){
                complete = true;
            }
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
                //removed casting on right hand side of = due to change in mailItem getPriorityLevel()
                if (mailItem instanceof PriorityMailItem) priority = mailItem.getPriorityLevel();
                System.out.println("T: "+Clock.Time()+" | Arrive    " + mailItem.toString());
            }
        }
        return priority;
    }
    
}
