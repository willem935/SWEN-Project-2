

public class BigSimpleRobotBehaviour implements IRobotBehaviour{
	
	private boolean newPriority; // Used if we are notified that a priority item has arrived. 
		
	public BigSimpleRobotBehaviour() {
		newPriority = false;
	}

	@Override
	// Will ****** this function is painfully terrible, thinking of just using the smart behaviour here
	public boolean fillStorageTube(IMailPool mailPool, StorageTube tube) {
		// Priority items are important;
		// if there are some, grab one and go, otherwise take as many items as we can and go
		try{
			// Start afresh
			newPriority = false;
			while(!tube.isEmpty()) {
				mailPool.addToPool(tube.pop());
			}
			// Check for a top priority item
			if (mailPool.getPriorityPoolSize() > 0) {
				// Add priority mail item
				tube.addItem(mailPool.getHighestPriorityMail());
				// Go deliver that item
				return true;
			}
			else{
				// Get as many nonpriority items as available or as fit
				while(tube.getSize() < tube.MAXIMUM_CAPACITY && mailPool.getNonPriorityPoolSize() > 0) {
					tube.addItem(mailPool.getNonPriorityMail());
				}
				return (tube.getSize() > 0);
			}
		}
		catch(TubeFullException e){
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
    public void priorityArrival(int priority) {
    	// Record that a new one has arrived
    	newPriority = true;
    	System.out.println("T: "+Clock.Time()+" | Priority arrived");
    }
 
	

}

	
	
	
	
}
