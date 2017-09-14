package strategies;

import automail.Clock;
import automail.IMailDelivery;
import automail.Robot;
import exceptions.ExcessiveDeliveryException;

public class Automail {
	      
    public Robot robot;
    public IMailPool mailPool;
    
    public Automail(IMailDelivery delivery) {
    	// Swap between simple provided strategies and your strategies here
    	    	
    	/** Initialize the MailPool */
    	mailPool = new MailPool();
    	
        /** Initialize the RobotAction */
        // Jason: need to make this change dynamic based on specified property
        // maybe add a parameter to the constructor? 
    	// IRobotBehaviour robotBehaviour = new SimpleRobotBehaviour();
    	IRobotBehaviour robotBehaviour = new SmartRobotBehaviour();
    	    	
    	/** Initialize robot */
    	robot = new Robot(robotBehaviour, delivery, mailPool);
    	
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
    
}
