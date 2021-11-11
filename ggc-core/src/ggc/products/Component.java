package ggc.products;

import java.io.Serializable;

public class Component implements Serializable {
	/** Amount of this component */
	private int _quantity;

	/** Product's ID that makes up the component */
	private String _pID;

	/**
	 * Main constructor
	 */
	public Component(String componentString) {
		String args[] = componentString.split(":");

		_pID = args[0];
		_quantity = Integer.parseInt(args[1]);
	}

	public String getProductID() {
		return _pID;
	}

	public int getQuantity() {
		return _quantity;
	}
}
