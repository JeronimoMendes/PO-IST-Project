package ggc.app.lookups;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;

import ggc.exceptions.UnknownPartnerException;
import ggc.app.exceptions.UnknownPartnerKeyException;

/**
 * Lookup payments by given partner.
 */
public class DoLookupPaymentsByPartner extends Command<WarehouseManager> {

	public DoLookupPaymentsByPartner(WarehouseManager receiver) {
		super(Label.PAID_BY_PARTNER, receiver);
		addStringField("partner", Prompt.partnerKey());
	}

	@Override
	public void execute() throws CommandException {
		try {
			String info = _receiver.lookupPaymentsByPartner(stringField("partner"));
			_display.popup(info);
		} catch (UnknownPartnerException e) {
			throw new UnknownPartnerKeyException(e.getKey());
		}
	}

}
