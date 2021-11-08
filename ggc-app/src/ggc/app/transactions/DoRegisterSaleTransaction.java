package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;

import ggc.exceptions.UnknownPartnerException;
import ggc.app.exceptions.UnknownPartnerKeyException;
import ggc.exceptions.UnknownProductException;
import ggc.app.exceptions.UnknownProductKeyException;


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
		try {
			_receiver.registerSale(stringField("partner"), stringField("product"),
									integerField("date"), integerField("amount"));
		} catch (UnknownPartnerException e) {
			throw new UnknownPartnerKeyException(e.getKey());
		} catch (UnknownProductException e) {
			throw new UnknownProductKeyException(e.getKey());
		}
	}

}
