package ggc.util;

import java.io.Serializable;

import ggc.transactions.Sale;
import ggc.transactions.Acquisition;
import ggc.transactions.Breakdown;

public interface Visitor extends Serializable {
	void visit(Sale sale);
	void visit(Acquisition accq);
	void visit(Breakdown breakdown);
	double getBudget();
}
