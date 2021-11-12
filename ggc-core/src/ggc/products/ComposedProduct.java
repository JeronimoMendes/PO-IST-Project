package ggc.products;

import ggc.partners.Observer;
import ggc.util.VisitorProduct;

import java.util.Map;
import java.util.List;

import ggc.exceptions.NoStockException;

// Locale is used to have a "." separating integer and decimal part 
import java.util.Locale;

public class ComposedProduct extends Product {
	private static final long serialVersionUID = 202110272100L;
	/** Recipe of products that composes this Composed Product */
	private Recipe _recipe;
	
	/** Number that multiplies the combined price of each Product in the recipe */
	private double _alpha;

	private int _N = 3;

	/**
	 * Main constructor
	 */
	public ComposedProduct(String id, List<Observer> observers, double alpha, Recipe recipe) {
		super(id, observers);
		_recipe = recipe;
		_alpha = alpha;
	}

	/**
	 * Checks if there's a given amount of composed product, and if not, checks if it can be
	 * made from other products
	 * 
	 * @param amount amount of product to be checked
	 * @param List<Batch> Existences of the warehouse to check if the composed product can be made
	 */
	public boolean checkStock(int amount) throws NoStockException {
		if (amount < getStock()) return true;

		// amount of composed product that needs to be fabricated
		int toMake = amount - getStock();

		// call on the recipe to see if you can make the product
		return _recipe.checkIfCanBeMade(toMake, getBatches());
	}

	public void update(int amount) {
		if (amount < 0 && getStock() < -amount) {
			makeComposed(-amount - getStock());
			setStock(0);
		} else {
			setStock(getStock() + amount);
		}
	}

	public void makeComposed(int amount) {
		double price = _recipe.computeRecipePrice() * (1 + _alpha);
		_recipe.make(amount);
		if (getMaxPrice() < price) {
			setMaxPrice(price);
		}

		setStock(getStock() + amount);
	}

	@Override
	public double priceToPay() {
		double price = _recipe.computeRecipePrice() * (1 + _alpha);
		return price;
	}

	@Override
	public Recipe getRecipe() {
		return _recipe;
	}

	@Override
	public String toString() {
		return String.format(Locale.US, "%s|%d|%d|%s|%s", getID(), Math.round(getMaxPrice()), getStock(), _alpha, _recipe.toString());
	}

	@Override
	public void accept(VisitorProduct visitor) {
		visitor.visit(this);
	}
}
