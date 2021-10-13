package ggc;

import ggc.exceptions.*;
import java.io.IOException;
import java.io.Serializable;

// FIXME import classes (cannot import from pt.tecnico or ggc.app)

/**
 * Class Warehouse implements a warehouse.
 */
public class Warehouse implements Serializable {

	/** Serial number for serialization. */
	private static final long serialVersionUID = 202109192006L;

	// FIXME define attributes
	private int _date = 0;
	// FIXME define contructor(s)
	// FIXME define methods

	/**
	 * @param txtfile filename to be loaded.
	 * @throws IOException
	 * @throws BadEntryException
	 */
	void importFile(String txtfile) throws IOException, BadEntryException /* FIXME maybe other exceptions */ {
		//FIXME implement method
	}


	/**
	 *  @return warehouse's date
	 */
	public int getDate() {
		return _date;
	}

	/**
	 * @param days days to advance date
	 * 
	 * @throws InvalidDateException if negative or null days
	 */
	public void advanceDate(int days) throws InvalidDaysException {
		if (days > 0) {
			_date += days;
		} else {
			throw new InvalidDaysException(days);
		}
	}
}
