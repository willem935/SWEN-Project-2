package automail;

import exceptions.ExcessiveDeliveryException;
import robot.Robot;
import simulation.Clock;
import strategies.BigSimpleRobotBehaviour;
import strategies.IRobotBehaviour;
import strategies.SimpleRobotBehaviour;
import strategies.SmartRobotBehaviour;

import java.util.HashMap;

public class Automail {
	      
    private Robot robot;
    private IMailPool mailPool;
    private Building building;
    private static HashMap<String, IRobotBehaviour> behaviours = new HashMap<>();
    
    /**
     * 
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
	    	    	
	    	/** Initialize robot */
	    building = new Building(floors, lowestFloor, mailRoomLocation);
	    	robot = new Robot(robotBehaviour, delivery, mailPool, building);
	    	
    }

    /**
     * used to create an IRobotBehaviour based on behaviour_s
     * @param behaviour_s description of robot type from automail.Properties
     * @return a concrete implementation of IRobotBehaviour
     */
    private IRobotBehaviour chooseBehaviour(String behaviour_s) {
        IRobotBehaviour robotBehaviour = null;
        // jason: I see two ways of doing this
        // option a
        // more coupling: has an instance of each robot type ready to go
//        if(behaviours.containsKey(behaviour_s)){
//            robotBehaviour = behaviours.get(behaviour_s);
//        }else{
//            System.err.println("Specified robot behaviour " + behaviour_s + " not found");
//            System.exit(1);
//        }

        // option b
        // harder to add more types in future
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
    
    public void step(int priority){
        // Jason: moved from while loop in Simulation
        // System.out.println("-- Step: "+Clock.Time());
        if (priority > 0) {
            robot.getBehaviour().priorityArrival(priority);
            //LEE : added print statement here, removed from robot behaviors.
            // seems that it makes more sense to be here instead of having
            // each robot do it individually.
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
    
    public IMailPool getMailPool(){
        return mailPool;
    }
    
    public Building getBuilding(){
        return building;
    }
    
    public static void addBehaviour(String description, IRobotBehaviour behaviour){
        behaviours.put(description, behaviour);
    }
    
    
    
    
}
