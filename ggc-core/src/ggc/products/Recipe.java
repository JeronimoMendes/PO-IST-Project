package ggc.products;

import java.io.Serializable;

import java.util.List;
import java.util.ArrayList;

public class Recipe implements Serializable {

	/** The recipe itself, the integer determines the amount of product */
	private List<Component> _components = new ArrayList<Component>();

	private String _recipeString;

	public Recipe(String stringRecipe) {
		for (String componentString: stringRecipe.split("#"))
			_components.add(new Component(componentString));
		
		_recipeString = stringRecipe;
	}

	public List<Component> getComponents() {
		return _components;
	}

	@Override
	public String toString() {
		return _recipeString;
	}

}
