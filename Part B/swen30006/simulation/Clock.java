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
    
    // 19/9 Jason: created these methods to allow LDT to be private
    public static void setLastDeliveryTime(int time){
        LAST_DELIVERY_TIME = time;
    }
    
    public static int getLastDeliveryTime(){
        return LAST_DELIVERY_TIME;
    }
}
