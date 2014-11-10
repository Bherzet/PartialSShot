package cz.tzima.partialsshot.publishing;

/**
 * This exception signalizes that a screenshot couldn't be published via
 * required content publisher.
 * 
 * @author Tomas Zima
 */
@SuppressWarnings("serial")
public class ContentPublishingException extends Exception {
	/**
	 * Non-parametric constructor. Do nothing.
	 */
	public ContentPublishingException() {
		super();
	}

	/**
	 * Passes value to the Exception's constructor.
	 *
	 * @param message Error message describing the error.
	 * @param cause   Original exception which caused the error.
	 */
	public ContentPublishingException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Passes value to the Exception's constructor.
	 *
	 * @param message Error message describing the error.
	 */
	public ContentPublishingException(String message) {
		super(message);
	}

	/**
	 * Passes value to the Exception's constructor.
	 *
	 * @param cause Original exception which caused the error.
	 */
	public ContentPublishingException(Throwable cause) {
		super(cause);
	}
}
