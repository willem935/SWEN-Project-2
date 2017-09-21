/* SWEN30006 Software Modelling and Design 
 * Semester 2, 2017
 * BigSimpleRobotBehaviour Class - Contains all the methods for the big simple behaviour.
 * Responsible for filling the robot tube and creating the StorageTube.
 * 
 * Authors: <Willem Stewart> <Lee Mintz> <Jason Pursey>
 * Student Numbers: <695852> <932739> <637551>
 */

package strategies;

import java.util.Comparator;
import java.util.ArrayList;

import automail.IMailPool;
import automail.MailItem;
import automail.MailPool;
import exceptions.TubeFullException;
import robot.BigTube;
import robot.StorageTube;

public class BigSimpleRobotBehaviour implements IRobotBehaviour{
	
	/**
	 * Constructor
	 */
	public BigSimpleRobotBehaviour() {}

	/**
	 * Fills the tube that the robot carries with new mail to deliver.
     * @param mailPool used to put back or get mail.
     * @param tube refers to the pack the robot uses to deliver mail.
     * @return Return true to indicate that the robot is ready to start delivering.
     */
	public boolean fillStorageTube(IMailPool mailPool, StorageTube tube) {
		
		ArrayList<MailItem> tempTube = new ArrayList<MailItem>();

		// Empty my tube
		while(!tube.tube.isEmpty()){
			mailPool.addToPool(tube.pop());
		}
		
		// Grab priority mail
		while(tempTube.size() < tube.getCapacity()){
                    // 20/9 Jason: removed 2 calls to containMail()
			if(mailPool.getPriorityPoolSize() > 0){
				tempTube.add(mailPool.getHighestPriorityMail());
			}
			else{
				// Fill it up with non priority
				if(mailPool.getNonPriorityPoolSize() > 0){
					tempTube.add(mailPool.getNonPriorityMail());
				}
				else{
					break;
				}
				
			}
		}
		
		// Sort tempTube based on floor
		tempTube.sort(new ArrivalComparer());
		
		// Iterate through the tempTube
		while(tempTube.iterator().hasNext()){
			try {
				tube.addItem(tempTube.remove(0));
			} catch (TubeFullException e) {
				e.printStackTrace();
			}
		}
		
		// Check if there is anything in the tube
		if(!tube.tube.isEmpty()){
			//newPriorityArrival = 0;
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
        // big simple doesn't know about new arrivals, so never returns early
        return false;
    }

	/**
     * Informs the robot that there has been a priority arrival.
     * @param priority is that of the mail item which just arrived.
     */
	public void priorityArrival(int priority) {
		// big simple doesn't respond to incoming priority item's arriving
		// so this method shouldn't really do anything.
    }

	/**
     * Creates a new tube for the robot that corresponds to the behaviour specs.
     * @return Returns a storage tube of the correct size.
     */
	public StorageTube createTube() {
		// TODO Auto-generated method stub
		return new BigTube();
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
