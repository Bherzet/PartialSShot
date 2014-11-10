package cz.tzima.partialsshot.camera;

/**
 * This exception signalizes either that currently used camera {@link Camera}
 * didn't received a required number of points or that such points couldn't be
 * used (e.g. if they're same).
 *  
 * @author Tomas Zima
 */
@SuppressWarnings("serial")
public class InvalidAreaSelectException extends Exception {
	/**
	 * Non-parametric constructor. Do nothing.
	 */
	public InvalidAreaSelectException() {
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
	public InvalidAreaSelectException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Passes value to the Exception's constructor.
	 *
	 * @param message
	 *     Error message describing the error.
	 */
	public InvalidAreaSelectException(String message) {
		super(message);
	}

	/**
	 * Passes value to the Exception's constructor.
	 *
	 * @param cause
	 *     Original exception which caused the error.
	 */
	public InvalidAreaSelectException(Throwable cause) {
		super(cause);
	}
}
