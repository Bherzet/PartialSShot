package cz.tzima.partialsshot.controller.messages;

/**
 * @author Tomas Zima
 * @see    StateType#Nth_CORNER_SELECTED
 */
public final class StateMessageNthCornerSelected implements StateMessage {
	/** Count of selected corners. */
	private final int number;
	
	/**
	 * Only stores values.
	 * 
	 * @param count
	 *     Count of selected corners.
	 */
	public StateMessageNthCornerSelected(int count) {
		this.number = count;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return
	 *     Value {@link StateType#Nth_CORNER_SELECTED}.
	 */
	@Override
	public StateType getStateType() {
		return StateType.Nth_CORNER_SELECTED;
	}
	
	/**
	 * @return
	 *     Count of selected corners.
	 */
	public int getNumber() {
		return number;
	}
}
