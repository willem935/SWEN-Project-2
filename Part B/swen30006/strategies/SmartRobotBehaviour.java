/* SWEN30006 Software Modelling and Design 
 * Semester 2, 2017
 * SmartRobotBehaviour Class - Contains all the methods for the smart behaviour.
 * Responsible for filling the robot tube, deciding when to return to the mail room, 
 * and creating the StorageTube.
 * 
 * Authors: <Willem Stewart> <Lee Mintz> <Jason Pursey>
 * Student Numbers: <695852> <932739> <637551>
 */

package strategies;

import java.util.ArrayList;
import java.util.Comparator;
import automail.IMailPool;
import automail.MailItem;
import automail.MailPool;
import automail.PriorityMailItem;
import exceptions.TubeFullException;
import robot.SmallTube;
import robot.StorageTube;

public class SmartRobotBehaviour implements IRobotBehaviour{
	
	/** tracks the number of priority arrivals while out delivering */
	private int newPriorityArrival;
	
	/**
	 * Constructor
	 */
	public SmartRobotBehaviour(){
		newPriorityArrival = 0;
	}
	
	/**
	 * Fills the tube that the robot carries with new mail to deliver.
     * @param mailPool used to put back or get mail.
     * @param tube refers to the pack the robot uses to deliver mail.
     * @return Return true to indicate that the robot is ready to start delivering.
     */
	public boolean fillStorageTube(IMailPool mailPool, StorageTube tube) {
		
		// temporary tube used when collecting mail
		ArrayList<MailItem> tempTube = new ArrayList<MailItem>();

		// empty my tube
		while(!tube.tube.isEmpty()){
			mailPool.addToPool(tube.pop());
		}
		
		// grab priority mail if available 
		while( tempTube.size()  < tube.getCapacity()){
			if(mailPool.getPriorityPoolSize() > 0){
				tempTube.add(mailPool.getHighestPriorityMail());
			}
			else {
				// fill it up with non priority
				if(mailPool.getNonPriorityPoolSize() > 0){ 
					tempTube.add(mailPool.getNonPriorityMail());
				}
				else{
					break;
				}
				
			}
		}
		
		// sort tempTube based on floor
		tempTube.sort(new ArrivalComparer());
		
		// iterate through the tempTube and add them to tube
		while(tempTube.iterator().hasNext()){
			try {
				tube.addItem(tempTube.remove(0));
			} catch (TubeFullException e) {
				e.printStackTrace();
			}
		}
		
		// check if there is anything in the tube
		if(!tube.tube.isEmpty()){
			newPriorityArrival = 0;
			return true;
		}
		return false;
	}

	
	
	/** 
	 * Tells the robot to return to the mail room if the requirements are met.
	 * @param tube refers to the pack the robot uses to deliver mail.
	 * @return When this is true, the robot is returned to the mail room.
	 */
	public boolean returnToMailRoom(StorageTube tube) {
		
		// check if my tube contains  items
		if(!tube.isEmpty()){
			int priorityCount = 0;
			int nonPriorityCount = 0;
			
			// there has to be more priority than non-priority to keep going
			for(MailItem m : tube.tube){
				if(m instanceof PriorityMailItem){
					priorityCount++;
				}
				else{
					nonPriorityCount++;
				}
			}
			if(priorityCount >= nonPriorityCount){
				return false;
			}
			else{
				// Check if there is more than 1 priority arrival and the size of the tube is greater than or equal to half
				if(newPriorityArrival > 1 && tube.getSize() >= tube.getCapacity()/2){

					return true;
				}
				else{
					return false;
				}
				
			}
		}
		else{
			return true;
		}
	}

	/**
     * Informs the robot that there has been a priority arrival.
     * @param priority is that of the mail item which just arrived.
     */
	public void priorityArrival(int priority) {
		// Record that a new one has arrived
		newPriorityArrival++;	
	}
	
	/**
     * Creates a new tube for the robot that corresponds to the behaviour specs.
     * @return Returns a storage tube of the correct size.
     */
	public StorageTube createTube() {
		// TODO Auto-generated method stub
		return new SmallTube();
	}

	
	private class ArrivalComparer implements Comparator<MailItem>{

		/**
	     * Compares two mail items.
	     * @param m1 is the first mailItem to compare.
	     * @param m2 is the second mailItem to compare.
	     * @return -1 if m1 is higher priority, 0 if same and 1 if m2 has higher priority.
	     */
		public int compare(MailItem m1, MailItem m2) {
			return MailPool.compareArrival(m1, m2);
		}
	}

}
