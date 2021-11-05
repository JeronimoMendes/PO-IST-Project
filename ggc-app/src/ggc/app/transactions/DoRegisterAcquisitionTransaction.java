package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import pt.tecnico.uilib.forms.Form;

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
		String product;
		String partner;
		double price;
		int amount;

		try {
			product = stringField("product");
			partner = stringField("partner");
			price = realField("price");
			amount = integerField("amount");

			_receiver.registerAcquisition(product, partner, price, amount);
		} catch (UnknownProductException e) {
			/*
			if (Form.confirm(Prompt.addRecipe())) {
				String recipe = Form.requestString(Prompt.numberOfComponents());
				double alpha = Form.requestReal(Prompt.alpha());
				Strign id = Form.requestString(Prompt.productKey());
				int amount = Form.requestInteger(Prompt.amount());
				_receiver.registerAcquisition(id, partner, price, amount)
			}
			_receiver.registerAcquisition(product, partner, price, amount)
		*/
		} catch (UnknownPartnerException e) {
			throw new UnknownPartnerKeyException(e.getKey());
		}
	}

}
