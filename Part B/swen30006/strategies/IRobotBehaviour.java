/* SWEN30006 Software Modelling and Design 
 * Semester 2, 2017
 * RobotBehaviour Interface - Contains all the architecture for a robot behaviour.
 * 
 * Authors: <Willem Stewart> <Lee Mintz> <Jason Pursey>
 * Student Numbers: <695852> <932739> <637551>
 */
package strategies;

import automail.IMailPool;
import robot.StorageTube;

public interface IRobotBehaviour {
	
	/**
	 * Fills the tube that the robot carries with new mail to deliver.
     * @param mailPool used to put back or get mail.
     * @param tube refers to the pack the robot uses to deliver mail.
     * @return Return true to indicate that the robot is ready to start delivering.
     */
    public boolean fillStorageTube(IMailPool mailPool, StorageTube tube);
	
	/** 
	 * Tells the robot to return to the mail room if the requirements are met.
	 * @param tube refers to the pack the robot uses to deliver mail.
	 * @return When this is true, the robot is returned to the mail room.
	 */
    public boolean returnToMailRoom(StorageTube tube);
    
    /**
     * Informs the robot that there has been a priority arrival.
     * @param priority is that of the mail item which just arrived.
     */
    public void priorityArrival(int priority);
    
    /**
	 * Creates a new tube for the robot that corresponds to the behaviour specs.
	 * @return Returns a storage tube of the correct size.
	 */
	public StorageTube createTube();

}
