package ggc.exceptions;

/**
 * Exception for invalid days when trying to change date.
 * Thrown when date =< 0
 */
public class InvalidDaysException extends Exception {
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