package ggc.transactions;

import ggc.util.Visitor;

import ggc.partners.Partner;
import ggc.products.Product;

public class Sale extends Transaction {
	private int _paymentDeadline;
	private double _paidPrice;
	private int _storeDate;

	public Sale(int id, Product product, Partner partner, int quantity, double baseValue, int paymentDeadline) {
		super(id, product, partner, quantity, baseValue);
		_paymentDeadline = paymentDeadline;
	}

	public double getTotalPrice() {
		if (isPaid()){
			return _paidPrice;
		}

		int N = getProduct().getN();

		int currentDate = getStoreDate();

		if (currentDate <= _paymentDeadline - N) {
			// p1
			return getBaseValue() * getPartner().getStatus().p1Price();
		} else if (_paymentDeadline - N < currentDate && currentDate <= _paymentDeadline) {
			// p2
			return getBaseValue() * getPartner().getStatus().p2Price(_paymentDeadline - currentDate);
		} else if (_paymentDeadline + N >= currentDate) {
			// p3
			return getBaseValue() * getPartner().getStatus().p3Price(currentDate - _paymentDeadline);
		} else {
			// p4
			return getBaseValue() * getPartner().getStatus().p4Price(currentDate - _paymentDeadline);
		}
	}

	public int getDateInterval() {
		return _paymentDeadline - getStoreDate();
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public void pay() {
		setPaymentDate(getStoreDate());
		double priceToPay = getTotalPrice();
		_paidPrice = priceToPay;
		
		getPartner().pay(priceToPay);
		getPartner().getStatus().pay(this);
		setPaid(true);
	}

	@Override
	public String toString() {
		String string = String.format("VENDA|%s|%s|%s|%d|%d|%d|%d",
							getID(), getPartner().getID(),
							getProduct().getID(), getQuantity(),
							(int)getBaseValue(), (int)(getTotalPrice()),
							_paymentDeadline
							);

		if (isPaid())
			string += "|" + getPaymentDate();

		return string;
	}
}
