package ggc.transactions;

import ggc.util.Visitor;

import ggc.partners.Partner;
import ggc.products.Product;


public class Acquisition extends Transaction {
	public Acquisition(int id, Product product, Partner partner, int quantity, double baseValue, int date) {
		super(id, product, partner, quantity, baseValue);
		setPaymentDate(date);
		pay();
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public void pay() {
		setPaid(true);
	}
}
