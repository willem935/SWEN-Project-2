/* SWEN30006 Software Modelling and Design 
 * Semester 2, 2017
 * IMailDelivery interface - structure for MailDelivery.
 *  
 * Authors: <Willem Stewart> <Lee Mintz> <Jason Pursey>
 * Student Numbers: <695852> <932739> <637551>
 */

package simulation;

import automail.MailItem;

/**
 * a MailDelivery is used by the Robot to deliver mail once it has arrived at
 * the correct location
 */
public interface IMailDelivery {

	/**
	 * Delivers an item at its floor
	 * 
	 * @param mailItem
	 *            the mail item being delivered.
	 */
	void deliver(MailItem mailItem);

}