package ggc.util;

import java.util.List;
import java.util.ArrayList;

import ggc.products.Product;
import ggc.products.ComposedProduct;
import ggc.transactions.Transaction;

public class IsComposed implements VisitorProduct {	
	private List<Product> _products = new ArrayList<Product>();

	@Override
	public void visit(ComposedProduct product) {
		_products.add(product);
	}

	@Override
	public void visit(Product product) {
		// do nothing because it isn't an Acquisition
	}

    @Override
    public List<Product> getProducts() {
		return _products;
	}

	@Override
	public Transaction getTransaction() {
		return null;
	}
}