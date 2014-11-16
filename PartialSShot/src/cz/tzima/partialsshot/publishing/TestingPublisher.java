package cz.tzima.partialsshot.publishing;

import java.awt.image.BufferedImage;

import cz.tzima.partialsshot.Configuration;
import cz.tzima.partialsshot.publishing.dropbox.DropboxContentPublisher;
import cz.tzima.partialsshot.publishing.local.LocalContentPublisher;

/**
 * This {@link ContentPublisher} is used for <b>testing purposes only</b>. It is
 * same as the {@link DropboxContentPublisher}, but it contains a delay.
 * 
 * @author Tomas Zima
 */
public class TestingPublisher extends DropboxContentPublisher {
	/** Delay (in ms) after which action will be actually executed. */
	private static final long delay = 3000;
	
	/**
	 * Stores the configuration. This content publisher will use the same
	 * configuration (same section) as Dropbox.
	 * 
	 * @param configuration
	 *     Configuration bean for the application.
	 */
	public TestingPublisher(Configuration configuration) {
		super(configuration);
	}
	
	/**
	 * Same as {@link LocalContentPublisher}, but contains a delay ({@link #delay}).
	 */
	@Override
	@SuppressWarnings("static-access")
	public void publish(BufferedImage image, String name) throws ContentPublishingException {
		try {
			Thread.currentThread().sleep(delay);
		} catch (InterruptedException e) {
		}
		
		super.publish(image, name);
	}
	
	/**
	 * @return {@link PublisherType#TESTING}.
	 */
	@Override
	public PublisherType getType() {
		return PublisherType.TESTING;
	}
}
