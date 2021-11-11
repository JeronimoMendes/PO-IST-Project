package ggc.util;

import java.util.List;
import java.util.ArrayList;

import ggc.transactions.Acquisition;
import ggc.transactions.Sale;
import ggc.transactions.Breakdown;
import ggc.transactions.Transaction;

public class IsAcquisition implements VisitorTransaction {	
	private List<Transaction> _transactions = new ArrayList<Transaction>();

	@Override
	public void visit(Acquisition accq) {
		_transactions.add(accq);
	}

	@Override
	public void visit(Sale sale) {
		// do nothing because it isn't an Acquisition
	}

	@Override
	public void visit(Breakdown breakdown) {
		// do nothing because it isn't an Acquisition
	}

	@Override
	public List<Transaction> getTransactions() {
		return _transactions;
	}

	@Override
	public double getBudget() {
		return 0;
	}
}