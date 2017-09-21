package strategies;

import java.util.ArrayList;
import java.util.Comparator;

import automail.IMailPool;
import automail.MailItem;
import automail.MailPool;
import automail.PriorityMailItem;
import exceptions.TubeFullException;
import robot.SmallTube;
import robot.StorageTube;

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
		while( tempTube.size()  < tube.getCapacity()){
			if(mailPool.getPriorityPoolSize() > 0){//LEE: removed containMail call, seems redundant when we have these get__Size() functionality
				tempTube.add(mailPool.getHighestPriorityMail());
			}
			else {
				// Fill it up with non priority
				if(mailPool.getNonPriorityPoolSize() > 0){ //LEE: removed containMail call, seems redundant when we have these get__Size() functionality
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

	
	private class ArrivalComparer implements Comparator<MailItem>{

		@Override
		public int compare(MailItem m1, MailItem m2) {
			return MailPool.compareArrival(m1, m2);
		}
		
	}


	@Override
	public StorageTube createTube() {
		// TODO Auto-generated method stub
		return new SmallTube();
	}

}
