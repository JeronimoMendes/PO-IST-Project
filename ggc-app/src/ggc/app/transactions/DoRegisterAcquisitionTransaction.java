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
		String product = stringField("product");
		String partner = stringField("partner");
		double price = realField("price");
		int amount = integerField("amount");

		try {
			_receiver.registerAcquisition(product, partner, price, amount);
		} catch (UnknownProductException e) {
			
			if (Form.confirm(Prompt.addRecipe())) {
				double alpha = Form.requestReal(Prompt.alpha());
				int numberOfComponents = Form.requestInteger(Prompt.numberOfComponents());
				String recipe = "";

				for (int i = 0; i < numberOfComponents; i++) {
					String ingredient = Form.requestString(Prompt.productKey());
					int iAmount = Form.requestInteger(Prompt.amount());
					recipe += String.format("%s:%d", ingredient, iAmount);
					if (i == numberOfComponents - 1) recipe += "#";
				}

				_receiver.registerProductInAcquisition(product, partner, price, amount, recipe, alpha);
			} else {
				_receiver.registerProductInAcquisition(product, partner, price, amount);
			}

		} catch (UnknownPartnerException e) {
			throw new UnknownPartnerKeyException(e.getKey());
		}
	}

}
