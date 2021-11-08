package ggc.transactions;

import ggc.util.Visitor;

import ggc.partners.Partner;
import ggc.products.Product;

public class Sale extends Transaction {
	private double _paidValue = 0;
	private int _paymentDeadline;

	public Sale(int id, Product product, Partner partner, int quantity, double baseValue, int paymentDeadline) {
		super(id, product, partner, quantity, baseValue);
		_paymentDeadline = paymentDeadline;
	}

	public double getPaidValue() {
		return _paidValue;
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
							(int)getBaseValue(), (int)(getBaseValue() - _paidValue),
							_paymentDeadline
							);

		if (isPaid())
			string += "|" + getPaymentDate();

		return string;
	}
}
