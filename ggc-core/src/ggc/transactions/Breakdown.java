package ggc.transactions;

import ggc.util.Visitor;

public class Breakdown extends Transaction {
	public Breakdown(String id) {
		super(id);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public void pay() {}
}
