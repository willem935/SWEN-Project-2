package strategies;

import java.util.Comparator;
import java.util.ArrayList;

import automail.IMailPool;
import automail.MailItem;
import automail.MailPool;
import exceptions.TubeFullException;
import robot.StorageTube;

public class BigSimpleRobotBehaviour implements IRobotBehaviour{
	
		
	public BigSimpleRobotBehaviour() {}

	@Override
	// Will ****** this function is painfully terrible, thinking of just using the smart behaviour here
	// Lee **** I agree. It'd make sense that the bigger bot that can't respond
		// to incoming priority items would at least want to move around intelligently.
		//Implemented.
	
	public boolean fillStorageTube(IMailPool mailPool, StorageTube tube) {
		
		ArrayList<MailItem> tempTube = new ArrayList<MailItem>();

		// Empty my tube
		while(!tube.tube.isEmpty()){
			mailPool.addToPool(tube.pop());
		}
		
		// Grab priority mail
		while(tempTube.size() < tube.getCapacity()){
                    // 20/9 Jason: removed 2 calls to containMail()
			if(mailPool.getPriorityPoolSize() > 0){
				tempTube.add(mailPool.getHighestPriorityMail());
			}
			else{
				// Fill it up with non priority
				if(mailPool.getNonPriorityPoolSize() > 0){
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
			//newPriorityArrival = 0;
			return true;
		}
		return false;
	}

    @Override
    public boolean returnToMailRoom(StorageTube tube) {
        // big simple doesn't know about new arrivals, so never returns early
        return false;
    }

 
	
	private class ArrivalComparer implements Comparator<MailItem>{

		@Override
		public int compare(MailItem m1, MailItem m2) {
			return MailPool.compareArrival(m1, m2);
		}
		
	}
	
	@Override
    public void priorityArrival(int priority) {
		//big simple doesn't respond to incoming priority item's arriving
		//so this method shouldn't really do anything.
    }
 
	

}
