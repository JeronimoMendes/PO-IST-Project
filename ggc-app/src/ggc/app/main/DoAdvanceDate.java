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
	
	private int _days;

	DoAdvanceDate(WarehouseManager receiver) {
		super(Label.ADVANCE_DATE, receiver);
		//FIXME add command fields
		addIntegerField("date", Message.advanceDate());
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
