/* SWEN30006 Software Modelling and Design 
 * Semester 2, 2017
 * Robot Class - Contains all the details regarding to the functionality of collecting
 * and delivering mail.
 * 
 * Authors: <Willem Stewart> <Lee Mintz> <Jason Pursey>
 * Student Numbers: <695852> <932739> <637551>
 */
package robot;

import automail.Building;
import automail.IMailPool;
import automail.MailItem;
import exceptions.ExcessiveDeliveryException;
import simulation.Clock;
import simulation.IMailDelivery;
import strategies.IRobotBehaviour;

public class Robot {

	/** tube which is used to deliver mail */
    private StorageTube tube;
    
    /** behaviour of the robot, determining how it acts when delivering or mail arrives etc */
    private IRobotBehaviour behaviour;
    
	/** delivery attribute for delivering mail through */
    private IMailDelivery delivery;

    /** Possible states the robot can be in */
    private enum RobotState { DELIVERING, WAITING, RETURNING }
    
    /** the state of the robot */
    private RobotState current_state;
    
    /** the floor the robot is on */
    private int current_floor;
    
    /** where the robot is heading */
    private int destination_floor;
    
    /** pool of mail that the robot is delivering from */
    private IMailPool mailPool;
    
    /** building in which the robot delivers mail */
    private Building building;
    
    /** mailItem that is currently being delivered */
    private MailItem deliveryItem;
    
    /** number of items that have been delivered */
    private int deliveryCounter;
    

    /**
     * Initiates the robot's location at the start to be at the mailroom
     * also set it to be waiting for mail.
     * @param behaviour governs selection of mail items for delivery and behaviour on priority arrivals
     * @param delivery governs the final delivery
     * @param mailPool is the source of mail items
     * @param building building the robot is working in
     */
    public Robot(IRobotBehaviour behaviour, IMailDelivery delivery, IMailPool mailPool, Building building){
    		
    		current_state = RobotState.WAITING;
        this.building = building;
        current_floor = building.getMailRoomLocation();
        this.behaviour = behaviour;
        tube = behaviour.createTube();
        this.delivery = delivery;
        this.mailPool = mailPool;
        this.deliveryCounter = 0;
    }
    
 	/**
     * This is called on every time step
     * @throws ExcessiveDeliveryException if robot delivers more than the capacity of the tube without refilling
     */
    public void step() throws ExcessiveDeliveryException{
    	
	    	boolean go = false;
	    	
	    	switch(current_state) {
	    		// triggered when the robot is returning to the mailroom after a delivery 
	    		case RETURNING:
	    			// if its current position is at the mailroom, then the robot should change state 
	                if(current_floor == building.getMailRoomLocation()){
	                	changeState(RobotState.WAITING);
	                } else {
	                		// if the robot is not at the mailroom floor yet, then move towards it! 
	                    moveTowards(building.getMailRoomLocation());
	                		break;
	                }
	    		
	    		case WAITING:
	    			// tell the sorter the robot is ready
	            go = behaviour.fillStorageTube(mailPool, tube);
	            
	            // if the StorageTube is ready and the Robot is waiting in the mailroom then start the delivery 
	            if(go){
	            		deliveryCounter = 0; // reset delivery counter
	            		setRoute();
	            		changeState(RobotState.DELIVERING);
	            }    
	            break;
	            
	    		case DELIVERING:
	    			// check whether or not the call to return is triggered manually
	    			// If already here drop off item
	    			if(current_floor == destination_floor){ 
	    				
	    				// Deliver items and remove it from tube 
                    delivery.deliver(deliveryItem);
                    tube.pop();
                    deliveryCounter++;

                    if(deliveryCounter > tube.getCapacity()){
                    		throw new ExcessiveDeliveryException();
                    }
                    // check if want to return or if there are more items in the tube 
                    if(tube.isEmpty() || behaviour.returnToMailRoom(tube)){ 
                    		changeState(RobotState.RETURNING);
                    }
                    else{
                    		// if there are more items, set the robot's route to the location to deliver the item 
                    		setRoute();
                    		changeState(RobotState.DELIVERING);
                    }
	    			} else {
	    				// Robot requested to return
		    			if(behaviour.returnToMailRoom(tube)){  
		    				changeState(RobotState.RETURNING);
		    			}
		    			else{
		        			// the robot is not at the destination yet, move towards it! 
		    				moveTowards(destination_floor);
		    			}
	    			}
	    			
	            break;
	    	}
    }

    /**
     * Sets the route for the robot
     */
    private void setRoute(){
        // Pop the item from the StorageUnit 
        deliveryItem = tube.peek();
        /// Set the destination floor 
        destination_floor = deliveryItem.getDestFloor();
    }

    /** 
     * Generic function that moves the robot towards the destination 
     * @param destination floor which the robot is heading towards
     */
    private void moveTowards(int destination){
        if(current_floor < destination){
            current_floor++;
        }
        else{
            current_floor--;
        }
    }
    
    /**
     * Prints out the change in state
     * @param nextState
     */
    private void changeState(RobotState nextState){
	    	if (current_state != nextState) {
	    		System.out.println("T: "+Clock.Time()+" | Robot changed from "+current_state+" to "+nextState);
	    	}
	    	current_state = nextState;
	    	if(nextState == RobotState.DELIVERING){
	    		System.out.println("T: "+Clock.Time()+" | Deliver   " + deliveryItem.toString());
	    	}
    }

	
    /**
     * Gets the robots behaviour
     * @return behaviour the current behaviour of the robot
     */
    public IRobotBehaviour getBehaviour() {
		return behaviour;
	}
}
