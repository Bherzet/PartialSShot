package cz.tzima.partialsshot.camera;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * <p>Camera which takes screenshot of the rectangular part of the screen. This
 * rectangle is specified by two points, which lies on the same diagonal. Some
 * examples of some possible coordinates (order is important!) and expected
 * results follows. Rectangle is specified by the coordinates of the left top
 * corner, width and height.
 * 
 * <ul>
 *   <li>[0; 0], [5; 5] ~ rectangle([0; 0], w=5, h=5)</li>
 *   <li>[5; 5], [0; 0] ~ rectangle([0; 0], w=5, h=5)</li>
 *   <li>[5; 0], [0; 5] ~ rectangle([0; 0], w=5, h=5)</li>
 *   <li>[0; 5], [5; 0] ~ rectangle([0; 0], w=5, h=5)</li>
 * </ul>
 * 
 * @author Tomas Zima
 */
public class RectangularCamera implements Camera {
	/**
	 * Takes screenshot of the rectangular part of the screen.
	 * 
	 * @param points
	 *     Two points lying on the same diagonal.
	 * @throws CameraActionException
	 *     When the internal technical error occurs.
	 * @throws InvalidAreaSelectException
	 *     When user selected invalid points.
	 */
	@Override
	public BufferedImage takeScreenshot(List<Point> points) throws CameraActionException, InvalidAreaSelectException {
		// is the count of points correct?
		if (points.size() != this.getRequiredPointsCount()) {
			throw new InvalidAreaSelectException(
				"Screenshot couldn't be made because required count of points of hasn't been entered."
			);
		}
		
		try {
			// take a screenshot ("new Robot()" is intended - it's better to create a new instance when screenshot
			// is actually needed, which doesn't happen that often, instead of holding the instance all the time)
			return new Robot().createScreenCapture(getRectangle(points.get(0), points.get(1)));
		} catch (AWTException e) {
			throw new CameraActionException("Screenshot couldn't be took because of an internal error.", e);
		}
	}
	
	/**
	 * Returns rectangle specified by two points, which are located on the same diagonal
	 * (doesn't matter which one of the two). It correctly detects the way of the diagonal,
	 * therefore allowing to choose the two points in any order.
	 * 
	 * @param firstCorner
	 *     First ending point lying on the diagonal.
	 * @param secondCorner
	 *     Second enging point lying on the same diagonal.
	 * @return
	 *     Rectangle with the specified diagonal.
	 * @throws InvalidAreaSelectException
	 *     The points doesn't lie on the same diagonal. 
	 */
	protected static Rectangle getRectangle(Point firstCorner, Point secondCorner) throws InvalidAreaSelectException {
		/*
		 * Terminology:
		 *   main diagonal    secondary diagonal 
		 *     [\  ]            [  /]
		 *     [ \ ]            [ / ]
		 *     [  \]            [/  ]
		 */
		// for the result
		Rectangle rectangle = new Rectangle();
		
		// calculate the vector of the diagonal
		int       dx        = secondCorner.x - firstCorner.x;
		int       dy        = secondCorner.y - firstCorner.y;
		
		// two different points are required
		if (dx == 0 || dy == 0) {
			throw new InvalidAreaSelectException(
				"Two different points are required for taking a screenshot. You entered points " +
				firstCorner.toString() + " and " + secondCorner.toString() + "."
			);
		}
		
		// if both vector's coordinates are positive or negative, it lies on the main diagonal.
		// If one of the coordinates is negative, while the second is positive, it is the secondary
		// diagonal. We need to get main diagonal.
		if ((dx < 0 && dy > 0) || (dx > 0 && dy < 0)) {
			// swap x coordinates of the corners
			int tmp        = firstCorner.x;
			firstCorner.x  = secondCorner.x;
			secondCorner.x = tmp;
			
			// recalculate the diagonal vector
			dx             = secondCorner.x - firstCorner.x;
			dy             = secondCorner.y - firstCorner.y;
		}
		
		// diagonal is oriented from the firstCorner to the secondCorner
		if (dx > 0 && dy > 0) {
			rectangle.x = firstCorner.x;
			rectangle.y = firstCorner.y;
		// diagonal is oriented from the secondCorner to the firstCorner
		} else if (dx < 0 && dy < 0) {
			rectangle.x = secondCorner.x;
			rectangle.y = secondCorner.y;
		}
		
		// size of rectangle (difference between x and y coordinates)
		rectangle.width  = Math.abs(dx);
		rectangle.height = Math.abs(dy);
		
		// result
		return rectangle;
	}

	/**
	 * This implementation needs two points.
	 * 
	 * @return 2
	 */
	@Override
	public int getRequiredPointsCount() {
		return 2;
	}
}
