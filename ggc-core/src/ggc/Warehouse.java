package ggc;

import java.io.IOException;
import java.io.Serializable;
import java.io.BufferedReader;
import java.io.FileReader;

import java.util.regex.Pattern;
import java.util.TreeMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ggc.exceptions.*;
import ggc.partners.*;
import ggc.products.*;
import ggc.transactions.*;
import ggc.util.*;


import java.util.Collections;


/**
 * Class Warehouse implements a warehouse.
 */
public class Warehouse implements Serializable {

	/** Serial number for serialization. */
	private static final long serialVersionUID = 202110272100L;

	/** Warehouse's date */
	private int _date = 0;

	/** Warehouse's partners */
	private Map<String, Partner> _partners = new TreeMap<String, Partner>(new CollatorWrapper());
	
	/** Warehouse's product */
	private Map<String, Product> _products = new TreeMap<String, Product>(new CollatorWrapper());

	/** Map tree of transactions */
	private Map<Integer, Transaction> _transactions = new TreeMap<Integer, Transaction>();

	/** Content of the warehouse */
	private Map<Product, Integer> _stock = new TreeMap<Product, Integer>();
	
	/**
	 * ########################################################################
	 * ################################# File #################################
	 * ########################################################################
	 */

	/**
	 * @param txtfile filename to be loaded.
	 * @throws IOException
	 * @throws BadEntryException
	 */
	public void importFile(String txtfile) throws IOException, BadEntryException, DuplicatePartnerException {
		BufferedReader input = new BufferedReader(new FileReader(txtfile));

		String line;
		while ((line = input.readLine()) != null) {
			String fields[] = line.split("\\|");
			parseLine(fields);
		}

		input.close();
	}

	/**
	 * Auxiliates importFile(), parsing each line into an object
	 * 
	 * @param fields[] the fields that come from the line split
	 */
	public void parseLine(String fields[]) throws DuplicatePartnerException {
		Pattern partner = Pattern.compile("^(PARTNER)");
		Pattern simpleBatch = Pattern.compile("^(BATCH_S)");
		Pattern complexBatch = Pattern.compile("^(BATCH_M)");
		Pattern sale = Pattern.compile("^(VENDA)");
		Pattern buy = Pattern.compile("^(COMPRA)");
		Pattern breakdown = Pattern.compile("^(DESAGREGA????O)");

		if (partner.matcher(fields[0]).matches()) {registerPartner(fields[1], fields[2], fields[3]);}
		else if (simpleBatch.matcher(fields[0]).matches()) {
			int stock = Integer.parseInt(fields[4]);
			double price = Double.parseDouble(fields[3]);
			registerBatch(fields[2], fields[1], stock, price);
		}
		else if (complexBatch.matcher(fields[0]).matches()) {
			int stock = Integer.parseInt(fields[4]);
			double price = Double.parseDouble(fields[3]);
			double alpha = Double.parseDouble(fields[5]);
			registerBatch(fields[2], fields[1], stock, price, alpha, fields[6]);
		}
		else if (sale.matcher(fields[0]).matches()) {}
		else if (buy.matcher(fields[0]).matches()) {}
		else if (breakdown.matcher(fields[0]).matches()) {}
	}

	/**
	 *  @return warehouse's date
	 */
	public int getDate() {
		return _date;
	}

	/**
	 * @param days days to advance date
	 * 
	 * @throws InvalidDateException if negative or null days
	 */
	public void advanceDate(int days) throws InvalidDaysException {
		if (days > 0) {
			_date += days;
		} else {
			throw new InvalidDaysException(days);
		}
	}

	/**
	 * Returns the ready budget of the warehouse
	 * 
	 * @return Warehouse's ready budget
	 */
	public double getReadyBudget() {
		VisitorTransaction visitor = new ReadyBudget();
		visitTransactions(visitor);
		return visitor.getBudget();
	}

