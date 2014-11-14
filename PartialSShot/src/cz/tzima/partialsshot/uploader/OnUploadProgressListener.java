package cz.tzima.partialsshot.uploader;

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
	 */
	public void onUploadFinished();
}
