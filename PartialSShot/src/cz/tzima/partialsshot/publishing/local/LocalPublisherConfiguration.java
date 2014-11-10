package cz.tzima.partialsshot.publishing.local;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Configuration related to the {@link LocalContentPublisher}.
 * 
 * @author Tomas Zima
 */
public final class LocalPublisherConfiguration {
	/** Directory to which screenshots will be stored. */
	@JsonProperty(value="directory", required=true)
	private String directory;
	
	/** Standard instantiation is purposely prevented. */
	private LocalPublisherConfiguration() {
		// empty purposely
	}
	
	/** @return {@link #directory} */
	public String getDirectory() {
		return directory;
	}
}