	/**
	 * Returns the accounting budget of the warehouse
	 * 
	 * @return Warehouse's accounting budget
	 */
	public double getAccountingBudget() {
		VisitorTransaction visitor = new AccountingBudget();
		visitTransactions(visitor);
		return visitor.getBudget();
	}

	/**
	 * Visits the transactions with a visitor
	 */
	public void visitTransactions(VisitorTransaction visitor) {
		for (Transaction t: _transactions.values())
			t.accept(visitor);
	}


	/**
	 * ########################################################################
	 * ############################### Partners ###############################
	 * ########################################################################
	 */

	/**
	 * Create new partner
	 * 
	 * @param id ID
	 * @param name name
	 * @param address address
	*/
	public void registerPartner(String id, String name, String address) throws DuplicatePartnerException {
		if (checkPartner(id)) {
			throw new DuplicatePartnerException(id);
		}

		Partner newPartner = new Partner(id, name, address);
		_partners.put(id, newPartner);
	}

	/**
	 * Returns information about a given partner
	 * 
	 * @param partnerKey partner's ID
	 * 
	 * @return info String with info about Partner
	 */
	public String getPartner(String partnerKey) throws UnknownPartnerException {
		if (!checkPartner(partnerKey)) {
			throw new UnknownPartnerException(partnerKey);
		}
		
		String info = "";

		Partner partner = _partners.get(partnerKey);

		info += partner.showPartner();
		return info;
	}

	/**
	 * Returns all the registered partners
	 * 
	 * @return partners list of all the partners
	 */
	public String listPartners() {
		String info = "";

		for (String key: _partners.keySet()) {
			info += _partners.get(key).toString() + '\n';
		}

		if (info.length() > 0)
			info = info.substring(0, info.length() - 1);

		return info; // remove the last \n
	}

	/**
	 * Toggles product notifications of partner
	 * 
	 * @param partnerID partner's ID
	 * @param productID product's ID
	 */
	public void toggleNotifications(String partnerID, String productID)
				throws UnknownPartnerException, UnknownProductException {
		if (!checkPartner(partnerID)) {
			throw new UnknownPartnerException(partnerID);
		}
		if (!checkProduct(productID)) {
			throw new UnknownProductException(productID);
		}

		Product product = _products.get(productID);
		Partner partner = _partners.get(partnerID);

		product.removeObserver(partner);
	}

	/**
	 * Checks if a given partner's key corresponds to a registered partner
	 */
	public boolean checkPartner(String partnerKey) {
		return _partners.containsKey(partnerKey);
	}

	/**
	 * #################################################################################
	 * ############################### Product & Batches ###############################
	 * #################################################################################
	 */

	/**
	 * Checks if a given product's key corresponds to a registered product
	 */
	public boolean checkProduct(String pID) {
		return _products.containsKey(pID);
	}

	/**
	 * Registers a new Batch composed of a simple Product
	 * 
	 * @param sID Supplier's string ID
	 * @param pID Product's string ID
	 * @param stock number of units that will be sold in the batch
	 * @param price price of 1 unit of product
	 */
	public void registerBatch(String sID, String pID, int stock, double price) {
		if (!productExists(pID)) {
			registerProduct(pID);
		}

		Product product = _products.get(pID);
		product.update(price, stock, true);

		Batch newBatch = new Batch(sID, pID, stock, price);
		product.addBatch(newBatch);
		// _batches.add(newBatch);
	}

	/**
	 * Registers a new Batch composed of a composed Product
	 * 
	 * @param sID Supplier's string ID
	 * @param pID Product's string ID
	 * @param stock number of units that will be sold in the batch
	 * @param price price of 1 unit of product
	 * @param alpha number that multiplies the combined price of the ComposedProduct
	 * @param recipe recipe for the ComposedProduct
	 */
	public void registerBatch(String sID, String pID, int stock, double price, double alpha, String recipe) {
		if (!productExists(pID)) {
			registerProduct(pID, alpha, recipe);
		}
		
		Product product = _products.get(pID);
		product.update(price, stock, true);
		Batch newBatch = new Batch(sID, pID, stock, price);
		product.addBatch(newBatch);
		// _batches.add(newBatch);
	}

