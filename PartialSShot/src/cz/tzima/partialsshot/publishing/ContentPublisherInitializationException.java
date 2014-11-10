package cz.tzima.partialsshot.publishing;

/**
 * This exception signalizes the a new instance of a content publisher couldn't
 * be created.
 * 
 * @author Tomas Zima
 */
@SuppressWarnings("serial")
public class ContentPublisherInitializationException extends Exception {
	/**
	 * Non-parametric constructor. Do nothing.
	 */
	public ContentPublisherInitializationException() {
		super();
	}

	/**
	 * Passes value to the Exception's constructor.
	 *
	 * @param message Error message describing the error.
	 * @param cause   Original exception which caused the error.
	 */
	public ContentPublisherInitializationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Passes value to the Exception's constructor.
	 *
	 * @param message Error message describing the error.
	 */
	public ContentPublisherInitializationException(String message) {
		super(message);
	}

	/**
	 * Passes value to the Exception's constructor.
	 *
	 * @param cause Original exception which caused the error.
	 */
	public ContentPublisherInitializationException(Throwable cause) {
		super(cause);
	}
}
