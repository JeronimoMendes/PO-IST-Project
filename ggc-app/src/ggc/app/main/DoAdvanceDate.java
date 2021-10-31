package ggc.app.main;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.app.exceptions.InvalidDateException;
import ggc.exceptions.InvalidDaysException;


/**
 * Advance current date.
 */
class DoAdvanceDate extends Command<WarehouseManager> {
	
	DoAdvanceDate(WarehouseManager receiver) {
		super(Label.ADVANCE_DATE, receiver);
		addIntegerField("date", Prompt.daysToAdvance());
	}

	@Override
	public final void execute() throws CommandException {
		try {
			// Execute command on WarehouseManager
			_receiver.advanceDate(integerField("date"));
		} catch (InvalidDaysException e) {
			throw new InvalidDateException(e.getDays());
		}
	}

}
