package exceptions;

/**
 * An exception thrown when the robot tries to deliver more items than its tube capacity without refilling.
 */
public class ExcessiveDeliveryException extends Throwable {
	public ExcessiveDeliveryException(int tubeCap){
		super("Attempting to deliver more than " + tubeCap + " items in a single trip!!");
	}
}
