package strategies;

import java.util.ArrayList;
import java.util.Comparator;

import automail.MailItem;
import automail.PriorityMailItem;
import automail.SmallTube;
import automail.StorageTube;
import exceptions.TubeFullException;

public class SmartRobotBehaviour implements IRobotBehaviour{
	
	private int newPriorityArrival;
	
	public SmartRobotBehaviour(){
		newPriorityArrival = 0;
	}
	@Override
	public boolean returnToMailRoom(StorageTube tube) {
		// Check if my tube contains only priority items
		if(!tube.isEmpty()){
			int priorityCount = 0;
			int nonPriorityCount = 0;
			// There has to be more priority than non-priority to keep going
			for(MailItem m : tube.tube){
				if(m instanceof PriorityMailItem){
					priorityCount++;
				}
				else{
					nonPriorityCount++;
				}
			}
			
			if(priorityCount >= nonPriorityCount){
				return false;
			}
			else{
				// Check if there is more than 1 priority arrival and the size of the tube is greater than or equal to half
				if(newPriorityArrival > 1 && tube.getSize() >= tube.getCapacity()/2){

					return true;
				}
				else{
					return false;
				}
				
			}
		}
		else{
			return true;
		}
	}

	@Override
	public void priorityArrival(int priority) {
    	// Record that a new one has arrived
		newPriorityArrival++;
    	// removed print statement, added to simulation.java instead.
		
	}

	@Override
	public boolean fillStorageTube(IMailPool mailPool, StorageTube tube) {
		
		ArrayList<MailItem> tempTube = new ArrayList<MailItem>();

		// Empty my tube
		while(!tube.tube.isEmpty()){
			mailPool.addToPool(tube.pop());
		}
		
		// Grab priority mail
		//int size = tempTube.size();
		//int cap = tube.getCapacity();
		while( tempTube.size()  < tube.getCapacity()){
			// Will 14/9 **** trying to avoid using containMail but its throwing errors.
			if(mailPool.getPriorityPoolSize() > 0){
			//if(containMail(mailPool,MailPool.PRIORITY_POOL)){
				tempTube.add(mailPool.getHighestPriorityMail());
			}
			else {
				// Fill it up with non priority
				if(containMail(mailPool,MailPool.NON_PRIORITY_POOL)){
					tempTube.add(mailPool.getNonPriorityMail());
				}
				else{
					break;
				}
				
			}
		}
		
		// Sort tempTube based on floor
		tempTube.sort(new ArrivalComparer());
		
		// Iterate through the tempTube
		while(tempTube.iterator().hasNext()){
			try {
				tube.addItem(tempTube.remove(0));
			} catch (TubeFullException e) {
				e.printStackTrace();
			}
		}
		
		// Check if there is anything in the tube
		if(!tube.tube.isEmpty()){
			newPriorityArrival = 0;
			return true;
		}
		return false;
	}
	
	private boolean containMail(IMailPool m, String mailPoolIdentifier){
		if(mailPoolIdentifier.equals(MailPool.PRIORITY_POOL) && m.getPriorityPoolSize() > 0){
			return true;
		}
		else if(mailPoolIdentifier.equals(MailPool.NON_PRIORITY_POOL) && m.getNonPriorityPoolSize() > 0){
			return true;
		}
		else{
			return false;
		}
	}

    @Override
    public StorageTube getTube() {
        return new SmallTube();
    }
	
	private class ArrivalComparer implements Comparator<MailItem>{

		@Override
		public int compare(MailItem m1, MailItem m2) {
			return MailPool.compareArrival(m1, m2);
		}
		
	}

}
