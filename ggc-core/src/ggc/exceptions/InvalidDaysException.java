package ggc.exceptions;

/**
 * Exception for invalid days when trying to change date.
 * Thrown when date =< 0
 */
public class InvalidDaysException extends Exception {
	private static final long serialVersionUID = 202110272100L;

	private int _days;

	/**
	 * @param days invalid days
	 */
	public InvalidDaysException(int days) {
		_days = days;
	}

	/**
	 * @return _days
	 */
	public int getDays() {
		return _days;
	}
}