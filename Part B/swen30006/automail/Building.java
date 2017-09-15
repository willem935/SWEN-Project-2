package automail;

public class Building {


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
   
    public int getFloors() {
        return floors;
    }

    public int getLowestFloor() {
        return lowestFloor;
    }

    /*
    /** The number of floors in the building **/
    public int getMailRoomLocation() {
        return mailRoomLocation;
    }

}
