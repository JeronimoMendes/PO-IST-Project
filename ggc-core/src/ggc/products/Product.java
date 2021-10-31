package ggc.products;

import java.io.Serializable;
import ggc.partners.Observer;

import java.util.Map;
import java.util.HashMap;

public class Product implements Observable {
	private static final long serialVersionUID = 202110272100L;

	/** Product's unique ID */
	private String _id;

	/** Product's max price */
	private double _maxPrice;

	/** Product's stock */
	private int _stock;

	/** Observers that will be notified when this Product is updated */
	private Map<Observer, Boolean> _observers = new HashMap<Observer, Boolean>();

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

	/**
	 * This function updates the stock and price.
	 * If the price given is higher then maxPrice, maxPrice will be updated.
	 * Stock will be incremented by the given stock change.
	 * 
	 * @param price price at which the product is being sold
	 * @param stock new amount of product being sold
	 */
	public void update(double price, int stock) {
		if (_maxPrice < price) {
			_maxPrice = price;
		}
		_stock += stock;
	}

	@Override
	public void registerObserver(Observer observer) {
		_observers.put(observer, true);
	}

	@Override
	public void removeObserver(Observer observer) {
		_observers.put(observer, false);
	}

	@Override
	public void notifyObservers(String event) {
		for (Map.Entry<Observer, Boolean> observer : _observers.entrySet()) {
			if (observer.getValue()) {
				observer.getKey().update(event, _id);
			}
		}
	}

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
