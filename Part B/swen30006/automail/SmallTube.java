package automail;

// import exceptions.RobotNotInMailRoomException;
import exceptions.TubeFullException;

import java.util.Stack;

/**
 * The storage tube carried by the robot.
 */
public class SmallTube extends StorageTube{

    @Override
    public void setCapacity() {
        this.MAXIMUM_CAPACITY = 4;
    }
    


}
