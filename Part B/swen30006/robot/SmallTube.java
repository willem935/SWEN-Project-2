/* SWEN30006 Software Modelling and Design 
 * Semester 2, 2017
 * SmallTube Class - Extends StorageTube and has a size of six.
 * 
 * Authors: <Willem Stewart> <Lee Mintz> <Jason Pursey>
 * Student Numbers: <695852> <932739> <637551>
 */

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
