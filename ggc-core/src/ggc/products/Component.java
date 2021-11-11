package ggc.products;

import java.io.Serializable;

public class Component implements Serializable {
	/** Amount of this component */
	private int _quantity;

	/** Product's ID that makes up the component */
	private Product _product;

	/**
	 * Main constructor
	 */
	public Component(Product product, int amount) {
		_product = product;
		_quantity = amount;
	}

	public Product getProduct() {
		return _product;
	}

	public int getQuantity() {
		return _quantity;
	}
}
