package cz.tzima.partialsshot.publishing;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import cz.tzima.partialsshot.Configuration;
import cz.tzima.partialsshot.publishing.dropbox.DropboxContentPublisher;
import cz.tzima.partialsshot.publishing.local.LocalContentPublisher;

/**
 * All existing (supported) content publishers ({@link ContentPublisher}).
 * 
 * @author Tomas Zima
 */
public enum PublisherType {
	/** This is just for use in the configuration. Do not use anywhere else! */
	NONE    (null),
	/** Related to the {@link DropboxContentPublisher}. */
	DROPBOX (DropboxContentPublisher.class),
	/** Related to the {@link LocalContentPublisher}. */
	LOCAL   (LocalContentPublisher.class),
	/** Modified {@link DropboxContentPublisher} - for testing purposes only! */
	TESTING (TestingPublisher.class);

	/** Implementation of the {@link ContentPublisher}. */
	private Class<? extends ContentPublisher> contentPublisherType;

	/** Stores the class implementing the {@link ContentPublisher}. */
	private PublisherType(Class<? extends ContentPublisher> contentPublisherType) {
		this.contentPublisherType = contentPublisherType;
	}
	
	/**
	 * Instantiates the content publisher.
	 * 
	 * @param configuration
	 *     Configuration loaded from the file. It's not directly used, but it's
	 *     being passed to the newly created instance.
	 * @return
	 *     A new instance of the {@link ContentPublisher}.
	 * @throws ContentPublisherInitializationException
	 *     A {@link ContentPublisher} couldn't be instantiated.
	 */
	public ContentPublisher getInstance(Configuration configuration) throws ContentPublisherInitializationException {
		try {
			// find a constructor which expects the Configuration as its only parameter
			Constructor<? extends ContentPublisher> constructor = this.contentPublisherType
					.getDeclaredConstructor(new Class<?>[] {Configuration.class});

			// in the case constructor wasn't marked public
			constructor.setAccessible(true);
			
			// try to instantiate and pass the configuration through constructor
			return constructor.newInstance(configuration);
		} catch (NullPointerException | NoSuchMethodException | SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new ContentPublisherInitializationException(
				"Requested content publisher (" + contentPublisherType.getSimpleName() + 
				") couldn't be initialized. This is an internal error.", e
			);
		}
	}
}
