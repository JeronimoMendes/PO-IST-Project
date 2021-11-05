package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;

import ggc.exceptions.UnknownPartnerException;
import ggc.exceptions.UnknownProductException;
import ggc.app.exceptions.UnknownPartnerKeyException;


/**
 * Register order.
 */
public class DoRegisterAcquisitionTransaction extends Command<WarehouseManager> {

	public DoRegisterAcquisitionTransaction(WarehouseManager receiver) {
		super(Label.REGISTER_ACQUISITION_TRANSACTION, receiver);
		addStringField("partner", Prompt.partnerKey());
		addStringField("product", Prompt.productKey());
		addRealField("price", Prompt.price());
		addIntegerField("amount", Prompt.amount());
	}

	@Override
	public final void execute() throws CommandException {
		try {
			_receiver.registerAccquisition(stringField("product"), stringField("partner"),
											realField("price"), integerField("amount"));
		} catch (UnknownProductException e) {

		} catch (UnknownPartnerException e) {
			throw new UnknownPartnerKeyException(e.getKey());
		}
	}

}
