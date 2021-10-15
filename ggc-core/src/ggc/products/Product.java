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

	@Override
	public String toString() {
		// TODO: Add search for the most expensive batch and total stock
		return _id;
	}
}
