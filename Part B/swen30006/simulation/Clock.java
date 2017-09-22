/* SWEN30006 Software Modelling and Design 
 * Semester 2, 2017
 * Clock Class - Used to track time during simulation to maintain score and when
 * to release new mail.
 *  
 * Authors: <Willem Stewart> <Lee Mintz> <Jason Pursey>
 * Student Numbers: <695852> <932739> <637551>
 */

package simulation;

public class Clock {

	/** Represents the current time **/
	private static int Time = 0;

	/** The threshold for the latest time for mail to arrive **/
	private static int LAST_DELIVERY_TIME;

	public static int Time() {
		return Time;
	}

	public static void Tick() {
		Time++;
	}

	public static void setLastDeliveryTime(int time) {
		LAST_DELIVERY_TIME = time;
	}

	public static int getLastDeliveryTime() {
		return LAST_DELIVERY_TIME;
	}
}
