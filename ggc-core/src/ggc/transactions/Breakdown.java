package ggc.transactions;

import ggc.util.Visitor;

import ggc.partners.Partner;
import ggc.products.Product;

public class Breakdown extends Transaction {
	public Breakdown(int id, Product product, Partner partner, int quantity, double baseValue) {
		super(id, product, partner, quantity, baseValue);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public void pay() {}
}
