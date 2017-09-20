package robot;

// import exceptions.RobotNotInMailRoomException;

/**
 * The storage tube carried by the robot.
 */
public class SmallTube extends StorageTube{
		
	private int tubeSize = 4;
	
    @Override
    public int getCapacity() {
        return tubeSize;
    }
    


}
