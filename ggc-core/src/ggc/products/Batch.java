package ggc.products;

import ggc.partners.Partner;
import java.io.Serializable;

public class Batch implements Serializable {
	/** Partner that supplies the batch */
	private Partner _supplier;

	/** Product that makes up the batch */
	private Product _product;

	/** Number of product available in the batch */
	private int _stock;

	/** Price of 1 unit of product */
	private double _price;


	/**
	 * Main constructor
	 */
	public Batch(Partner supplier, Product product, int stock, double price) {
		_supplier = supplier;
		_product = product;
		_stock = stock;
		_price = price;
	}

	/**
	 * Returns partner who's the supplier
	 * 
	 * @return supplier Partner
	 */
	public Partner getSupplier() { return _supplier; }

	/**
	 * Returns the product that composes the batch
	 * 
	 * @return product Product
	 */
	public Product getProduct() { return _product; }

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

}
