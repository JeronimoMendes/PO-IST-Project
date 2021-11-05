package ggc.util;

import ggc.transactions.Sale;
import ggc.transactions.Acquisition;
import ggc.transactions.Breakdown;

public class ReadyBudget implements Visitor {
	private double _budget = 0;

	@Override
	public void visit(Sale sale) {
		if (sale.isPaid())
			_budget += sale.getPaidValue();
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
}