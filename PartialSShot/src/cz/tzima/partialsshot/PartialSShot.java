package cz.tzima.partialsshot;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JFrame;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import cz.tzima.partialsshot.camera.Camera;
import cz.tzima.partialsshot.camera.CameraActionException;
import cz.tzima.partialsshot.camera.CameraInitializationException;
import cz.tzima.partialsshot.camera.InvalidAreaSelectException;
import cz.tzima.partialsshot.controller.Controller;
import cz.tzima.partialsshot.controller.OnScreenshotRequestListener;
import cz.tzima.partialsshot.controller.OnStateChangeListener;
import cz.tzima.partialsshot.controller.messages.StateMessage;
import cz.tzima.partialsshot.publishing.ContentPublisher;
import cz.tzima.partialsshot.publishing.ContentPublisherInitializationException;
import cz.tzima.partialsshot.publishing.ContentPublishingException;
import cz.tzima.partialsshot.publishing.PublisherType;

/**
 * <p>PartialSShot is an application for quick taking of screenshots, uploading
 * them and sharing them.
 * 
 * <p>This particular class is responsible for putting the lower layers together.
 * 
 * @author Tomas Zima
 */
@SuppressWarnings("serial")
public class PartialSShot extends JFrame implements OnStateChangeListener, OnScreenshotRequestListener {
	// -- error handling
	/** Logger for {@link PartialSShot}. */
	private static final Logger logger = Logger.getLogger(PartialSShot.class.getName());
	
	// -- gui
	/**
	 * Icon in the tray. This reference might be used for changing the icon
	 * image, menu etc. */
	private TrayIcon trayIcon = null;
	/** Used for determining which icon should be displayed. */
	private final IconsManager iconsManager = new IconsManager();
	
	// -- data
	/** Contains configuration of a whole application and all modules. */
	private Configuration configuration = null;
	/**
	 * Will be true if application is running from a JAR file. In that case
	 * icons and other possible unchangeable resources should be included in
	 * the JAR file.  
	 */
	private final boolean runningFromJAR = isRunningFromJAR();
	/**
	 * Path to the configuration file. It should always be located in the
	 * directory from which application is executed.
	 */
	private static final String configFileName = makePath(getExecutionDirectory(), "partialsshot.json");
	/** Absolute path to the log file. */
	private static final String logFileName = makePath(getExecutionDirectory(), "partialsshot.log");

	// -- logic
	/** Determines which actions to run based on a keyboard and mouse input. */
	private Controller controller = new Controller();
	/** Camera using which screenshots will be made. */
	private Camera camera = null;
	/** All loaded content publishers. Screenshots will be published using each one. */
	private ContentPublisher[] contentPublishers;
	
	/**
	 * Initializes and executes the application.
	 */
	private void run() {
		try {
			logger.log(Level.INFO, "Path to a configuration file: " + configFileName);
			
			// prepare necessary data. Running the application without configuration is pointless
			// (defining default behaviour doesn't make a sense).
			this.configuration     = Configuration.loadFrom(configFileName);
			this.contentPublishers = loadContentPublishers();
			
			// load all icons. There're very small, holdin them in memory doesn't cost anything.
			this.iconsManager.load(runningFromJAR);

			// there must be at least one content publisher. Running the application
			// without it doesn't make any sense.
			if (contentPublishers.length == 0) {
				logger.log(
					Level.SEVERE,
					"Any content publisher hasn't been loaded. Application is going to quit."
				);
				exit();
			}
			
			// if someone pasts a logfile, it'd be nice to see which content publishers he used
			logloadedContentPublishers();
			
			// add tray icon and setup menu
			this.initTray();
			
			// registers keyboard & mouse listening
			GlobalScreen.registerNativeHook();
			
			// initialize the camera
			this.camera = configuration.getCameraType().newInstance();
			
			// setup & initialize the controller and run the application
			this.controller.register();
			this.controller.setOnScreenshotRequestListener(this);
			this.controller.setOnStatusChangeListener(this);
			this.controller.setRequestedPointsCount(camera.getRequiredPointsCount());
		} catch (JsonParseException ex) {
			// json syntax error
			logger.log(Level.SEVERE, "Configuration file is invalid and probably contains some syntax errors.", ex);
		} catch (JsonMappingException ex) {
			// deserialization error
			logger.log(
				Level.SEVERE,
				
				"Configuration couldn't be loaded. This is probably caused by including new elements " +
				"in the configuration file. You should delete your configuration file and run application again (it " +
				"will generate a new example of a configuration file instead).",
				
				ex
			);
		} catch (IOException ex) {
			// i/o error while working with configuration file
			logger.log(
				Level.SEVERE,
				
				"Configuration file couldn't be found (under a location \"" + configFileName +
				"\"). A new file will be generated there.",
				
				ex
			);
			
			// generate a new config file if running from a JAR file. Not running from a JAR file should mean
			// that this is a developer version and configuration won't be generated.
			if (runningFromJAR) {
				try {
					copyFromJAR("/res/default_config.json", makePath(getExecutionDirectory(), "partialsshot.json"));
				} catch (IOException e) {
					logger.log(Level.SEVERE, "Application has tried to generate a new configuration file but failed.", e);
				}
			} else {
				logger.log(
					Level.SEVERE,
					
					"Application isn't running from a JAR file and wasn't able to generate a new configuration " + 
					"file. This probably means that you're a developer running the application from IDE. In that " +
					"case, you should have created your own configuration file (located in the data/partialsshot" +
					".json) and build application using Ant script. It'll automatically copy your custom " +
					"configuration file to the bin/ directory." 
				);
			}
		} catch (AWTException ex) {
			// error while initializing the tray
			logger.log(Level.SEVERE, "Application couldn't start (tray icon couldn't be created).", ex);
		} catch (NativeHookException ex) {
			// error while trying to register a keyboard/mouse listeners
			logger.log(Level.SEVERE, "Application couldn't start (shortcut couldn't be registered).", ex);
		} catch (CameraInitializationException ex) {
			// error while trying to instantiate a camera using reflection
			logger.log(Level.SEVERE, "Application couldn't start (camera couldn't be initialized).", ex);
		}
	}

