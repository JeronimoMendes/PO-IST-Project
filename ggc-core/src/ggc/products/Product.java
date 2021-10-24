package ggc.products;

import java.io.Serializable;

public class Product implements Serializable {
	/** Product's unique ID */
	protected String _id;

	/** Product's max price */
	protected double _maxPrice;

	/** Product's stock */
	protected int _stock;

	/**
	 * Main constructor
	 */
	public Product(String id){
		_id = id;
	}

	public String getID() { return _id; }

	public double getMaxPrice() { return _maxPrice; }

	public int getStock() { return _stock; }

	public void setMaxPrice(double price) { _maxPrice = price; }

	public void setStock(int newStock) { _stock = newStock; }

	@Override
	public String toString() {
		return String.format("%s|%d|%d", _id, (int)_maxPrice, _stock);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Product) {
			Product p = (Product) o;
			return _id.equals(p.getID());
		}
		return false;
	}
}
