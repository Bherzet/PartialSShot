package cz.tzima.partialsshot.controller.messages;

/**
 * Implementations of this interface represents the messages which are used for
 * signalizing the change of application's state.
 * 
 * @author Tomas Zima
 */
public interface StateMessage {
	/**
	 * After checking a type of the message, you're able to safely cast to the
	 * corresponding class (e.g. when it's {@link StateType#DEFAULT} to {@link
	 * StateMessageDefault})..
	 * 
	 * @return
	 *     Type of the message.
	 */
	public StateType getStateType();
}
