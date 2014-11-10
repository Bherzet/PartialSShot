package cz.tzima.partialsshot.camera;

/**
 * This exception signalizes that requested camera couldn't be initialized.
 * 
 * @author Tomas Zima
 */
@SuppressWarnings("serial")
public class CameraInitializationException extends Exception {
	/**
	 * Non-parametric constructor. Do nothing.
	 */
	public CameraInitializationException() {
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
	public CameraInitializationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Passes value to the Exception's constructor.
	 *
	 * @param message
	 *     Error message describing the error.
	 */
	public CameraInitializationException(String message) {
		super(message);
	}

	/**
	 * Passes value to the Exception's constructor.
	 *
	 * @param cause
	 *     Original exception which caused the error.
	 */
	public CameraInitializationException(Throwable cause) {
		super(cause);
	}
}
