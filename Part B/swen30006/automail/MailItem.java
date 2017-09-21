/* SWEN30006 Software Modelling and Design 
 * Semester 2, 2017
 * MailItem Class - Contains the architecture for the construction of a non-priority MailItem instance
 * as well as a getPriorityLevel method for use in comparing nonPriorityMailItems to priorityMailItems
 * Authors: <Willem Stewart> <Lee Mintz> <Jason Pursey>
 * Student Numbers: <695852> <932739> <637551>
 */

package automail;

import java.util.UUID;

/**
 * Represents a mail item
 */
public class MailItem {
	
    /** Represents the destination floor to which the mail is intended to go */
    protected final int DESTINATION_FLOOR;
    /** The mail identifier */
    protected final String ID;
    /** The time the mail item arrived */
    protected final int ARRIVAL_TIME;

    /**
     * Constructor for a MailItem
     * @param dest_floor the destination floor intended for this mail item
     * @param arrival_time the time that the mail arrived
     */
    public MailItem(int dest_floor, int arrival_time){
        this.DESTINATION_FLOOR = dest_floor;
        this.ID = UUID.randomUUID().toString();
        this.ARRIVAL_TIME = arrival_time;
    }

    /**
     * @return String containing MailItem's ID, Destination floor, and Arrival time
     */
    @Override
    public String toString(){
        return "Mail Item: " +
                "| ID: " + ID +
                "| Destination: "+ DESTINATION_FLOOR +
                "| Arrival: "+ ARRIVAL_TIME
                ;
    }

    /**
     *
     * @return the destination floor of the mail item
     */
    public int getDestFloor() {
        return DESTINATION_FLOOR;
    }
    
    /**
     *
     * @return the ID of the mail item
     */
    public String getId() {
        return ID;
    }

    /**
     * Gives regular mail items a priority of 0 so we don't have to constantly
     * check for instances of prioritymailitems whenever we want to calculate
     * priority levels.
     * @return 0 as default priority level. Overridden by prioritymailitem objects when necessary
     */
    public int getPriorityLevel(){
    	return 0;
    }
    
    /**
     *
     * @return the arrival time of the mail item
     */
    public int getArrivalTime(){
        return ARRIVAL_TIME;
    }

}
