package ggc.util;

import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Method;

import ggc.transactions.Acquisition;
import ggc.transactions.Sale;
import ggc.transactions.Breakdown;
import ggc.transactions.Transaction;

public class Paid implements Visitor {	
	private List<Transaction> _transactions = new ArrayList<Transaction>();

	@Override
	public void visit(Acquisition accq) {
	}

	@Override
	public void visit(Sale sale) {
		if (sale.isPaid())
			_transactions.add(sale);
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