package cz.tzima.partialsshot.controller.messages;

/**
 * @author Tomas Zima
 * @see    StateType#ACTIVATED
 */
public final class StateMessageActivated implements StateMessage {
	/**
	 * {@inheritDoc}
	 * 
	 * @return
	 *     Value {@link StateType#ACTIVATED}.
	 */
	@Override
	public StateType getStateType() {
		return StateType.ACTIVATED;
	}
}
