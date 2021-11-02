package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;

import ggc.exceptions.UnknownPartnerException;
import ggc.exceptions.UnknownProductException;
import ggc.app.exceptions.UnknownPartnerKeyException;
import ggc.app.exceptions.UnknownProductKeyException;


/**
 * Toggle product-related notifications.
 */
class DoToggleProductNotifications extends Command<WarehouseManager> {

	DoToggleProductNotifications(WarehouseManager receiver) {
		super(Label.TOGGLE_PRODUCT_NOTIFICATIONS, receiver);
		addStringField("partner", Prompt.partnerKey());
		addStringField("product", Prompt.productKey());
	}

	@Override
	public void execute() throws CommandException {
		try {
			_receiver.toggleNotifications(stringField("partner"), stringField("product"));
		} catch (UnknownProductException e) {
			throw new UnknownProductKeyException(e.getKey());
		} catch (UnknownPartnerException e) {
			throw new UnknownPartnerKeyException(e.getKey());
		}
	}

}
