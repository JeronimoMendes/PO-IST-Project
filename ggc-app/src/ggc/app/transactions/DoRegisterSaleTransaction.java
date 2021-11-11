package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;

import ggc.exceptions.UnknownPartnerException;
import ggc.app.exceptions.UnknownPartnerKeyException;
import ggc.exceptions.UnknownProductException;
import ggc.app.exceptions.UnknownProductKeyException;
import ggc.exceptions.NoStockException;
import ggc.app.exceptions.UnavailableProductException;


public class DoRegisterSaleTransaction extends Command<WarehouseManager> {

	public DoRegisterSaleTransaction(WarehouseManager receiver) {
		super(Label.REGISTER_SALE_TRANSACTION, receiver);
		addStringField("partner", Prompt.partnerKey());
		addIntegerField("date", Prompt.paymentDeadline());
		addStringField("product", Prompt.productKey());
		addIntegerField("amount", Prompt.amount()); 
	}

	@Override
	public final void execute() throws CommandException {
		String partner = stringField("partner");
		String product = stringField("product");
		int date = integerField("date");
		int amount = integerField("amount");

		try {
			_receiver.registerSale(partner, product, date, amount);
		} catch (UnknownPartnerException e) {
			throw new UnknownPartnerKeyException(e.getKey());
		} catch (UnknownProductException e) {
			throw new UnknownProductKeyException(e.getKey());
		} catch (NoStockException e) {
			throw new UnavailableProductException(e.getProductID(), e.getAskedStock(), e.getCurrentStock());
		}
	}

}