	/**
	 * Checks if there's a registered product with a given ID		
	 * 
	 * @param pID Product's ID
	 * @return boolean 
	 */
	public boolean productExists(String pID) {
		return _products.containsKey(pID);
	}

	/**
	 * Registers a simple Product
	 * 
	 * @param pID Product's ID
	 */
	public Product registerProduct(String pID) {
		Product newProduct = new Product(pID, new ArrayList<Observer>(_partners.values()));
		_products.put(pID, newProduct);

		return newProduct;
	}

	/**
	 * Registers a composed Product
	 * 
	 * @param pID Product's ID
	 */
	public Product registerProduct(String pID, double alpha, String recipeString) {
		List<Component> parsedRecipe = new ArrayList<Component>();
		
		for (String componentString: recipeString.split("#")) {
			String args[] = componentString.split(":");
			Product componentProduct = _products.get(args[0]);
			int quantity = Integer.parseInt(args[1]);
			parsedRecipe.add(new Component(componentProduct, quantity));
		}

		Recipe recipe = new Recipe(parsedRecipe, recipeString);

		Product newProduct = new ComposedProduct(pID, new ArrayList<Observer>(_partners.values()), alpha, recipe);
		_products.put(pID, newProduct);

		return newProduct;
	}

	/**
	 * Returns the list of all products
	 * 
	 * @return info String with info of all registered Products
	 */
	public String listProducts() {
		String info = "";

		for (String key: _products.keySet()) {
			Product product = _products.get(key);

			info += product.toString() + "\n";
		}

		if (info.length() > 0)
			info = info.substring(0, info.length() - 1);

		return info; // remove the last \n
	}

	/**
	 * Returns an array of batchs of a given Product
	 * 
	 * @param pID Product's ID to filter the array of batchs
	 * 
	 * @return batches list of batches of the given product
	 */
	public List<Batch> getBatchesOfProduct(String pID) throws UnknownProductException {
		if (!checkProduct(pID)) {
			throw new UnknownProductException(pID);
		}
		return getBatchesOfProduct(_products.get(pID));
	}

	/**
	 * Returns an array of batchs of a given Product
	 * 
	 * @param product Product
	 * 
	 * @return batches list of batches of the given product
	 */
	public List<Batch> getBatchesOfProduct(Product product) {
		return product.getBatches();
	}

	public List<Batch> getAllBatches() {
		List<Batch> batches = new ArrayList<Batch>();
		for (String p: _products.keySet()) {
			Product product = _products.get(p);
			batches.addAll(product.getBatches());
		}

		return batches;
	}

	/**
	 * Returns an array of batchs of a given Partner
	 * 
	 * @param pID Partner's ID to filter the array of batchs
	 * 
	 * @return batches list of batches of the given Partner
	 */
	public String getBatchesOfPartner(String pID) throws UnknownPartnerException {
		if (!checkPartner(pID)) {
			throw new UnknownPartnerException(pID);
		}

		List<Batch> list = getAllBatches().stream()
							.filter(batch -> batch.getSupplier().equals(pID) && batch.getStock() > 0)
							.collect(Collectors.toList());

		return getStrOfBatches(list);
	}

	/**
	 * Returns an array of batchs under a given price
	 * 
	 * @param pID Partner's ID to filter the array of batchs
	 * 
	 * @return batches list of batches under the given price
	 */
	public String getBatchesUnderPrice(int price) {
		List<Batch> list = getAllBatches().stream()
						.filter(batch -> batch.getPrice() < price && batch.getStock() > 0)
						.collect(Collectors.toList());

		return getStrOfBatches(list);
	}

	/**
	 * Returns list of all available batches (stock > 0)
	 * 
	 * @return String info of all available batches
	 */
	public String listAvailableBatches() {
		return getStrOfBatches(getAllBatches().stream().filter(b -> b.getStock() > 0)
												.collect(Collectors.toList())); 
	}

