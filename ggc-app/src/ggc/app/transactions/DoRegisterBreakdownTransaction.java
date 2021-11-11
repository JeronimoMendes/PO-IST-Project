package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;

import ggc.exceptions.UnknownPartnerException;
import ggc.exceptions.UnknownProductException;
import ggc.exceptions.NoStockException;
import ggc.app.exceptions.UnknownPartnerKeyException;
import ggc.app.exceptions.UnknownProductKeyException;
import ggc.app.exceptions.UnavailableProductException;


/**
 * Register order.
 */
public class DoRegisterBreakdownTransaction extends Command<WarehouseManager> {

	public DoRegisterBreakdownTransaction(WarehouseManager receiver) {
		super(Label.REGISTER_BREAKDOWN_TRANSACTION, receiver);
		addStringField("partner", Prompt.partnerKey());
		addStringField("product", Prompt.productKey());
		addIntegerField("amount", Prompt.amount());
	}

	@Override
	public final void execute() throws CommandException {
		String partner = stringField("partner");
		String product = stringField("product");
		int amount = integerField("amount");

		try {
			_receiver.registerBreakdown(partner, product, amount);
		} catch (UnknownPartnerException e) {
			throw new UnknownPartnerKeyException(e.getKey());
		} catch (UnknownProductException e) {
			throw new UnknownProductKeyException(e.getKey());
		} catch (NoStockException e) {
			throw new UnavailableProductException(product, amount, e.getCurrentStock());
		}
	}

}
