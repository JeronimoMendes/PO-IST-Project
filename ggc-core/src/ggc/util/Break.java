package ggc.util;

import java.util.List;
import java.util.ArrayList;

import ggc.products.Product;
import ggc.products.ComposedProduct;
import ggc.transactions.Transaction;
import ggc.transactions.Breakdown;
import ggc.partners.Partner;
import ggc.products.Batch;
import ggc.products.Component;


public class Break implements VisitorProduct {
    private int _amount;
    private Transaction _transaction;
	private int _tID;
	private Partner _partner;

    public Break(int amount, int tID, Partner partner) {
        _amount = amount;
		_tID = tID;
		_partner = partner;
    }

	@Override
	public void visit(ComposedProduct product) {
		product.update(-_amount);

		double insertionPrice = product.getRecipe().computeInsertionPrice(_amount);
		String recipeValue = product.getRecipe().getRecipeValueString(_amount);
		product.getRecipe().createNewBatches(_amount, _partner);

		double breakdownPrice = product.getMinPrice() * _amount - insertionPrice;
		Breakdown breakdown = new Breakdown(
			_tID,
			product,
			_partner,
			_amount,
			breakdownPrice / _amount,
			Math.abs(breakdownPrice),
			recipeValue
		);
		breakdown.pay();

		_transaction = breakdown;
	}

	@Override
	public void visit(Product product) {
		// do nothing because it isn't a
	}

    @Override
    public List<Product> getProducts() {
		return null;
	}

    @Override
    public Transaction getTransaction() {
        return _transaction;
    }
}