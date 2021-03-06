package ggc.util;

import java.util.List;

import ggc.transactions.Sale;
import ggc.transactions.Acquisition;
import ggc.transactions.Breakdown;
import ggc.transactions.Transaction;

public class ReadyBudget implements VisitorTransaction {
	private double _budget = 0;

	@Override
	public void visit(Sale sale) {
		if (sale.isPaid())
			_budget += sale.getTotalPrice();
	}

	@Override
	public void visit(Acquisition accq) {
		if (accq.isPaid())
			_budget -= accq.getBaseValue();
	}

	@Override
	public void visit(Breakdown breakdown) {
		if (breakdown.isPaid())
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