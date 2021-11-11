package ggc.util;

import java.io.Serializable;
import java.util.List;

import ggc.products.ComposedProduct;
import ggc.products.Product;

public interface VisitorProduct extends Serializable {
	void visit(ComposedProduct product);
	void visit(Product product);
	List<Product> getProducts();
}
