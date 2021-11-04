package ggc.transactions;

import ggc.util.Visitor;

public class Accquisition extends Transaction {
	public Accquisition(String id) {
		super(id);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public void pay() {}
}
