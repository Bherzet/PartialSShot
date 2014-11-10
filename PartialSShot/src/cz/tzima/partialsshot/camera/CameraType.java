package cz.tzima.partialsshot.camera;

/**
 * Contains all available cameras ({@link Camera}).
 * 
 * @author Tomas Zima
 */
public enum CameraType {
	/**
	 * For {@link RectangularCamera}.
	 */
	RECTANGULAR_CAMERA (RectangularCamera.class);
	
	/** The implementing class. */
	private Class<? extends Camera> cameraType;

	/**
	 * Stores the given value.
	 * 
	 * @param cameraType
	 *     The implementing class (must be a child of {@link Camera}).
	 */
	private CameraType(Class<? extends Camera> cameraType) {
		this.cameraType = cameraType;
	}
	
	/**
	 * Creates a new instance of the {@link Camera}. It uses the non-parametric
	 * constructor and calls no methods.
	 * 
	 * @return
	 *     New instance of a camera.
	 * @throws CameraInitializationException
	 *     Camera couldn't be initialized. This could be caused either by a
	 *     missing non-parametric constructor or by a non-accessible (non-public)
	 *     constructor.
	 */
	public Camera newInstance() throws CameraInitializationException {
		try {
			return cameraType.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new CameraInitializationException(
				"Requested camera (" + cameraType + ") couldn't be initialized.",
				e
			);
		}
	}
}
