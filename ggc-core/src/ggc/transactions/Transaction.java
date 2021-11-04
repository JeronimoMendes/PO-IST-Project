package ggc.transactions;

import java.io.Serializable;

import ggc.partners.Partner;
import ggc.products.Product;

public abstract class Transaction implements Serializable {
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
	private double baseValue;

	public Transaction(String id) {
		_id = id;
	}

	public abstract void pay();
}
