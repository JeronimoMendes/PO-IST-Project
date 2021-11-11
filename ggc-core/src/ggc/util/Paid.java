package ggc.util;

import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Method;

import ggc.transactions.Acquisition;
import ggc.transactions.Sale;
import ggc.transactions.Breakdown;
import ggc.transactions.Transaction;

public class Paid implements VisitorTransaction {	
	private List<Transaction> _transactions = new ArrayList<Transaction>();
	private String _pID;

	public Paid(String pID) {
		_pID = pID;
	}

	@Override
	public void visit(Acquisition accq) {
	}

	@Override
	public void visit(Sale sale) {
		if (sale.isPaid() && sale.getPartner().getID().equals(_pID))
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