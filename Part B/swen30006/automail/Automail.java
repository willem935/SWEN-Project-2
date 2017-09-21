/* SWEN30006 Software Modelling and Design 
 * Semester 2, 2017
 * Automail Class - Contains the functionality running a mail room automatically.
 * It contains the robot, building and IMailPool classes.
 * 
 * Authors: <Willem Stewart> <Lee Mintz> <Jason Pursey>
 * Student Numbers: <695852> <932739> <637551>
 */

package automail;

import exceptions.ExcessiveDeliveryException;
import robot.Robot;
import simulation.Clock;
import simulation.IMailDelivery;
import strategies.BigSimpleRobotBehaviour;
import strategies.IRobotBehaviour;
import strategies.SimpleRobotBehaviour;
import strategies.SmartRobotBehaviour;

import java.util.HashMap;

public class Automail {
	      
    private Robot robot;
    private IMailPool mailPool;
    private Building building;
    
    /**
     * constructor
     * @param delivery delivery class for robot
     * @param floors number of floors in building
     * @param lowestFloor lowest floor in building
     * @param mailRoomLocation floor mailroom is on
     * @param behaviour String representing type of robot behaviour to use. Must match one specified in makeBehaviour
     */
    public Automail(IMailDelivery delivery, int floors, int lowestFloor, int mailRoomLocation, String behaviour_s) {
	    	/** Initialize the MailPool */
	    	mailPool = new MailPool();
	    	
	    /** Initialize the RobotAction */
	    IRobotBehaviour robotBehaviour = chooseBehaviour(behaviour_s);
	    	    	
	    	/** Initialize building */
	    building = new Building(floors, lowestFloor, mailRoomLocation);
    		
	    /** Initialize robot */
	    	robot = new Robot(robotBehaviour, delivery, mailPool, building);
	    	
    }

    /**
     * used to create an IRobotBehaviour based on behaviour_s
     * @param behaviour_s description of robot type from automail.Properties
     * @return a concrete implementation of IRobotBehaviour
     */
    private IRobotBehaviour chooseBehaviour(String behaviour_s) {
        IRobotBehaviour robotBehaviour = null;
        
        // Using input string, selects appropriate behaviour.
        // When adding new robot behaviours be sure to include here.
        switch(behaviour_s){
            case("Small_Comms_Simple"):
                robotBehaviour = new SimpleRobotBehaviour();
                break;
            case("Small_Comms_Smart"):
                robotBehaviour = new SmartRobotBehaviour();
                break;
            case("Big_Simple"):
                robotBehaviour = new BigSimpleRobotBehaviour();
                break;
            default:
                System.err.println("Specified robot behaviour " + behaviour_s + " not found");
                System.exit(1);
        }
        
        return robotBehaviour;
    }
    
    /**
     * used to drive functionality of automail class and associated classes
     * @param priority of newly created mail (0 for nonPriority)
     */
    public void step(int priority){
        
    		// Priority mail arrival check
        if (priority > 0) {
            robot.getBehaviour().priorityArrival(priority);
            System.out.println("T: "+Clock.Time()+" | Priority arrived");
        }
        
        try {
                robot.step();
        } catch (ExcessiveDeliveryException e) {
                e.printStackTrace();
                System.out.println("Simulation unable to complete..");
                System.exit(0);
        }
        Clock.Tick();
    }
    
    /**
     * @return mailPool being used
     */
    public IMailPool getMailPool(){
        return mailPool;
    }
    
    /**
     * @return building created by automail
     */
    public Building getBuilding(){
        return building;
    }    
    
}
