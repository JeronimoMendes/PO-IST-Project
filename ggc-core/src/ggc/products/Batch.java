package ggc.products;

import ggc.partners.Partner;
import java.io.Serializable;

public class Batch implements Serializable {
	private static final long serialVersionUID = 202110272100L;
	/** Partner's ID that supplies the batch */
	private String _supplier;

	/** Product's ID that makes up the batch */
	private String _product;

	/** Number of product available in the batch */
	private int _stock;

	/** Price of 1 unit of product */
	private double _price;

	/** Number that multiplies the price of the Product, in case it is a composed Product */
	private double _alpha;

	/**
	 * Constructor for a batch of a simple Product
	 */
	public Batch(String supplier, String product, int stock, double price) {
		_supplier = supplier;
		_product = product;
		_stock = stock;
		_price = price;
	}

	/**
	 * COnstructor for a batch of a composed Product
	 */
	public Batch(String supplier, String product, int stock, double price, double alpha) {
		_supplier = supplier;
		_product = product;
		_stock = stock;
		_price = price;
		_alpha = alpha;
	}


	/**
	 * Returns partner who's the supplier
	 * 
	 * @return supplier Partner
	 */
	public String getSupplier() { return _supplier; }

	/**
	 * Returns the product that composes the batch
	 * 
	 * @return product Product
	 */
	public String getProduct() { return _product; }

	/**
	 * Returns the remaining stock of product
	 * 
	 * @return stock int
	 */
	public int getStock() { return _stock; }

	/**
	 * Returns the price per unit of product
	 * 
	 * @return price double
	 */
	public double getPrice() { return _price; }

	@Override
	public String toString() {
		return String.format("%s|%s|%d|%d", _product, _supplier, (int)_price, _stock);
	}

}
