package ggc.products;

import java.io.Serializable;

import ggc.partners.Observer;

public interface Observable extends Serializable {
	public void registerObserver(Observer observer);
	public void removeObserver(Observer observer);
	public void notifyObservers(String event);
}
