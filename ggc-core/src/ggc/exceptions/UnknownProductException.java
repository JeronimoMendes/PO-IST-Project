package ggc.exceptions;

/**
 * Exception thrown when a given product's key doesn't corresponde to a 
 * registered product
 */
public class UnknownProductException extends Exception {
	private static final long serialVersionUID = 202110272100L;

	/** The requested key. */
	private String _key;

	/**
	 * @param key 
	 */
	public UnknownProductException(String key) {
	  _key = key;
	}

	/**
	 * @return the requested key
	 */
	public String getKey() {
		return _key;
	}
}
