package ggc.exceptions;

/**
 * Exception thrown when a given partner's key doesn't corresponde to a 
 * registered partner
 */
public class UnknownPartnerException extends Exception {

	/** The requested key. */
	String _key;

	/**
	 * @param key 
	 */
	public UnknownPartnerException(String key) {
	  _key = key;
	}

	/**
	 * @return the requested key
	 */
	public String getKey() {
		return _key;
	}

}
