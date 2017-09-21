/* SWEN30006 Software Modelling and Design 
 * Semester 2, 2017
 * MailPool Class - Contains the functionality for creating and managing pools of priority
 * and non-priority mailItems.
 * It contains comparators for sorting priorityMailItems items by priority level, and 
 * non-priorityMailItems by arrival time.
 * 
 * Authors: <Willem Stewart> <Lee Mintz> <Jason Pursey>
 * Student Numbers: <695852> <932739> <637551>
 */

package automail;

import java.util.ArrayList;
import java.util.Comparator;

public class MailPool implements IMailPool{
	

	private ArrayList<MailItem> nonPriorityPool;
	private ArrayList<MailItem> priorityPool;
	
	public MailPool(){
		nonPriorityPool = new ArrayList<MailItem>();
		priorityPool = new ArrayList<MailItem>();
	}
	
	public int getPriorityPoolSize(){
		return priorityPool.size();
	}

	public int getNonPriorityPoolSize() {
		return nonPriorityPool.size();
	}

	/**
	 * Adds a mailItem to one of the two mailPool ArrayLists based on whether it is a PriorityMailItem or not.
	 * Once added, addToPool calls the .sort() method available to ArrayLists, giving the appropriate comparator
	 * as an argument, so that the highest priority / earliest arriving items of each pool are closest to the head.
	 * @param mailItem mailItem instance to be added
	 * 
	 */
	public void addToPool(MailItem mailItem) {
		// Check whether it has a priority or not
		if(mailItem instanceof PriorityMailItem){
			// Add to priority items
			priorityPool.add(mailItem);
			// Sort to maintain that highest priority items at front
			priorityPool.sort(new PriorityComparer());

		}
		else{
			// Add to nonpriority items
			nonPriorityPool.add(mailItem);
			// Sort to maintain that earliest arriving items at front
			nonPriorityPool.sort(new NonPriorityComparer());
		}
	}
	
	/**
	 * Returns the MailItem at the head of the nonPriorityPool if one exists, or null if not.
	 */
	public MailItem getNonPriorityMail(){
		if(getNonPriorityPoolSize() > 0){
			return nonPriorityPool.remove(0);
		}
		else{
			return null;
		}
	}
	/**
	 * Returns the MailItem at the head of the PriorityPool if one exists, or null if not.
	 */
	public MailItem getHighestPriorityMail(){
		if(getPriorityPoolSize() > 0){
			return priorityPool.remove(0);
		}
		else{
			return null;
		}
		
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//getBestMail unused in our current setup. left code here in case it becomes more useful in a future version//
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public MailItem getBestMail(int FloorFrom, int FloorTo) {
		
		ArrayList<MailItem> tempPriority = new ArrayList<MailItem>();
		
		// Check if there are any priority mail within the range
		for(MailItem m : priorityPool){
			if(isWithinRange(m,FloorFrom,FloorTo)){
				tempPriority.add(m);
			}
		}
		
		// If there is already something in priority then return it as the best mail
		if(tempPriority.size() > 0){
			// Since priorityPool is already sorted, that means items being added are already sorted with the
			// highest priority being in the front of the arraylist
			
			return tempPriority.get(0);
		}
		else{

			ArrayList<MailItem> tempNonPriority = new ArrayList<MailItem>();
			// Try the same thing with nonPriorityPool
			for(MailItem m : nonPriorityPool){
				if(isWithinRange(m,FloorFrom,FloorTo)){
					tempNonPriority.add(m);
				}
			}
			if(tempNonPriority.size() > 0){
				return tempNonPriority.get(0);
			}
		}
		
		return null;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//isWithinRange unused in our current setup. left code here in case it becomes more useful in a future version//
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private boolean isWithinRange(MailItem m, int FloorFrom, int FloorTo){

		if(m.getDestFloor() <= FloorTo && m.getDestFloor() >= FloorFrom){
			return true;
		}
		else{
			return false;
		}
		

	}

	/**
	 * Comparator classes and helper method
	 * Used by the ArrayList class's sort() function
	 */
	private class PriorityComparer implements Comparator<MailItem> {
		// Compare Priority level, if they are the same, try comparing arrival time
		public int compare(MailItem m1, MailItem m2){
			if(((PriorityMailItem)m1).getPriorityLevel() > ((PriorityMailItem)m2).getPriorityLevel()){
				return -1;
			}
			else if(((PriorityMailItem)m1).getPriorityLevel() == ((PriorityMailItem)m2).getPriorityLevel()){
				return compareArrival(m1,m2);
			}
			else{
				return 1;
			}
		}
	}
	
	private class NonPriorityComparer implements Comparator<MailItem>{
		
		// Compare arrival time
		public int compare(MailItem m1, MailItem m2){
			return compareArrival(m1,m2);
		}
	}
	/**
	 * Checks when two MailItems arrived and returns an int to indicate which is oldest
	 * @param m1 MailItem to compare
	 * @param m2 MailItem to compare against
	 * @return -1 if m1 arrived first, 0 if same, 1 if m2 arrived first
	 */
	public static int compareArrival(MailItem m1, MailItem m2){
		if(m1.getArrivalTime() < m2.getArrivalTime()){
			return -1;
		}
		else if(m1.getArrivalTime() == m2.getArrivalTime()){
			return 0;
		}
		else{
			return 1;
		}
	}
	
	
}
