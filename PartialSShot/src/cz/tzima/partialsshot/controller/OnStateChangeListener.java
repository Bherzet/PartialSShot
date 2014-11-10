package cz.tzima.partialsshot.controller;

import cz.tzima.partialsshot.controller.messages.StateMessage;
import cz.tzima.partialsshot.controller.messages.StateType;

/**
 * Listener through which controller notifies the logical core about a change
 * of application's current status. This enables the frontend to let user know
 * about processing of his operations.
 * 
 * @author Tomas Zima
 */
public interface OnStateChangeListener {
	/**
	 * <p>Method which is triggered each time the state is changed. Frontend
	 * should somehow display this information to the user.
	 * 
	 * <p>Consult {@link StateType} to see the exact meaning of each state and
	 * concept of the state itself.
	 * 
	 * @param message
	 *     The message describing the current state. 
	 */
	public void stateChanged(StateMessage message);
}
