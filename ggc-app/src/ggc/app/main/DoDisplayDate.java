package ggc.app.main;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;

/**
 * Show current date.
 */
class DoDisplayDate extends Command<WarehouseManager> {
	private int date = 0;

	DoDisplayDate(WarehouseManager receiver) {
		super(Label.SHOW_DATE, receiver);
	}

	@Override
	public final void execute() throws CommandException {
		// Displays date on Display
		_display.popup(Message.currentDate(_receiver.getDate()));
	}

}
