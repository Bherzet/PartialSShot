package cz.tzima.partialsshot.controller.messages;

/**
 * <p>Represents current status of the application. Status changes as user
 * enters data and requests actions.</p>
 * 
 * <p>The standard cycle while taking screenshot looks like:
 *   <ul>
 *     <li>{@link #DEFAULT}             - after startup</li>
 *     <li>{@link #ACTIVATED}           - shortcut has been pressed. Program
 *                                        is activated (waits for other commands).</li>
 *     <li>{@link #Nth_CORNER_SELECTED} - N corners has been selected. After the
 *                                        second corner would be selected, screenshot
 *                                        would be taken and status would be changed
 *                                        back to the default.</li>
 *   </ul>
 *
 * @author Tomas Zima
 */
public enum StateType {
	/**
	 * Default application's state (on startup or after action has been done
	 * or canceled).
	 * 
	 * @see StateMessageDefault
	 */
	DEFAULT,
	
	/**
	 * After shortcut is pressed, but no action was requested yet.
	 * 
	 * @see StateMessageActivated
	 */
	ACTIVATED,

	/**
	 * N points (from which shape of the screenshot will be derived) has been
	 * selected.
	 * 
	 * @see StateMessageNthCornerSelected
	 */
	Nth_CORNER_SELECTED
}
