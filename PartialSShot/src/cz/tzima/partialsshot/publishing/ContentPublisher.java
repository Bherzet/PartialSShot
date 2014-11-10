package cz.tzima.partialsshot.publishing;

import java.awt.image.BufferedImage;

/**
 * Implementations of {@link ContentPublisher} are supposed to publish created
 * screenshots to any place (HDD, Dropbox, image sharing services etc).
 * 
 * @author Tomas Zima
 */
public interface ContentPublisher {
	/**
	 * Publish the screenshot with the specified name.
	 * 
	 * @param image
	 *     Screenshot.
	 * @param name
	 *     Name (e.g. filename, title, ...)
	 * @throws ContentPublishingException
	 *     Screenshot couldn't be published.
	 */
	public void publish(BufferedImage image, String name) throws ContentPublishingException;
	
	/**
	 * Publish the screenshot.
	 * 
	 * @param image
	 *     Screenshot.
	 * @throws ContentPublishingException
	 *     Screenshot couldn't be published.
	 */
	public void publish(BufferedImage image) throws ContentPublishingException;
	
	/**
	 * Link to the screenshot. If {@link #isLinkable()} returns false then this
	 * method should return null.
	 * 
	 * @return
	 *     Link to the screenshot or null if link is not available.
	 * @throws IllegalStateException
	 *     Any screenshot wasn't published and there isn't link to generate.
	 */
	public String getTargetURL();
	
	/**
	 * Checks if the (published) screenshot is linkable.
	 * 
	 * @return
	 *     True if a direct link to the screenshot can be generated.
	 */
	public boolean isLinkable();

	/**
	 * @return
	 *     Type of the publisher (from {@link PublisherType}).
	 */
	public PublisherType getType();
}
