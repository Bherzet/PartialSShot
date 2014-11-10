package cz.tzima.partialsshot.controller.messages;

/**
 * @author Tomas Zima
 * @see    StateType#DEFAULT
 */
public final class StateMessageDefault implements StateMessage {
	/**
	 * {@inheritDoc}
	 * 
	 * @return
	 *     Value {@link StateType#DEFAULT}.
	 */
	@Override
	public StateType getStateType() {
		return StateType.DEFAULT;
	}
}
