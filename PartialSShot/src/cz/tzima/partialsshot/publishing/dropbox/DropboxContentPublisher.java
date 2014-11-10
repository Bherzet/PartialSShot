package cz.tzima.partialsshot.publishing.dropbox;

import java.io.File;

import cz.tzima.partialsshot.Configuration;
import cz.tzima.partialsshot.publishing.ContentPublisher;
import cz.tzima.partialsshot.publishing.PublisherType;
import cz.tzima.partialsshot.publishing.local.LocalContentPublisher;

/**
 * Publishes screenshots to the local Dropbox's public directory. If Dropbox
 * daemon is properly set up and running, it should be immediately uploaded
 * online and link can be generated.
 * 
 * @author Tomas Zima
 */
public class DropboxContentPublisher extends LocalContentPublisher implements ContentPublisher {
	/** Configuration obtained from the file. */
	private DropboxPublisherConfiguration configuration = null;
	/** URL to the root of public Dropbox's folder. */
	private String linkBase = null;
	
	/**
	 * Initialize the {@link DropboxContentPublisher}.
	 * 
	 * @param configuration
	 *     Configuration obtained from the file.
	 */
	public DropboxContentPublisher(Configuration configuration) {
		this.configuration   = configuration.getDropboxPubConfiguration();
		this.linkBase        = getLinkBase();
		this.targetDirectory = new File(
			this.configuration.getPublicDirectoryRoot(),
			this.configuration.getRelativeTargetDirPath()
		);
	}

	/**
	 * Content published to the dropbox is linkable.
	 * 
	 * @return
	 *     Always `true`.
	 */
	@Override
	public boolean isLinkable() {
		return true;
	}
	
	/**
	 * @return
	 *     Link to the root of public Dropbox's folder.
	 */
	private String getLinkBase() {
		// generate link and put the user's id into the place
		String link = configuration.getPublicDirLink().replaceAll("%USER_ID%",configuration.getUserID());
		
		// link to the public dropbox's folder
		return makeDir(makeDir(link) + configuration.getRelativeTargetDirPath());
	}
	
	/**
	 * Modifies the name of the directory so there's a '/' in the end.
	 * 
	 * @param dir
	 *     Original name of the directory.
	 * @return
	 *     Modified name of the directory (with a slash in the end).
	 */
	private String makeDir(String dir) {
		if (dir.endsWith(File.separator)) {
			return dir;
		}
		
		return dir + File.separator;
	}

	/**
	 * @return
	 *     Link to the last screenshot.
	 * @throws IllegalStateException
	 *     Any screenshot has been made yet.
	 */
	@Override
	public String getTargetURL() {
		if (lastName == null) {
			throw new IllegalStateException("Any screenshot hasn't been published yet!");
		}
		
		return linkBase + lastName + ".png";
	}
	
	/**
	 * @return
	 *     Always {@link PublisherType#DROPBOX}.
	 */
	@Override
	public PublisherType getType() {
		return PublisherType.DROPBOX;
	}
}
