package ggc.transactions;

import ggc.util.Visitable;

import ggc.partners.Partner;
import ggc.products.Product;

public abstract class Transaction implements Visitable {
	/** Transaction's ID */
	private String _id;

	/** Partner with whom the transaction is */
	private Partner _partner;

	/** Product that's being traded */
	private Product _product;

	/** Quantity of product that's being traded */
	private int _quantity;

	/** If this transaction has been paid or not */
	private boolean _paid = false;

	/** Date at which the transaction was paid */
	private int _paymentDate;

	/** Base value of the transaction */
	private double _baseValue;

	public Transaction(String id) {
		_id = id;
	}

	public double getBaseValue() {
		return _baseValue;
	}

	public boolean isPaid() {
		return _paid;
	}

	public abstract void pay();
}
