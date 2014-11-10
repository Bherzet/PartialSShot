package cz.tzima.partialsshot.camera;
import java.awt.AWTException;;

/**
 * This exception signalizes that screenshot couldn't be made. This could be
 * caused by some technical error (for example {@link AWTException}).
 * 
 * @author Tomas Zima
 */
@SuppressWarnings("serial")
public class CameraActionException extends Exception {
	/**
	 * Non-parametric constructor. Do nothing.
	 */
	public CameraActionException() {
		super();
	}

	/**
	 * Passes value to the Exception's constructor.
	 *
	 * @param message
	 *     Error message describing the error.
	 * @param cause
	 *     Original exception which caused the error.
	 */
	public CameraActionException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Passes value to the Exception's constructor.
	 *
	 * @param message
	 *     Error message describing the error.
	 */
	public CameraActionException(String message) {
		super(message);
	}

	/**
	 * Passes value to the Exception's constructor.
	 *
	 * @param cause
	 *     Original exception which caused the error.
	 */
	public CameraActionException(Throwable cause) {
		super(cause);
	}
}
