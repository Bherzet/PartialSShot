package cz.tzima.partialsshot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cz.tzima.partialsshot.camera.Camera;
import cz.tzima.partialsshot.camera.CameraType;
import cz.tzima.partialsshot.publishing.PublisherType;
import cz.tzima.partialsshot.publishing.dropbox.DropboxContentPublisher;
import cz.tzima.partialsshot.publishing.dropbox.DropboxPublisherConfiguration;
import cz.tzima.partialsshot.publishing.local.LocalContentPublisher;
import cz.tzima.partialsshot.publishing.local.LocalPublisherConfiguration;

/**
 * Configuration for the whole application and all its parts.
 * 
 * @author Tomas Zima
 */
public final class Configuration {
	/** All required content publishers (where screenshots would be stored). */
	@JsonProperty(value="publishers", required=true)
	private PublisherType[] publishers;
	
	/** Requested camera ({@link Camera}). */
	@JsonProperty(value="camera", required=false)
	private CameraType cameraType = CameraType.RECTANGULAR_CAMERA;
	
	/** Generate & copy a link to the clipboard? From which publisher? */
	@JsonProperty(value="generateLink", required=false)
	private PublisherType generateLink = PublisherType.NONE;
	
	/** Ask for a name of the screenshot or generate it automatically? */
	@JsonProperty(value="askForName", required=false)
	private boolean askForName = false;
	
	/** Configuration for the {@link LocalContentPublisher}. */
	@JsonProperty(value="publisher_Local", required=true)
	private LocalPublisherConfiguration localPubConfiguration;
	
	/** Configuration for the {@link DropboxContentPublisher}. */
	@JsonProperty(value="publisher_Dropbox", required=true)
	private DropboxPublisherConfiguration dropboxPubConfiguration;
	
	/**
	 * Loads configuration from the specified path. It adds a support for the
	 * multiline comments (enclosed by '/*' and '*\/', which are not supported
	 * by JSON. It should be, however, supported by the Jackson, but for some
	 * reason it doesn't work. The solution used will break if there is a string
	 * containg any sequence like "/*...*\/". Be careful wit that.  
	 * 
	 * @param path
	 *     Path to the file.
	 * @return
	 *     A loaded configuration.
	 * @throws JsonParseException
	 *     Invalid JSON syntax.
	 * @throws JsonMappingException
	 *     Deserialization error.
	 * @throws IOException
	 *     I/O error while accessing the file.
	 */
	public static Configuration loadFrom(String path) throws JsonParseException, JsonMappingException, IOException {
		byte[] content = Files.readAllBytes(new File(path).toPath());
		String jsonStr = new String(content);

		// Quick & dirty fix: remove comments from the input JSON. This should be handled by Jackson, but for some
		// reason, it doesn't work. THIS IS NOT A PROPER WAY TO DO IT! It won't work for strings like "/*a*/" etc.,
		// but since such strings are unlikely to be used, I'm going with this.
		for (int i; (i = jsonStr.indexOf("/*")) >= 0; ) {
			jsonStr = jsonStr.substring(0, i) + jsonStr.substring(jsonStr.indexOf("*/") + 2);
		}

		ObjectMapper objectMapper = new ObjectMapper();
		//objectMapper.enable(JsonParser.Feature.ALLOW_COMMENTS);
		return objectMapper.readValue(jsonStr, Configuration.class);
	}
	
	/** @return {@link #publishers} */
	public PublisherType[] getPublishers() {
		return publishers;
	}
	
	/** @return {@link #cameraType} */
	public CameraType getCameraType() {
		return cameraType;
	}
	
	/** @return {@link #generateLink} */
	public PublisherType getGenerateLink() {
		return generateLink;
	}
	
	/** @return {@link #askForName} */
	public boolean isAskForName() {
		return askForName;
	}
	
	/** @return {@link #localPubConfiguration} */
	public LocalPublisherConfiguration getLocalPubConfiguration() {
		return localPubConfiguration;
	}
	
	/** @return {@link #dropboxPubConfiguration} */
	public DropboxPublisherConfiguration getDropboxPubConfiguration() {
		return dropboxPubConfiguration;
	}
}
