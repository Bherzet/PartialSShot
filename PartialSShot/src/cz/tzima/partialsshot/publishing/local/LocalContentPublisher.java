package cz.tzima.partialsshot.publishing.local;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import cz.tzima.partialsshot.Configuration;
import cz.tzima.partialsshot.publishing.ContentPublisher;
import cz.tzima.partialsshot.publishing.ContentPublishingException;
import cz.tzima.partialsshot.publishing.PublisherType;

/**
 * Publishes into the local filesystem's directory.
 * 
 * @author Tomas Zima
 */
public class LocalContentPublisher implements ContentPublisher {
	/** Directory for publishing screenshots. */
	protected File targetDirectory;
	/** Last screenshot's filename. */
	protected String lastName;
	/** Logger. */
	private static final Logger LOGGER = Logger.getLogger(LocalContentPublisher.class.getName()); 
	
	/**
	 * @param configuration
	 *     Configuration loaded from the configuration file.
	 */
	public LocalContentPublisher(Configuration configuration) {
		this.targetDirectory = new File(configuration.getLocalPubConfiguration().getDirectory());
	}
	
	/**
	 * Available for the childs of this class.
	 */
	protected LocalContentPublisher() {
		// empty purposely
	}
	
	/**
	 * Publishes screenshot to the local directory. Name of the file will be
	 * taken from the parameter.
	 * 
	 * @param image
	 *     Screenshot.
	 * @param name
	 *     Name of the file.
	 * @throws ContentPublishingException
	 *     Probably some sort of I/O error. 
	 */
	@Override
	public void publish(BufferedImage image, String name) throws ContentPublishingException {
		try {
			// directory must exist
			if (!targetDirectory.isDirectory()) {
				throw new ContentPublishingException(
					"The directory you specified (" + targetDirectory +
					") doesn't exists. Screenshot couldn't be saved."
				);
			}

			// add extension
			name += ".png";
			
			// file
			File targetFile = new File(targetDirectory, name);
			LOGGER.log(Level.INFO, "Publishing a screenshot to the " + targetFile);
			ImageIO.write(image, "png", targetFile);
			
			// store for a potential future use
			this.lastName = name;
		} catch (IOException e) {
			throw new ContentPublishingException("Screenshot couldn't be saved.", e);
		}
	}

	/**
	 * Publishes screenshot to the local directory. Name of the file will be
	 * generated.
	 * 
	 * @param image
	 *     Screenshot.
	 * @throws ContentPublishingException
	 *     Probably some sort of I/O error. 
	 */
	@Override
	public void publish(BufferedImage image) throws ContentPublishingException {
		String name = generateName(targetDirectory);
		publish(image, name);
		
		// store for a potential future use
		this.lastName = name;
	}
	
	/**
	 * Link to the local image cannot be created.
	 * 
	 * @return
	 *     Always `null`.
	 */
	@Override
	public String getTargetURL() {
		return null;
	}

	/**
	 * Local images are not linkable.
	 * 
	 * @return
	 *     Always `false`.
	 */
	@Override
	public boolean isLinkable() {
		return false;
	}

	/**
	 * @return
	 *     Always {@link PublisherType#LOCAL}.
	 */
	@Override
	public PublisherType getType() {
		return PublisherType.LOCAL;
	}
	
	/**
	 * Generates name of the file using the current date and time. In the case
	 * such file already exists, name will be modified until a free filename is
	 * found.
	 * 
	 * @param targetDirectory
	 *     Local directory to which screenshots are stored.
	 * @return
	 *     A name of the file.
	 */
	protected static String generateName(File targetDirectory) {
		// generate a name candidate 
		String name       = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		File   targetFile = new File(targetDirectory, name + ".png");
		
		// modify name until a free one is found
		for (int i = 1; targetFile.exists(); i++) {
			targetFile = new File(targetDirectory, name + "-" + i + ".png");
		}
		
		// return the name
		name = targetFile.getName();
		return name.substring(0, name.length() - ".png".length());
	}

}
