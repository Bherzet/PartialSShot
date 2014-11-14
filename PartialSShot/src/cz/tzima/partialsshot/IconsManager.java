package cz.tzima.partialsshot;

import java.awt.Image;
import java.awt.Toolkit;

import cz.tzima.partialsshot.controller.messages.StateMessage;

public class IconsManager {
	/**
	 * Path to all the icons. There must be at least three files (but other ones
	 * would be ignored). First icon is the default state, second icon is when
	 * application is activated, third icon is when the first point is selected
	 * and fourth icon signalizes running upload.
	 */
	private static final String[] iconNames = {"res/logo.png", "res/logo-0.png", "res/logo-1.png", "res/logo-U.png"};
	
	/**
	 * All icons. The order is the same as for names ({@link #iconNames}).
	 */
	private static final Image icons[] = new Image[iconNames.length];
	
	/**
	 * Loads all icons.  
	 * 
	 * @param runFromJAR
	 *     True if application runs from the JAR file. This affects from where
	 *     are the resources loaded.
	 */
	public void load(boolean runFromJAR) {
		Toolkit toolkit  = Toolkit.getDefaultToolkit();

		for (int i = 0; i < iconNames.length; i++) {
			icons[i] = (runFromJAR) ?
				toolkit.getImage(getClass().getResource("/" + iconNames[i])) :
				toolkit.getImage(iconNames[i]);
		}
	}
	
	/**
	 * This should be called each time the application's state is changed.
	 * 
	 * @param message
	 *     Message describing the current state.
	 * @return
	 *     Appropriate icon for the given state.
	 */
	public Image getIconFor(StateMessage message) {
		switch (message.getStateType()) {
			// default state (application just runs on the background)
			case DEFAULT:
				return icons[0];
				
			// activated (application has been activated and waits for specific
			// commands)
			case ACTIVATED:
				return icons[1];

			// upload is running (application should ignore new screenshot
			// requests)
			case UPLOADING:
				return icons[3];
				
			// for the future updates, which may add plugins supporting more
			// selection of more than two points, it'd be good to add more icons
			// and this code replace with something like:
			//     return icons[1 + ((StateMessageNthCornerSelected) message).getNumber()]
			case Nth_CORNER_SELECTED:
			default:
				return icons[2];
		}
	}
	
	/**
	 * Default icon which should be shown when application runs on the
	 * background and waits for commands.
	 * 
	 * @return
	 *     Default icon.
	 */
	public Image getDefaultIcon() {
		return icons[0];
	}
}
