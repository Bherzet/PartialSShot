package cz.tzima.partialsshot.uploader;

import cz.tzima.partialsshot.publishing.ContentPublisher;

/**
 * Listener for obtaining informations from an uploader (running in its own
 * thread) to the core.
 * 
 * @author Tomas Zima
 */
public interface OnUploadProgressListener {
	/**
	 * This method is triggered once upload finished (either successfuly or with
	 * failure).
	 * 
	 * @param contentPublisher
	 *     ContentPublisher which finished.
	 * @param failure
	 *     Null if upload finished successfuly or exception which caused the error.
	 */
	public void onUploadFinished(ContentPublisher contentPublisher, Throwable failure);
}
