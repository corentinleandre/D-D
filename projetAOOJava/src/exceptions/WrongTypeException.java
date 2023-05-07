package exceptions;

public class WrongTypeException extends FileSyncException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4287603266818310464L;

	public WrongTypeException(String message) {
		super(message);
	}

}
