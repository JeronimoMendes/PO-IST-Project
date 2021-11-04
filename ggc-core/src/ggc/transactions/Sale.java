package ggc.transactions;

import ggc.util.Visitor;

public class Sale extends Transaction {
	private double _paidValue = 0;

	public Sale(String id) {
		super(id);
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
