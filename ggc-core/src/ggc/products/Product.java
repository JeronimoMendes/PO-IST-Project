package ggc.products;

import java.io.Serializable;
import ggc.partners.Observer;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class Product implements Observable {
	private static final long serialVersionUID = 202110272100L;

	/** Product's unique ID */
	private String _id;

	/** Product's max price */
	private double _maxPrice = 0;

	/** Product's min price */
	private double _minPrice = 0;

	/** Product's stock */
	private int _stock;

	private int _N = 5;

	/** Observers that will be notified when this Product is updated */
	private Map<Observer, Boolean> _observers = new HashMap<Observer, Boolean>();

	/**
	 * Main constructor
	 */
	public Product(String id, List<Observer> observers) {
		_id = id;
		for (Observer obs: observers)
			_observers.put(obs, true);
	}

	public String getID() { return _id; }

	public double getMaxPrice() { return _maxPrice; }
	
	public void setMaxPrice(double price) { _maxPrice = price; }

	public double getMinPrice() { return _minPrice; }
	
	public void setMinPrice(double price) { _minPrice = price; }

	public int getStock() { return _stock; }

	public void setStock(int newStock) { _stock = newStock; }

	public int getN() {
		return _N;
	}

	/**
	 * This function updates the stock and price.
	 * If the price given is higher then maxPrice, maxPrice will be updated.
	 * Stock will be incremented by the given stock change.
	 * 
	 * @param price price at which the product is being sold
	 * @param stock new amount of product being sold
	 * @param importing if the update is done when importing a file
	 */
	public void update(double price, int stock, boolean importing) {
		if (_maxPrice < price) {
			_maxPrice = price;
		}
		
		if (_minPrice > price || _minPrice == 0) {
			_minPrice = price;
			if (!importing) 
				notifyObservers(String.format("BARGAIN|%s|%s", _id, (int)price));
		}

		_stock += stock;
		if (!importing && stock == _stock)
			notifyObservers(String.format("NEW|%s|%s", _id, (int)price));
	}

	public void update(int amount) {
		_stock += amount;
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
