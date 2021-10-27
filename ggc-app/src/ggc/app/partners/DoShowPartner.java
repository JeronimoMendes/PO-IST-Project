package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;

import ggc.exceptions.UnknownPartnerException;
import ggc.app.exceptions.UnknownPartnerKeyException;

/**
 * Show partner.
 */
class DoShowPartner extends Command<WarehouseManager> {

	DoShowPartner(WarehouseManager receiver) {
		super(Label.SHOW_PARTNER, receiver);
		addStringField("partnerKey", Prompt.partnerKey());
	}

	@Override
	public void execute() throws CommandException {
		try {
			String partner = _receiver.getPartner(stringField("partnerKey"));

			_display.popup(partner);
		} catch (UnknownPartnerException e) {
			throw new UnknownPartnerKeyException(e.getKey());
		}
	}

}
