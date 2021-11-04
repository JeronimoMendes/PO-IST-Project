package ggc.util;

import java.io.Serializable;

import ggc.transactions.Sale;
import ggc.transactions.Accquisition;
import ggc.transactions.Breakdown;

public interface Visitor extends Serializable {
	void visit(Sale sale);
	void visit(Accquisition accq);
	void visit(Breakdown breakdown);
}
