package ggc.products;

import java.io.Serializable;
import ggc.partners.Observer;
import ggc.util.VisitorProduct;
import ggc.util.Visitable;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import ggc.exceptions.NoStockException;

public class Product implements Observable, Visitable<VisitorProduct> {
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

	private List<Batch> _batches = new ArrayList<Batch>();

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

	public boolean checkStock(int amount) throws NoStockException {
		if (_stock < amount) {
			throw new NoStockException(_id, _stock, amount);
		}
		return true;
	}

	public void setStock(int newStock) { _stock = newStock; }

	public double priceToPay() {
		return _minPrice;
	}

	public int getN() {
		return _N;
	}

	public List<Batch> getBatches() {
		return _batches;
	}

	public void addBatch(Batch batch) {
		_batches.add(batch);
	}

	/**
	 * Updates batches stock when a product is bought
	 * 
	 * @param product Product thats being sold
	 * @param amount units of the product being sold
	 */
	public void updateBatches(int amount) {
		Collections.sort(_batches, new Batch.PriceComparator());

		for (Batch batch: _batches) {
			int batchStock = batch.getStock();
			if (batchStock - amount < 0) {
				batch.setStock(0);
				amount = -(batchStock - amount);
			} else {
				batch.setStock(batchStock - amount);
				break;
			}
		}
	}

	// FIXME
	public Recipe getRecipe() {return null;};

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
		if (amount < 0) {
			updateBatches(-amount);
		}
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
		return String.format("%s|%d|%d", _id, Math.round(_maxPrice), _stock);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Product) {
			Product p = (Product) o;
			return _id.equals(p.getID());
		}
		return false;
	}

	@Override
	public void accept(VisitorProduct visitor) {
		visitor.visit(this);
	}
}
