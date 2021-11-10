package ggc.products;

import ggc.partners.Partner;
import java.io.Serializable;
import java.util.Comparator;

import ggc.CollatorWrapper;

public class Batch implements Serializable, Comparable<Batch> {
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

	public void setStock(int newStock) { _stock = newStock; }

	/**
	 * Returns the price per unit of product
	 * 
	 * @return price double
	 */
	public double getPrice() { return _price; }

	public static class PriceComparator implements Comparator<Batch> {
		@Override
		public int compare(Batch batch1, Batch batch2) {
			return (int)(batch1.getPrice() - batch2.getPrice());
		}
	}

	@Override
	public String toString() {
		return String.format("%s|%s|%d|%d", _product, _supplier, (int)_price, _stock);
	}

	@Override
	public int compareTo(Batch other) {
		CollatorWrapper col = new CollatorWrapper();

		if (col.compare(_product, other.getProduct()) != 0)
			return (col.compare(_product, other.getProduct()));
		if (col.compare(_supplier, other.getSupplier()) != 0)
			return (col.compare(_supplier, other.getSupplier()));
		if (_price != other.getPrice())
			return ((_price - other.getPrice()) > 0) ? 1 : -1;
		
		return _stock - other.getStock();
	}
}