	/**
	 * Logs all loaded content publishers into the logfile.
	 */
	private void logloadedContentPublishers() {
		for (ContentPublisher contentPublisher : contentPublishers) {
			logger.log(Level.INFO, "Content publisher " + contentPublisher.getType() + " has been loaded.");
		}
	}
	
	/**
	 * Extract a file from JAR file to the specified location.
	 * 
	 * @param jarPath
	 *     Path to the file inside a JAR.
	 * @param dirPath
	 *     Path for extraction.
	 * @throws IOException
	 *     I/O error (most probably invalid paths or bad permissions).
	 */
	private void copyFromJAR(String jarPath, String dirPath) throws IOException {
		// open a stream
		URL         resource    = PartialSShot.class.getResource(jarPath);
		InputStream inputStream = resource.openStream();
		byte[]      data        = new byte[inputStream.available()];
		
		// load content
		inputStream.read(data);
		inputStream.close();
		
		// write content
		FileOutputStream outputStream = new FileOutputStream(dirPath);
		outputStream.write(data);
		outputStream.close();
	}

	/**
	 * Initializes the try icon and appends a menu.
	 * 
	 * @throws AWTException
	 *     Fatal internal error.
	 */
	private void initTray() throws AWTException {
		trayIcon = new TrayIcon(iconsManager.getDefaultIcon(), "PartialSShot", new PopupMenu(){{
			this.add(new MenuItem("Exit"){{
				this.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						exit();
					}
				});
			}});
		}});

		SystemTray.getSystemTray().add(trayIcon);
	}
	
	/**
	 * This should be called for killing the application. It flushes all handlers
	 * (without this operation, log file will be probably empty or incomplete). 
	 */
	private static void exit() {
		Handler[] handlers = PartialSShot.logger.getHandlers();
		for (Handler handler : handlers) {
			handler.flush();
			handler.close();
		}
		
		logger.log(Level.FINEST, "Closing the application.");
		System.exit(0);
	}

	/**
	 * Tries to load all requested content publishers ({@link ContentPublisher}).
	 * If some content publisher can't be loaded, it'll be logged and ignored.
	 * Application can run with the other content publishers.
	 * 
	 * @return
	 *     List of all loaded content publishers.
	 */
	private ContentPublisher[] loadContentPublishers() {
		PublisherType[]    requestedPublishers = configuration.getPublishers();
		ContentPublisher[] contentPublishers   = new ContentPublisher[requestedPublishers.length];

		// try to load all content publisher into array . If some content publisher couldn't be
		// loaded, log & ignore it. 
		int i, j;
		for (i = 0, j = 0; i < requestedPublishers.length; i++, j++) {
			try {
				contentPublishers[j] = requestedPublishers[i].getInstance(configuration);
			} catch (ContentPublisherInitializationException e) {
				j--;
				
				logger.log(
					Level.WARNING, 
					"A requested content publisher (" + requestedPublishers[i] + ") couldn't be loaded.",
					e
				);
			}
		}
		
		// cut off unused empty elements (space for content publishers which weren't loaded)
		return Arrays.copyOf(contentPublishers, (j < 0) ? 0 : j);
	}
	
	/**
	 * Takes screenshot using the specified camera ({@link Camera}) and
	 * publishes it using all requested content publishers.
	 * 
	 * @see OnScreenshotRequestListener#takeScreenshot(List)
	 */
	@Override
	public void takeScreenshot(List<Point> points) {
		try {
			// take a screenshot
			BufferedImage screenshot = camera.takeScreenshot(points);
			
			// publish it using all loaded content publishers
			for (ContentPublisher contentPublisher : contentPublishers) {
				try {
					contentPublisher.publish(screenshot);
				} catch (ContentPublishingException exception) {
					logger.log(
						Level.WARNING,
						"Screenshot couldn't be published via " + contentPublisher.getType(),
						exception
					);
				}
			}
			
			// generate link from one content publisher (specified in the configuration)
			copyLinkToClipboard();
		} catch (CameraActionException exception) {
			// this is most likely application's fault
			logger.log(
				Level.WARNING,
				
				"Screenshout couldn't be made due to the unknown error.\n"
				+ "Please consult logs.",
				
				exception
			);
		} catch (InvalidAreaSelectException exception) {
			// this is most likely user's fault
			logger.log(
				Level.WARNING,
				
				"You entered invalid points from which the currently used camera (" +
				configuration.getCameraType() +
				") wasn't able to determine the shape of the screenshot. Please try again.",
				
				exception
			);
		}
	}

	/**
	 * Changes the tray icon.
	 * @see OnStateChangeListener#stateChanged(StateMessage) 
	 */
	@Override
	public void stateChanged(StateMessage message) {
		this.trayIcon.setImage(iconsManager.getIconFor(message));
	}
	
	/**
	 * @return True if application is executed from JAR.
	 */
	private static boolean isRunningFromJAR() {
		// this could actually be null if the class wasn't found but it seems ridiculous. Lets
		// just pretend it won't happen.
		URL path = PartialSShot.class.getResource(PartialSShot.class.getSimpleName() + ".class");
		return path.toString().startsWith("jar:");
	}
	
	/**
	 * Checks the directory from which application is executed. This may be
	 * different than a current working (e.g. if you run the application as
	 * "java -jar bin/app.jar", current working directory wouldn't be "bin").
	 * 
	 * @return Path to the directory from which application is executed.
	 */
	private static String getExecutionDirectory() {
		// if the application is executed from the JAR file, this would be file.
		// Otherwise it should be a directory.
		String executionPath = PartialSShot.class.getProtectionDomain().getCodeSource().getLocation().toExternalForm();
		int    protocol      = executionPath.lastIndexOf(':');
		
		// if there's a protocol, cut it off
		if (protocol != -1) {
			executionPath = executionPath.substring(protocol + 1);
		}
		
		// cut off the filename and return just directory
		File file = new File(executionPath);
		if (file.isFile()) {
			return executionPath.substring(0, executionPath.lastIndexOf(File.separatorChar));
		}
		
		// it's already just a directory, return it
		return executionPath;
	}
	
	/**
	 * Creates a path to the file in a specified directory. This is safer than
	 * just joining these two strings (if directory wouldn't end with a
	 * separator '/').
	 * 
	 * @param dir
	 *     Full path to the directory.
	 * @param file
	 *     Name of the file. 
	 * @return
	 *     Path to the file inside the specified directory.
	 */
	private static String makePath(String dir, String file) {
		return new File(dir, file).toString();
	}
	
	/**
	 * Copies a link of the last screenshot to the clipboard. This will only
	 * happen if these three criteria are met:
	 * <ul>
	 *   <li>configuration contains a `generateLink` item and its value is not
	 *       NONE</li>
	 *   <li>content publisher, from which the link is requested, supports it (
	 *       meaning it's linkable)</li>
	 *   <li>content publisher, from which the link is requested, is actually
	 *       used and loaded</li>
	 * </ul>
	 * If there is more than one content publisher of the given type, it will
	 * only use the first one found.
	 */
	private void copyLinkToClipboard() {
		PublisherType generateLink = this.configuration.getGenerateLink();
		if (generateLink != PublisherType.NONE) {
			for (ContentPublisher contentPublisher : contentPublishers) {
				if (contentPublisher.getType() == generateLink && contentPublisher.isLinkable()) {
					logger.log(Level.FINE, "Copying link {0} (from {1}) to the clipboard...", new Object[]{
						contentPublisher.getTargetURL(), contentPublisher.getType()});
					
					StringSelection stringSelection = new StringSelection(contentPublisher.getTargetURL());
					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, stringSelection);
					return;
				}
			}
		}
	}
	
	/**
	 * Program entry point.
	 * 
	 * @param args
	 *     Not used.
	 */
	public static void main(String[] args) {
		try {
			// disable jNativeHook's logger
			Logger.getLogger(GlobalScreen.class.getPackage().getName()).setLevel(Level.OFF);
	//		System.err.close(); // not so wise but works
			
			// enable logging (logger receives everything, everything will be logged
			// to the file, warnings and higher will be displayed to the user)
			try {
				PartialSShot.logger.setLevel(Level.ALL);
				PartialSShot.logger.addHandler(new GUIErrorLogger(){{
					this.setLevel(Level.WARNING);
				}});
				PartialSShot.logger.addHandler(new FileHandler(PartialSShot.logFileName){{
					this.setFormatter(new SimpleFormatter());
					this.setLevel(Level.ALL);
				}});
			} catch (IOException e) {
				PartialSShot.logger.log(Level.WARNING, "Log file (partialsshot.log) couldn't be created.");
			}
			
			// initialize the application
			PartialSShot application = new PartialSShot();
			application.setVisible(false);
			application.run();
		} catch (Throwable e) {
			// if there's a critical (maybe runtime) error, lets user know about it
			PartialSShot.logger.log(
				Level.SEVERE,
				
				"A critical internal error occured. There should be a log file (partialsshot.log) " +
				"with some extra informations. Application is going to die.",
				
				e
			);
			
			// kill safely
			exit();
		}
	}
}
