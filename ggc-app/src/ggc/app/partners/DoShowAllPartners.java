package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;

/**
 * Show all partners.
 */
class DoShowAllPartners extends Command<WarehouseManager> {

	DoShowAllPartners(WarehouseManager receiver) {
		super(Label.SHOW_ALL_PARTNERS, receiver);
	}

	@Override
	public void execute() throws CommandException {
		String partners = _receiver.listPartners();

		_display.popup(partners);
	}

}
