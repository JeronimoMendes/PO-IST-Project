package ggc.transactions;

import ggc.util.Visitor;

import ggc.partners.Partner;
import ggc.products.Product;

public class Sale extends Transaction {
	private double _paidValue = 0;

	public Sale(int id, Product product, Partner partner, int quantity, double baseValue) {
		super(id, product, partner, quantity, baseValue);
	}

	public double getPaidValue() {
		return _paidValue;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public void pay() {}
}