	/**
	 * This function returns the String representation of a list of Batch
	 * 
	 * @param batches List of Batch
	 * 
	 * @return info String representation of the list of Batch
	 */
	public String getStrOfBatches(List<Batch> batches) {
		String info = "";

		Collections.sort(batches);
		for(Batch batch: batches) {
			if (batch.getStock() > 0)
				info += batch.toString() + '\n';
		}

		if (info.length() > 0)
			info = info.substring(0, info.length() - 1);

		return info;
	}

	/**
	 * #################################################################################
	 * ################################# Transactions ##################################
	 * #################################################################################
	 */

	/**
	 * Checks if there's a registered Transaction with a given ID		
	 * 
	 * @param tID Transaction's ID
	 * @return boolean 
	 */
	public boolean checkTransaction(int tID) {
		return _transactions.containsKey(tID);
	}

	/**
	 * Register a new acquisition. Basically a function that accepts keys instead of objects
	 * 
	 * @param productID
	 * @param partnerID
	 * @param price
	 * @param amount
	 */
	public void registerAcquisition(String productID, String partnerID, double price, int amount) 
		throws UnknownPartnerException, UnknownProductException
	{
		if (!checkPartner(partnerID)) 
			throw new UnknownPartnerException(partnerID);

		if (!checkProduct(productID)) 
			throw new UnknownProductException(productID);

		registerAcquisition(_products.get(productID), _partners.get(partnerID), price, amount, false);
	}


	/**
	 * Register a new acquisition
	 * 
	 * @param product
	 * @param partner
	 * @param price
	 * @param amount
	 * @param isProductNew
	 */
	public void registerAcquisition(Product product, Partner partner, double price, int amount, boolean isProductNew) {
		int id = _transactions.size();

		Batch newBatch = new Batch(partner.getID(), product.getID(), amount, price);
		product.addBatch(newBatch);
		//_batches.add(newBatch);

		product.update(price, amount, isProductNew);

		Acquisition accq = new Acquisition(id, product, partner, amount, price, _date);

		_transactions.put(id, accq);
	}

	/**
	 * Register a new simple Product and Transaction
	 * 
	 * @param productID
	 * @param partnerID
	 * @param price
	 * @param amount
	 */
	public void registerProductInAcquisition(String productID, String partnerID, double price, int amount) {
		Product newProduct = registerProduct(productID);

		registerAcquisition(newProduct, _partners.get(partnerID), price, amount, true);
	}

	/**
	 * Register a new complex Product and Transaction
	 * 
	 * @param productID
	 * @param partnerID
	 * @param price
	 * @param amount
	 * @param recipe
	 * @param alpha
	 */
	public void registerProductInAcquisition(String productID, String partnerID, double price,
												int amount, String recipe, double alpha) {
		Product newProduct = registerProduct(productID, alpha, recipe);

		registerAcquisition(newProduct, _partners.get(partnerID), price, amount, true);
	}

	/**
	 * returns a String representation of a transaction
	 * 
	 * @param tID Transaction's ID
	 * 
	 * @return String representing a transaction
	 */
	public String getTransacation(int tID) throws UnknownTransactionException {
		if (!checkTransaction(tID))
			throw new UnknownTransactionException(tID);
		Transaction t = _transactions.get(tID);
		t.setStoreDate(_date);
		return t.toString();
	}

	/**
	 * Returns a string representing all the acquisitions with a given partner
	 * 
	 * @param pID partner's id
	 * 
	 * @return String representation of transactions
	 */
	public String getAcquisitionsWithPartner(String pID) throws UnknownPartnerException {
		if (!checkPartner(pID))
			throw new UnknownPartnerException(pID);

		VisitorTransaction visitor = new IsAcquisition();
		visitTransactions(visitor);

		List<Transaction> tList = visitor.getTransactions().stream().filter(tr -> tr.getPartner().getID().equals(pID))
									.collect(Collectors.toList());
		String info = "";
		for (Transaction t: tList)
			info += t.toString() + "\n";
		
		if (info.length() > 0)
			info = info.substring(0, info.length() - 1);


		return info;
	}

