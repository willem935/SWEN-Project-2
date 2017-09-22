/* SWEN30006 Software Modelling and Design 
 * Semester 2, 2017
 * PriorityMailItem Class - Extends MailItem to provide for mail 
 * that are priority deliveries
 * 
 * Authors: <Willem Stewart> <Lee Mintz> <Jason Pursey>
 * Student Numbers: <695852> <932739> <637551>
 */

package automail;

public class PriorityMailItem extends MailItem {

	/** The priority of the mail item from 1 low to 100 high */
	private final int PRIORITY_LEVEL;

	public PriorityMailItem(int dest_floor, int priority_level,
			int arrival_time) {
		super(dest_floor, arrival_time);
		this.PRIORITY_LEVEL = priority_level;
	}

	/**
	 *
	 * @return the priority level of a mail item
	 */
	public int getPriorityLevel() {
		return PRIORITY_LEVEL;
	}

	@Override
	public String toString() {
		return super.toString() + "| Priority Level: " + PRIORITY_LEVEL;
	}

}
