package ggc.util;

import java.io.Serializable;
import java.util.List;

import ggc.products.ComposedProduct;
import ggc.products.Product;
import ggc.transactions.Transaction;

public interface VisitorProduct extends Serializable {
	void visit(ComposedProduct product);
	void visit(Product product);
	List<Product> getProducts();
	Transaction getTransaction();
}
