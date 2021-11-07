package ggc.transactions;

import ggc.util.Visitable;

import ggc.partners.Partner;
import ggc.products.Product;

public abstract class Transaction implements Visitable {
	/** Transaction's ID */
	private int _id;

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

	public Transaction(int id, Product product, Partner partner, int quantity, double unityValue) {
		_id = id;
		_product = product;
		_partner = partner;
		_quantity = quantity;
		_baseValue = unityValue * quantity;
	}

	public double getBaseValue() {
		return _baseValue;
	}

	public boolean isPaid() {
		return _paid;
	}
	
	public void setPaid(boolean paid) {
		_paid = paid;
	}

	public void setPaymentDate(int date) {
		_paymentDate = date;
	}

	public int getPaymentDate() {
		return _paymentDate;
	}

	public Partner getPartner() {
		return _partner;
	}

	public Product getProduct() {
		return _product;
	}

	public int getQuantity() {
		return _quantity;
	}

	public int getID() {
		return _id;
	}


	public abstract void pay();
}
