package ggc.transactions;

import ggc.util.VisitorTransaction;

import ggc.partners.Partner;
import ggc.products.Product;

public class Breakdown extends Transaction {
	private double _vpag;
	private String _recipeWithPrices;

	public Breakdown(int id, Product product, Partner partner, int quantity, double vbase, double vpag, String recipeValues) {
		super(id, product, partner, quantity, vbase);
		_vpag = vpag;
		_recipeWithPrices = recipeValues;
	}

	public double getTotalPrice() {
		return _vpag;
	}

	@Override
	public void accept(VisitorTransaction visitor) {
		visitor.visit(this);
	}

	@Override
	public void pay() {
		setPaymentDate(getStoreDate());
		getPartner().getStatus().pay(this);
		setPaid(true);
	}

	@Override
	public String toString() {
		return String.format("DESAGREGAÇÃO|%s|%s|%s|%d|%d|%d|%d|%s",
							getID(), getPartner().getID(),
							getProduct().getID(), getQuantity(),
							Math.round(getBaseValue()), Math.round((_vpag)),
							getPaymentDate(), _recipeWithPrices);
	}
}
