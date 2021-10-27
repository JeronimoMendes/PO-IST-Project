package ggc.exceptions;

/**
 * Exception for a registration of a already registered Partner
 */
public class DuplicatePartnerException extends Exception {
	private static final long serialVersionUID = 202110272100L;
	
	private String _key;

	/**
	 * 
	 * @param key Duplicate Partner's key
	 */
	public DuplicatePartnerException(String key) {
		_key = key; 
	}

	/**
	 * @return key String 
	 */
	public String getKey() {
		return _key;
	}
}
