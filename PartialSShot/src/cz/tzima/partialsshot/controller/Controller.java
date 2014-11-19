package cz.tzima.partialsshot.controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;
import org.jnativehook.mouse.NativeMouseMotionListener;

import cz.tzima.partialsshot.controller.messages.StateMessage;
import cz.tzima.partialsshot.controller.messages.StateMessageActivated;
import cz.tzima.partialsshot.controller.messages.StateMessageDefault;
import cz.tzima.partialsshot.controller.messages.StateMessageNthCornerSelected;
import cz.tzima.partialsshot.controller.messages.StateType;

/**
 * Decides which actions should be executed based on user's keyboard or mouse
 * input.
 * 
 * @author Tomas Zima
 */
public final class Controller implements NativeKeyListener, NativeMouseListener, NativeMouseMotionListener {
	// -- keyboard handling
	/** Count of all recognizable keys. */
	private static final int keysCount = KeyType.values().length;
	/**
	 * Array with state of all recognizable keys. Index is by calling ordinal()
	 * on the value from {@link KeyType}.
	 */
	private boolean[] pressedKeys = new boolean[keysCount];
	
	// -- mouse handling
	/** Last X-coordinate where mouse moved. */
	private int lastX = 0;
	/** Last Y-coordinate where mouse moved. */
	private int lastY = 0;
	
	// -- listeners
	/** Listener which is triggered each time application's state is changed. */
	private OnStateChangeListener onStateChangeListener = null;
	/** Listener which is triggered each time screenshot is requested. */
	private OnScreenshotRequestListener onScreenshotRequestListener = null;
	
	// -- variables for logic
	/** Current application's state. */
	private StateMessage currentState = new StateMessageDefault();
	/** List of all selected points. */
	private List<Point> points = new ArrayList<Point>();
	/** Requested count of points. */
	private int requestedPointsCount = 0;
	
	/**
	 * Hooks up keyboard and mouse listeners.
	 * 
	 * @throws NativeHookException Internal technical error.
	 */
	public void register() throws NativeHookException {
		if (!GlobalScreen.isNativeHookRegistered()) {
			GlobalScreen.registerNativeHook();
		}
		
		GlobalScreen globalScreen = GlobalScreen.getInstance();
		globalScreen.addNativeKeyListener(this);
		globalScreen.addNativeMouseListener(this);
		globalScreen.addNativeMouseMotionListener(this);
	}
	
	/**
	 * Sets a listener which is triggered each time screenshot is requested.
	 * 
	 * @param onScreenshotRequestListener
	 *     Implementation of {@link OnScreenshotRequestListener}.
	 */
	public void setOnScreenshotRequestListener(OnScreenshotRequestListener onScreenshotRequestListener) {
		this.onScreenshotRequestListener = onScreenshotRequestListener;
	}
	
	/**
	 * Sets a listener which is triggered each time application's state is changed.
	 * 
	 * @param onStatusChangeListener
	 *     Implementation of {@link OnStateChangeListener}.
	 */
	public void setOnStatusChangeListener(OnStateChangeListener onStatusChangeListener) {
		this.onStateChangeListener = onStatusChangeListener;
	}
	
	/**
	 * Called each time a key is pressed (at system level) by jNativeHook.
	 * 
	 * @param e
	 *     Event.
	 */
	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		setKeyState(e.getKeyCode(), true);

