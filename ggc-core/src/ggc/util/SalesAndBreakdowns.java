package ggc.util;

import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Method;

import ggc.transactions.Acquisition;
import ggc.transactions.Sale;
import ggc.transactions.Breakdown;
import ggc.transactions.Transaction;

public class SalesAndBreakdowns implements VisitorTransaction {	
	private List<Transaction> _transactions = new ArrayList<Transaction>();
    private String _pID;

    public SalesAndBreakdowns(String pID) {
        _pID = pID;
    }

	@Override
	public void visit(Acquisition accq) {
		// do nothing
	}

	@Override
	public void visit(Sale sale) {
        if (sale.getPartner().getID().equals(_pID))
		    _transactions.add(sale);
	}

	@Override
	public void visit(Breakdown breakdown) {
        if (breakdown.getPartner().getID().equals(_pID))
		    _transactions.add(breakdown);
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