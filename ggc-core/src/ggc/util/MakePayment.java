package ggc.util;

import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Method;

import ggc.transactions.Acquisition;
import ggc.transactions.Sale;
import ggc.transactions.Breakdown;
import ggc.transactions.Transaction;

public class MakePayment implements VisitorTransaction {
	@Override
	public void visit(Acquisition accq) {
		// do nothing 
	}

	@Override
	public void visit(Sale sale) {
		sale.pay();
	}

	@Override
	public void visit(Breakdown breakdown) {
		// do nothing
	}

	@Override
	public List<Transaction> getTransactions() {
		return null;
	}

	@Override
	public double getBudget() {
		return 0;
	}
}