	/**
	 * Registers a new sale
	 * 
	 * @param partnerID partner's id
	 * @param productID product's id
	 * @param date payment deadline
	 * @param amount amount of product being bought
	 */
	public void registerSale(String partnerID, String productID, int date, int amount) 
		throws UnknownPartnerException, UnknownProductException, NoStockException {
		if (!checkProduct(productID)) 
			throw new UnknownProductException(productID);
		if (!checkPartner(partnerID)) 
			throw new UnknownPartnerException(partnerID);
		
		Product product = _products.get(productID);
		product.checkStock(amount);
		
		Partner partner = _partners.get(partnerID);
		
		double totalPrice = 0;
		double difference = amount - product.getStock();
		double basePrice = product.priceToPay();
		if (difference > 0) {
			totalPrice = product.getMinPrice() * (product.getStock());

			totalPrice += product.priceToPay() * difference;
		
			basePrice = totalPrice / amount;
		}
		product.update(-amount);
				
		partner.setAcquisitionsValue(partner.getAcquisitionsValue() + amount * basePrice);

		int id = _transactions.size();

		Sale newSale = new Sale(id, product, _partners.get(partnerID),
									amount, basePrice, date);
		

		_transactions.put(id, newSale);
	}

	/**
	 * Receive a payment from a partner on a sale
	 * 
	 * @param tID transaction ID
	 */
	public void receivePayment(int tID) throws UnknownTransactionException {
		if (!checkTransaction(tID))
			throw new UnknownTransactionException(tID);
		VisitorTransaction makePayment = new MakePayment();

		Transaction transaction = _transactions.get(tID);
		transaction.setStoreDate(_date);

		transaction.accept(makePayment);
	}

	/**
	 * Returns all the transaction paid by a partner
	 * 
	 * @param pID Partner's ID
	 */
	public String lookupPaymentsByPartner(String pID) throws UnknownPartnerException {
		if (!checkPartner(pID))
			throw new UnknownPartnerException(pID);

		VisitorTransaction visitor = new Paid(pID);
		visitTransactions(visitor);

		return strOfTransactions(visitor.getTransactions());
	}

	/**
	 * Returns a string will all the sales and breakdowns of a given partner
	 * 
	 * @param pID Partner's ID
	 * 
	 * @return string
	 */
	public String getPartnerSalesAndBreakdowns(String pID) throws UnknownPartnerException {
		if (!checkPartner(pID))
			throw new UnknownPartnerException(pID);

			VisitorTransaction visitor = new SalesAndBreakdowns(pID);
			visitTransactions(visitor);

		return strOfTransactions(visitor.getTransactions());
	}

	/**
	 * Returns the strings of a given list of transactions
	 * 
	 * @param transactions List of Transaction
	 * 
	 * @return string
	 */
	public String strOfTransactions(List<Transaction> transactions) {
		String info = "";
		for (Transaction t: transactions) {
			t.setStoreDate(_date);
			info += t.toString() + '\n';
		}

		if (info.length() > 0)
			info = info.substring(0, info.length() - 1);

		return info;
	}

	/**
	 * Registers a new breakdown
	 * 
	 * @param partnerID Partner's ID
	 * @param productID Product's ID
	 * @param amount amount of product
	 */
	public void registerBreakdown(String partnerID, String productID, int amount)
		throws UnknownPartnerException, UnknownProductException, NoStockException {
		if (!checkPartner(partnerID))
			throw new UnknownPartnerException(partnerID);
		
		if (!checkProduct(productID))
			throw new UnknownProductException(productID);
			
		Product product = _products.get(productID);
		if (product.getStock() < amount) 
			throw new NoStockException(product.getID(), product.getStock(), amount);

		VisitorProduct visitor = new Break(amount, _transactions.size(), _partners.get(partnerID));

		product.accept(visitor);

		if (visitor.getTransaction() != null)
			_transactions.put(_transactions.size(), visitor.getTransaction());
	}
}
