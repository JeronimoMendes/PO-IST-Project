package ggc.exceptions;

/**
 * Exception thrown when a given Transaction's key doesn't corresponde to a 
 * registered Transaction
 */
public class UnknownTransactionException extends Exception {
	private static final long serialVersionUID = 202110272100L;

	/** The requested key. */
	private int _key;

	/**
	 * @param key 
	 */
	public UnknownTransactionException(int key) {
	  _key = key;
	}

	/**
	 * @return the requested key
	 */
	public int getKey() {
		return _key;
	}
}
