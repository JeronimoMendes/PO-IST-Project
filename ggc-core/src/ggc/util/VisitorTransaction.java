package ggc.util;

import java.io.Serializable;
import java.util.List;

import ggc.transactions.Sale;
import ggc.transactions.Acquisition;
import ggc.transactions.Breakdown;
import ggc.transactions.Transaction;

public interface VisitorTransaction extends Serializable {
	void visit(Sale sale);
	void visit(Acquisition accq);
	void visit(Breakdown breakdown);
	double getBudget();
	List<Transaction> getTransactions();
}
