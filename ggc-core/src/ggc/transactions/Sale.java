package ggc.transactions;

import ggc.util.Visitor;

import ggc.partners.Partner;
import ggc.products.Product;

public class Sale extends Transaction {
	private double _totalValue;
	private int _paymentDeadline;

	public Sale(int id, Product product, Partner partner, int quantity, double baseValue, int paymentDeadline) {
		super(id, product, partner, quantity, baseValue);
		_paymentDeadline = paymentDeadline;
		_totalValue = baseValue * quantity;
	}

	public double getTotalValue() {
		return _totalValue;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public void pay() {}

	@Override
	public String toString() {
		String string = String.format("VENDA|%s|%s|%s|%d|%d|%d|%d",
							getID(), getPartner().getID(),
							getProduct().getID(), getQuantity(),
							(int)getBaseValue(), (int)(_totalValue),
							_paymentDeadline
							);

		if (isPaid())
			string += "|" + getPaymentDate();

		return string;
	}
}
