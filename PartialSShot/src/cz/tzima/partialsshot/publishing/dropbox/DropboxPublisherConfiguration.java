package cz.tzima.partialsshot.publishing.dropbox;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Configuration for the {@link DropboxContentPublisher}.
 * 
 * @author Tomas Zima
 */
public final class DropboxPublisherConfiguration {
	/** Path to the local Dropbox's public directory. */
	@JsonProperty(value="publicDirectoryRoot", required=true)
	private String publicDirectoryRoot;
	
	/** Directory (inside the Dropbox's public directory) for. */
	@JsonProperty(value="relativeTargetDirPath", required=true)
	private String relativeTargetDirPath;
	
	/** Dropbox's user ID (present in the public URLs). */
	@JsonProperty(value="userID", required=true)
	private String userID;
	
	/** Link to the root of Dropbox's public directory. */
	@JsonProperty(value="publicDirLink", required=true)
	private String publicDirLink;
	
	/** Prevent standard instantiation purposely. */
	private DropboxPublisherConfiguration() {
		// empty purposely
	}
	
	/** @return {@link #publicDirectoryRoot} */
	public String getPublicDirectoryRoot() {
		return publicDirectoryRoot;
	}
	
	/** @return {@link #relativeTargetDirPath} */
	public String getRelativeTargetDirPath() {
		return relativeTargetDirPath;
	}
	
	/** @return {@link #userID} */
	public String getUserID() {
		return userID;
	}
	
	/** @return {@link #publicDirLink} */
	public String getPublicDirLink() {
		return publicDirLink;
	}
}