		// trigger action if shortcut is pressed 
		if (isShortcutPressed(new KeyType[]{KeyType.CTRL_L, KeyType.SHIFT_L, KeyType.PRTSCR})) {
			this.switchState();
		} else if (isShortcutPressed(new KeyType[]{KeyType.ESC})) {
			if (this.currentState.getStateType() != StateType.DEFAULT) {
				this.currentState = new StateMessageDefault();
				this.points.clear();
				reportStateChange();
			}
		}
	}

	/**
	 * This method should be called each time a state changed. It will try to
	 * invoke a listener (if it has been registered).
	 */
	private void reportStateChange() {
		if (this.onStateChangeListener != null) {
			this.onStateChangeListener.stateChanged(currentState);
		}
	}

	/**
	 * Called each time a key is released (at system level) by jNativeHook.
	 * 
	 * @param e
	 *     Event.
	 */
	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
		setKeyState(e.getKeyCode(), false);
	}
	
	/**
	 * Changes the state of key.
	 * 
	 * @param keyCode Code of the key obtained from {@link NativeKeyEvent}.
	 * @param state   True if the key is pressed, false if not.
	 */
	private void setKeyState(int keyCode, boolean state) {
		// ignore unknown keys
		keyCode = KeyType.decodeKeyCode(keyCode);
		if (keyCode == -1) {
			return;
		}
		
		// set state
		pressedKeys[keyCode] = state;
	}

	/**
	 * Called each time a key is pressed and released by jNativeHook.
	 * 
	 * @param e
	 *     Event.
	 */
	@Override
	public void nativeKeyTyped(NativeKeyEvent e) {
		// empty purposely
	}

	/**
	 * Called each time a mouse button is pressed and released by jNativeHook.
	 * 
	 * @param e
	 *     Event.
	 */
	@Override
	public void nativeMouseClicked(NativeMouseEvent e) {
		// empty purposely
	}

	/**
	 * Called each time a mouse button is pressed by jNativeHook.
	 * 
	 * @param e
	 *     Event.
	 */
	@Override
	public void nativeMousePressed(NativeMouseEvent e) {
		// select another point if application is activated
		if (currentState.getStateType() != StateType.DEFAULT) {
			switchState();
		}
	}

	/**
	 * Switches state of application to the next logical point.
	 */
	private void switchState() {
		switch (currentState.getStateType()) {
			case DEFAULT: {
				this.currentState = new StateMessageActivated();
			} break;
			
			case ACTIVATED: {
				this.points.add(new Point(lastX, lastY));
				this.currentState = new StateMessageNthCornerSelected(1);
			} break;
			
			case Nth_CORNER_SELECTED: {
				this.points.add(new Point(lastX, lastY));
				
				// if enough points is selected
				if (this.points.size() == requestedPointsCount) {
					this.currentState = new StateMessageDefault();
					this.requestScreenshot();
					this.points.clear();
				// new point has been added
				} else {
					this.currentState = new StateMessageNthCornerSelected(
						((StateMessageNthCornerSelected) currentState).getNumber() + 1
					);
				}

			} break;

			default:
				break;
		}

		reportStateChange();
	}

	/**
	 * Called each time mouse button is released by jNativeHook.
	 * 
	 * @param e
	 *     Event.
	 */
	@Override
	public void nativeMouseReleased(NativeMouseEvent e) {
		// empty purposely
	}
	
	/**
	 * Checks if shortcut is pressed.
	 * 
	 * @param keys
	 *     List of the keys which should be pressed together.
	 * @return
	 *     True if shortcut (all the given keys togethers) is pressed.
	 */
	private boolean isShortcutPressed(KeyType[] keys) {
		for (KeyType key : keys) {
			if (!pressedKeys[key.ordinal()]) {
				return false;
			}
		}
		
		return true;
	}

	/**
	 * Called each time a mouse moved from one location to another with mouse
	 * button pressed by jNativeHook.
	 * 
	 * @param e
	 *     Event.
	 */
	@Override
	public void nativeMouseDragged(NativeMouseEvent e) {
		// empty purposely
	}

	/**
	 * Called each time a mouse moved (at system level) by jNativeHook. This
	 * is used for remembering where the mouse where last time (in the case
	 * point is selected using keyboard). It's unfortunately impossible to get
	 * mouse coordinates directly.
	 * 
	 * @param e
	 *     Event.
	 */
	@Override
	public void nativeMouseMoved(NativeMouseEvent e) {
		this.lastX = e.getX();
		this.lastY = e.getY();
	}

	/**
	 * Triggers listener if it has been registered.
	 */
	private void requestScreenshot() {
		if (this.onScreenshotRequestListener != null) {
			this.onScreenshotRequestListener.takeScreenshot(points);
		}
	}
	
	/**
	 * Sets count of points which are requested for determining the shape of the
	 * screenshot.
	 * 
	 * @param count Count of points.
	 */
	public void setRequestedPointsCount(int count) {
		this.requestedPointsCount = count;
	}
}
