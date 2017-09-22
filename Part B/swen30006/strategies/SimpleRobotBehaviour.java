/* SWEN30006 Software Modelling and Design 
 * Semester 2, 2017
 * SimpleRobotBehaviour Class - Contains all the methods for the simple behaviour.
 * Responsible for filling the robot tube, deciding when to return to the mail room, 
 * and creating the StorageTube.
 * 
 * Authors: <Willem Stewart> <Lee Mintz> <Jason Pursey>
 * Student Numbers: <695852> <932739> <637551>
 */
package strategies;

import automail.IMailPool;
import automail.PriorityMailItem;
import exceptions.TubeFullException;
import robot.SmallTube;
import robot.StorageTube;

public class SimpleRobotBehaviour implements IRobotBehaviour {

	/** tracks the number of priority arrivals while out delivering */
	private boolean newPriority;

	/**
	 * Constructor
	 */
	public SimpleRobotBehaviour() {
		newPriority = false;
	}

	/**
	 * Fills the tube that the robot carries with new mail to deliver.
	 * 
	 * @param mailPool used to put back or get mail.
	 * @param tube refers to the pack the robot uses to deliver mail.
	 * @return Return true to indicate that the robot is ready to start
	 * 			delivering.
	 */
	public boolean fillStorageTube(IMailPool mailPool, StorageTube tube) {
		// Priority items are important;
		// if there are some, grab one and go, otherwise take as many items as
		// we can and go
		try {
			// Start afresh
			newPriority = false;
			while (!tube.isEmpty()) {
				mailPool.addToPool(tube.pop());
			}
			// Check for a top priority item
			if (mailPool.getPriorityPoolSize() > 0) {
				// Add priority mail item
				tube.addItem(mailPool.getHighestPriorityMail());
				// Go deliver that item
				return true;
			} else {
				// Get as many nonpriority items as available or as fit
				while (tube.getSize() < tube.getCapacity()
						&& mailPool.getNonPriorityPoolSize() > 0) {
					tube.addItem(mailPool.getNonPriorityMail());
				}
				return (tube.getSize() > 0);
			}
		} catch (TubeFullException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Tells the robot to return to the mail room if the requirements are met.
	 * 
	 * @param tube
	 *            refers to the pack the robot uses to deliver mail.
	 * @return When this is true, the robot is returned to the mail room.
	 */
	public boolean returnToMailRoom(StorageTube tube) {
		// only return if we don't have a priority item and a new one came in
		if (tube.getSize() > 0) {
			Boolean priority = (tube.peek() instanceof PriorityMailItem);
			return !priority && newPriority;
		} else {
			return false;
		}
	}

	/**
	 * Informs the robot that there has been a priority arrival.
	 * 
	 * @param priority
	 *            is that of the mail item which just arrived.
	 */
	public void priorityArrival(int priority) {
		// Record that a new one has arrived
		// why is priority mail arrived print statement here? Moved to
		// simulation.java
		newPriority = true;

	}

	/**
	 * Creates a new tube for the robot that corresponds to the behaviour specs.
	 * 
	 * @return Returns a storage tube of the correct size.
	 */
	public StorageTube createTube() {
		return new SmallTube();
	}

}
