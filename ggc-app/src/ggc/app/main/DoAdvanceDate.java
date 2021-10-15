package ggc.app.main;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
//FIXME import classes
import ggc.app.exceptions.InvalidDateException;
import ggc.exceptions.InvalidDaysException;


/**
 * Advance current date.
 */
class DoAdvanceDate extends Command<WarehouseManager> {
	
	/** Number of days to advance */
	private int _days;

	DoAdvanceDate(WarehouseManager receiver) {
		super(Label.ADVANCE_DATE, receiver);
		addIntegerField("date", Prompt.daysToAdvance());
	}

	@Override
	public final void execute() throws CommandException {
		// Fetch days from Form
		_days = integerField("date");

		try {
			// Execute command on WarehouseManager
			_receiver.advanceDate(_days);
		} catch (InvalidDaysException e) {
			throw new InvalidDateException(e.getDays());
		}
	}

}
