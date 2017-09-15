package strategies;

import automail.Building;
import automail.Clock;
import automail.IMailDelivery;
import automail.Robot;
import exceptions.ExcessiveDeliveryException;

public class Automail {
	      
    private Robot robot;
    private IMailPool mailPool;
    private Building building;
    
    public Automail(IMailDelivery delivery, int floors, int lowestFloor, int mailRoomLocation) {
    	// jason: still to do
        //          make robot dynamically
    	/** Initialize the MailPool */
    	mailPool = new MailPool();
    	
        /** Initialize the RobotAction */
    	// IRobotBehaviour robotBehaviour = new SimpleRobotBehaviour();
    	IRobotBehaviour robotBehaviour = new SmartRobotBehaviour();
    	    	
    	/** Initialize robot */
        building = new Building(floors, lowestFloor, mailRoomLocation);
    	robot = new Robot(robotBehaviour, delivery, mailPool, building);
    	
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
    
    
    
    
}
