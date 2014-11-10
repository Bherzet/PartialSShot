package cz.tzima.partialsshot.camera;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Instances of {@link Camera} are used for taking screenshots of the screen.
 * Each implementation might generate a different screenshot (e.g. rectangle,
 * ellipse etc).
 * 
 * @author Tomas Zima
 */
public interface Camera {
	/**
	 * Takes a screenshot of the part of the screen. The points, which are given
	 * as parameters, are entered by the user and might be used however the
	 * implementation wants. It could represent for example a diagonal line of a
	 * rectangle.
	 * 
	 * @param points
	 *     Points which specifies the shape of the screenshot.
	 * @return
	 *     Screenshot of the part of the screen.
	 * @throws CameraActionException
	 *     Screenshot couldn't be made due to some error caused when taking the
	 *     screenshot.
	 * @throws InvalidAreaSelectException
	 *     Screenshot couldn't be made due to invalid selection of points.
	 */
	public BufferedImage takeScreenshot(List<Point> points) throws CameraActionException, InvalidAreaSelectException;
	
	/**
	 * Each implementation may require a different count of points for
	 * specifying the part of the screen to take screenshot of.
	 * 
	 * @return
	 *     Count of the required points.
	 */
	public int getRequiredPointsCount();
}
