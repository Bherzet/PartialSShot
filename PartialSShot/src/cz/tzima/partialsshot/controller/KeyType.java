package cz.tzima.partialsshot.controller;

import org.jnativehook.keyboard.NativeKeyEvent;

/**
 * <p>Types of some common keys. This enum is present because we need to be able
 * to store states of keys (pressed / not pressed) in some boolean array. The
 * values present in {@link NativeKeyEvent}, which would be used as indexes, are
 * far too big.</p>
 * 
 * @author Tomas Zima
 * @see    #decodeKeyCode(int)
 */
public enum KeyType {
	CTRL_L  (NativeKeyEvent.VC_CONTROL_L),
	CTRL_R  (NativeKeyEvent.VC_CONTROL_R),
	ALT_L   (NativeKeyEvent.VC_ALT_L),
	ALT_R   (NativeKeyEvent.VC_ALT_R),
	SHIFT_L (NativeKeyEvent.VC_SHIFT_L),
	SHIFT_R (NativeKeyEvent.VC_SHIFT_R),
	PRTSCR  (NativeKeyEvent.VC_PRINTSCREEN);
	
	/** Value of the key from {@link NativeKeyEvent}. */
	private int nativeKeyCode;
	
	/** Save value of the key from {@link NativeKeyEvent}. */
	private KeyType(int nativeKeyCode) {
		this.nativeKeyCode = nativeKeyCode;
	}
	
	/**
	 * Returns an integer value of this enum's item, which corresponds to the
	 * value given value from {@link NativeKeyEvent}.
	 * 
	 * @param keyCode
	 *     Value of the key (from {@link NativeKeyEvent}) or -1 when not found.
	 * @return
	 *     Integer value of the corresponding item from this enum.         
	 */
	public static int decodeKeyCode(int keyCode) {
		for (KeyType key : KeyType.values()) {
			if (key.nativeKeyCode == keyCode) {
				return key.ordinal();
			}
		}

		return -1;
	}
}