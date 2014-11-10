package cz.tzima.partialsshot;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import javax.swing.JOptionPane;

/**
 * Displays logged informations to the user using a GUI. It'd be wise to only
 * use this for {@link Level#WARNING} and higher. 
 * 
 * @author Tomas Zima
 */
public class GUIErrorLogger extends Handler {
	/** {@inheritDoc} */
	@Override
	public void publish(LogRecord record) {
		if (this.getLevel().intValue() <= record.getLevel().intValue()) {
			JOptionPane.showMessageDialog(
				null, splitLines(record.getMessage(), 80), "Error", getGUIMessageLevel(record.getLevel())
			);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void flush() {
		// empty purposely
	}

	/** {@inheritDoc} */
	@Override
	public void close() throws SecurityException {
		// empty purposely
	}
	
	/**
	 * @param level
	 *     Level of the log's record.
	 * @return
	 *     Value from {@link JOptionPane} appropriate to the given level.
	 */
	private int getGUIMessageLevel(Level level) {
		if (level == Level.SEVERE)  return JOptionPane.ERROR_MESSAGE;
		if (level == Level.WARNING) return JOptionPane.WARNING_MESSAGE;

		return JOptionPane.INFORMATION_MESSAGE;
	}
	
	/**
	 * Splits the original string into multiple lines so that any of the lines
	 * isn't longer than expected.
	 * 
	 * @param message
	 *     Original message (without linebreaks, or with linebreaks used for
	 *     formatting purposes).
	 * @param lineWidth
	 *     Maximal possible line width (in characters).
	 * @return
	 *     String formatted into multiple lines (honoring the maximal length).
	 */
	private static String splitLines(String message, int lineWidth) {
		String result = "";
		for (int i = 0, j = 0; i < message.length(); i++, j++) {
			result += message.charAt(i);
			
			if (j >= lineWidth) {
				j = 0;
				result += "\n";
			}
		}
		
		return result;
	}
}
