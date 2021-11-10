package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.exceptions.UnknownPartnerException;
import ggc.app.exceptions.UnknownPartnerKeyException;

/**
 * Show all transactions for a specific partner.
 */
class DoShowPartnerSales extends Command<WarehouseManager> {

	DoShowPartnerSales(WarehouseManager receiver) {
		super(Label.SHOW_PARTNER_SALES, receiver);
		addStringField("partner", Prompt.partnerKey());
	}

	@Override
	public void execute() throws CommandException {
		try {
			_display.popup(_receiver.showPartnerSalesAndBreakdowns(stringField("partner")));
		} catch (UnknownPartnerException e) {
			throw new UnknownPartnerKeyException(e.getKey());
		}
	}
}
