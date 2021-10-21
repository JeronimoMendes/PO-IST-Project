package ggc.products;

import java.io.Serializable;

public class Product implements Serializable {
	/** Product's unique ID */
	private String _id;

	/**
	 * Main constructor
	 */
	public Product(String id){
		_id = id;
	}

	public String getID() {
		return _id;
	}

	@Override
	public String toString() {
		// TODO: Add search for the most expensive batch and total stock
		return _id;
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
