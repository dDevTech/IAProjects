package exceptions;

public class DimensionalityMismatch extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7471850810026257042L;
	
	public DimensionalityMismatch(String message) {
		super(message);
	}

}
