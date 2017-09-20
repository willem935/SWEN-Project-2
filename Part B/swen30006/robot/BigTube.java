package robot;

import automail.StorageTube;

/**
 * The storage tube carried by the robot.
 */
public class BigTube extends StorageTube{
	
	private int tubeSize = 6;
	
	
    @Override
    public int getCapacity() {
        return tubeSize;
    }
    


}
