/* SWEN30006 Software Modelling and Design 
 * Semester 2, 2017
 * BigTube Class - Extends StorageTube and has a size of six.
 * 
 * Authors: <Willem Stewart> <Lee Mintz> <Jason Pursey>
 * Student Numbers: <695852> <932739> <637551>
 */
package robot;

/**
 * The storage tube carried by the robot.
 */
public class BigTube extends StorageTube {

	/** the maximum tube size */
	private int tubeSize = 6;

	@Override
	public int getCapacity() {
		return tubeSize;
	}

}
