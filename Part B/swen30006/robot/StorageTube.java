/* SWEN30006 Software Modelling and Design 
 * Semester 2, 2017
 * StorageTube class - Contains functionality that the robot uses to deliver the mail, 
 * including addItem and peek.
 * 
 * Authors: <Willem Stewart> <Lee Mintz> <Jason Pursey>
 * Student Numbers: <695852> <932739> <637551>
 */

package robot;

// import exceptions.RobotNotInMailRoomException;
import exceptions.TubeFullException;

import java.util.Stack;

import automail.MailItem;

/**
 * The storage tube carried by the robot.
 */
public abstract class StorageTube {

	public Stack<MailItem> tube;

	/**
	 * Constructor for the storage tube
	 */
	public StorageTube() {
		this.tube = new Stack<MailItem>();
	}

	public abstract int getCapacity();

	/**
	 * j: Changed tube.capacity() -> tube.size() so acts as expected
	 * 
	 * @return if the storage tube is full
	 */
	public boolean isFull() {
		return tube.size() == getCapacity();
	}

	/**
	 * @return if the storage tube is empty
	 */
	public boolean isEmpty() {
		return tube.isEmpty();
	}

	/**
	 * @return the first item in the storage tube (without removing it)
	 */
	public MailItem peek() {
		return tube.peek();
	}

	/**
	 * Add an item to the tube
	 * 
	 * @param item
	 *            The item being added
	 * @throws TubeFullException
	 *             thrown if an item is added which exceeds the capacity
	 */
	public void addItem(MailItem item) throws TubeFullException {
		int current = getSize();
		if (current + 1 <= getCapacity()) {
			tube.add(item);
		} else {
			throw new TubeFullException();
		}
	}

	/** @return the size of the tube **/
	public int getSize() {
		return tube.size();
	}

	/**
	 * @return the first item in the storage tube (after removing it)
	 */
	public MailItem pop() {
		return tube.pop();
	}

}
