package cz.tzima.partialsshot.uploader;

import java.awt.image.BufferedImage;

import cz.tzima.partialsshot.publishing.ContentPublisher;
import cz.tzima.partialsshot.publishing.ContentPublishingException;

/**
 * <p>Runs the content publisher in its own thread, therefore preventing
 * freezing of the main thread.
 * 
 * <p>You need to register your own {@link OnUploadProgressListener} (see {@link
 * #setOnUploadProgressListener(OnUploadProgressListener)}) to connect it to
 * application's logic.
 * 
 * @author Tomas Zima
 */
public class Uploader extends Thread {
	/** ContentPublisher which will be executed in the thread. */
	private final ContentPublisher contentPublisher;
	/** Screenshot being published. */
	private final BufferedImage screenshot;
	/** Name of the screenshot. */
	private final String name;
	/** Listener which will be triggered when upload finishes. */
	private OnUploadProgressListener onUploadProgressListener;
	
	/**
	 * Stores values. Uses auto-generated name for the screenshot.
	 * 
	 * @param contentPublisher
	 *     Implementation of {@link ContentPublisher} which will be used to
	 *     publish the screenshot.
	 * @param screenshot
	 *     Screenshot to be published. 
	 */
	public Uploader(ContentPublisher contentPublisher, BufferedImage screenshot) {
		this.contentPublisher = contentPublisher;
		this.screenshot       = screenshot;
		this.name             = null;
	}
	
	/**
	 * Stores values.
	 * 
	 * @param contentPublisher
	 *     Implementation of {@link ContentPublisher} which will be used to
	 *     publish the screenshot..
	 * @param screenshot
	 *     Screenshot to be published.
	 * @param name
	 *     Name of the screenshot.
	 */
	public Uploader(ContentPublisher contentPublisher, BufferedImage screenshot, String name) {
		this.contentPublisher = contentPublisher;
		this.screenshot       = screenshot;
		this.name             = name;
	}
	
	/**
	 * Sets listener which will be triggered after upload finishes (either
	 * sucessfuly or with a failure).
	 * 
	 * @param onUploadProgressListener
	 *     See {@link OnUploadProgressListener}.
	 */
	public void setOnUploadProgressListener(OnUploadProgressListener onUploadProgressListener) {
		this.onUploadProgressListener = onUploadProgressListener;
	}
	
	/**
	 * Calls {@link ContentPublisher#publish(BufferedImage)} with a name (when
	 * specified) or without a name.
	 * 
	 * @throws ContentPublishingException
	 *     Exception thrown by a {@link ContentPublisher}.
	 */
	private void publish() throws ContentPublishingException {
		if (name == null) {
			contentPublisher.publish(screenshot);
		} else {
			contentPublisher.publish(screenshot, name);
		}
	}
	
	/**
	 * Runs upload.
	 */
	@Override
	public void run() {
		// null ~= success
		Throwable failure = null;

		try {
			publish();
		} catch (ContentPublishingException e) {
			// save exception and pass it to the listener
			failure = e;
		} finally {
			// invoke listener
			if (this.onUploadProgressListener != null) {
				this.onUploadProgressListener.onUploadFinished(contentPublisher, failure);
			}
		}
	}
}
