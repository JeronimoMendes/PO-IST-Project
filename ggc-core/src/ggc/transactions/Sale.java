package ggc.transactions;

import ggc.util.Visitor;

public class Sale extends Transaction {
	public Sale(String id) {
		super(id);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public void pay() {}
}
