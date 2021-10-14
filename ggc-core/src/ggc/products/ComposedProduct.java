package ggc.products;

import java.util.Map;
import java.util.List;

public class ComposedProduct extends Product {
	/** Recipe of products that composes this Composed Product */
	private Map<Integer, Product>[] _recipe;
	
	/** Number that multiplies the combined price of each Product in the recipe */
	private double _alpha;

	/**
	 * Main constructor
	 */
	public ComposedProduct(String id, Map<Integer, Product>[] recipe, double alpha) {
		super(id);
		_recipe = recipe;
		_alpha = alpha;
	}
}
