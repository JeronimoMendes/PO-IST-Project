package ggc.products;

import java.io.Serializable;

import java.util.List;
import java.util.ArrayList;

import ggc.exceptions.NoStockException;

public class Recipe implements Serializable {

	/** The recipe itself, the integer determines the amount of product */
	private List<Component> _components = new ArrayList<Component>();

	private String _recipeString;

	public Recipe(List<Component> components, String recipeString) {
		_components = components;
		_recipeString = recipeString;
	}

	public boolean checkIfCanBeMade(int amount, List<Batch> resources) throws NoStockException {
		for (Component component: _components) {
			component.getProduct().checkStock(amount * component.getQuantity());
		}
		return true;
	}

	public List<Component> getComponents() {
		return _components;
	}

	public void make(int amount) {
		for (Component component: _components) {
			Product product = component.getProduct();
			System.out.println("A fazer produto" + product.toString());
			product.update(-amount * component.getQuantity());
		}
	}

	public double computeRecipePrice() {
		double price = 0;
		for (Component component: _components) {
			Product product = component.getProduct();
			price += product.priceToPay() * component.getQuantity();
		}

		return price;
	}

	@Override
	public String toString() {
		return _recipeString;
	}

}
