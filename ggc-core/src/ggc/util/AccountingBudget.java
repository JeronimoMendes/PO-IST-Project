package ggc.util;

import java.util.List;

import ggc.transactions.Sale;
import ggc.transactions.Acquisition;
import ggc.transactions.Breakdown;
import ggc.transactions.Transaction;


public class AccountingBudget implements Visitor {
	private double _budget = 0;

	@Override
	public void visit(Sale sale) {
		_budget += sale.getPaidValue();
	}

	@Override
	public void visit(Acquisition accq) {
		_budget -= accq.getBaseValue();
	}

	@Override
	public void visit(Breakdown breakdown) {
		_budget += breakdown.getBaseValue();
	}

	@Override
	public double getBudget() {
		return _budget;
	}

	@Override
	public List<Transaction> getTransactions() {
		return null;
	};
}
