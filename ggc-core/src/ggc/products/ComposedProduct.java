package ggc.products;

import ggc.partners.Observer;
import ggc.util.VisitorProduct;

import java.util.Map;
import java.util.List;

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
	public ComposedProduct(String id, List<Observer> observers, double alpha, String recipe) {
		super(id, observers);
		_recipe = new Recipe(recipe);
		_alpha = alpha;
	}

	@Override
	public Recipe getRecipe() {
		return _recipe;
	}

	@Override
	public String toString() {
		return String.format(Locale.US, "%s|%d|%d|%s|%s", getID(), (int)getMaxPrice(), getStock(), _alpha, _recipe.toString());
	}

	@Override
	public void accept(VisitorProduct visitor) {
		visitor.visit(this);
	}
}
