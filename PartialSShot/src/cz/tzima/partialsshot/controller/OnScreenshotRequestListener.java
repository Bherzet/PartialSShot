package cz.tzima.partialsshot.controller;

import java.awt.Point;
import java.util.List;

/**
 * Listener through which controller notifies the logical core about a new
 * request to take a new screenshot.
 * 
 * @author Tomas Zima
 */
public interface OnScreenshotRequestListener {
	/**
	 * Method which is triggered each time a new screenshot is requested.
	 * 
	 * @param points Points which specifies the screenshot shape.
	 */
	public void takeScreenshot(List<Point> points);
}
