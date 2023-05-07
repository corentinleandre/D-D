package exceptions;

public abstract class FileSyncException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -939697435332978897L;

	public FileSyncException(String message) {
		super(message);
	}
	
}
