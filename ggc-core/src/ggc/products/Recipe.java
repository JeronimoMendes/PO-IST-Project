package ggc.products;

import java.io.Serializable;

import java.util.List;
import java.util.ArrayList;
import ggc.partners.Partner;

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

	public double computeInsertionPrice(int amount) {
		double price = 0;
		for (Component component: _components) {
			Product product = component.getProduct();
			price += component.computePrice() * component.getQuantity() * amount;
		}

		return price;
	}

	public String getRecipeValueString(int amount) {
		String recipeValue = "";
		for (Component component: _components) {
			recipeValue += String.format(
				"%s:%d:%d#",
				component.getProduct().getID(),
				component.getQuantity() * amount,
				Math.round(component.computePrice()) * component.getQuantity() * amount
			);
		}
		recipeValue = recipeValue.substring(0, recipeValue.length() - 1);

		return recipeValue;
	}

	public void createNewBatches(int amount, Partner partner) {
		for (Component component: _components) {
			
			Batch newBatch = new Batch(
				partner.getID(),
				component.getProduct().getID(),
				component.getQuantity() * amount,
				component.computePrice()
			);
			component.getProduct().addBatch(newBatch);

			component.getProduct().update(amount * component.getQuantity());
		}
	}

	@Override
	public String toString() {
		return _recipeString;
	}

}
