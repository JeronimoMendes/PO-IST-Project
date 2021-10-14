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
}
