package automail;

public class Building {

    /** The number of floors in the building */
    private int floors;
    /** Represents the ground floor location */
    private int lowestFloor; 
    /** Represents the mailroom location */
   private int mailRoomLocation;
   
   
   
   public Building(int floors, int lowestFloor, int mailRoomLocation){
       this.floors = floors;
       this.lowestFloor = lowestFloor;
       this.mailRoomLocation = mailRoomLocation;
   }
   
   
   /** Returns the number of floors **/
    public int getFloors() {
        return floors;
    }
    
    /** Returns the lowest floor **/
    public int getLowestFloor() {
        return lowestFloor;
    }

    
    /** Returns the mail room location **/
    public int getMailRoomLocation() {
        return mailRoomLocation;
    }

}
