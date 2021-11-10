package ggc;

import ggc.exceptions.*;
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

import ggc.partners.Partner;
import ggc.products.Batch;
import ggc.products.Product;
import ggc.products.ComposedProduct;
import ggc.partners.Observer;
import ggc.transactions.Transaction;
import ggc.util.AccountingBudget;
import ggc.util.ReadyBudget;
import ggc.util.IsAcquisition;
import ggc.util.MakePayment;
import ggc.transactions.Transaction;
import ggc.util.Visitor;
import ggc.util.Paid;
import ggc.transactions.Acquisition;
import ggc.transactions.Sale;

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

	/** Warehouse's batches */
	private List<Batch> _batches = new ArrayList<Batch>();
	
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
	void importFile(String txtfile) throws IOException, BadEntryException, DuplicatePartnerException {
		// TODO: Add exceptions that come from parseline()
		
		BufferedReader input = new BufferedReader(new FileReader(txtfile));

		String line;
		while ((line = input.readLine()) != null) {
			String fields[] = line.split("\\|");
			parseLine(fields);
		}

		input.close();
	}

	void parseLine(String fields[]) throws DuplicatePartnerException {
		// TODO: Add exceptions that come from the different objects creations

		Pattern partner = Pattern.compile("^(PARTNER)");
		Pattern simpleBatch = Pattern.compile("^(BATCH_S)");
		Pattern complexBatch = Pattern.compile("^(BATCH_M)");
		Pattern sale = Pattern.compile("^(VENDA)");
		Pattern buy = Pattern.compile("^(COMPRA)");
		Pattern breakdown = Pattern.compile("^(DESAGREGAÇÃO)");

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
		Visitor visitor = new ReadyBudget();
		visitTransactions(visitor);
		return visitor.getBudget();
	}

	/**
	 * Returns the accounting budget of the warehouse
	 * 
	 * @return Warehouse's accounting budget
	 */
	public double getAccountingBudget() {
		Visitor visitor = new AccountingBudget();
		visitTransactions(visitor);
		return visitor.getBudget();
	}

	/**
	 * Visits the transactions with a visitor
	 */
	public void visitTransactions(Visitor visitor) {
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
	 * 
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

		info += partner.showPartner(); // + "\n";\

		// TODO: Include partner's notifications

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
		_batches.add(newBatch);
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
		_batches.add(newBatch);
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
	public Product registerProduct(String pID, double alpha, String recipe) {
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

		List<Batch> list = _batches.stream()
								.filter(batch -> batch.getProduct().equals(product.getID()) && batch.getStock() > 0)
								.collect(Collectors.toList());

		return list;
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

		List<Batch> list = _batches.stream()
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
		List<Batch> list = _batches.stream()
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
		return getStrOfBatches(_batches.stream().filter(b -> b.getStock() > 0)
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
	 */
	public void registerAcquisition(Product product, Partner partner, double price, int amount, boolean isProductNew) {
		int id = _transactions.size();

		Batch newBatch = new Batch(partner.getID(), product.getID(), amount, price);
		_batches.add(newBatch);

		product.update(price, amount, isProductNew);

		Acquisition accq = new Acquisition(id, product, partner, amount, price, _date);

		_transactions.put(id, accq);
	}

	/**
	 * Register a new simple Product and Transaction
	 */
	public void registerProductInAcquisition(String product, String partner, double price, int amount) {
		Product newProduct = registerProduct(product);

		registerAcquisition(newProduct, _partners.get(partner), price, amount, true);
	}

	/**
	 * Register a new complex Product and Transaction
	 */
	public void registerProductInAcquisition(String product, String partner, double price,
												int amount, String recipe, double alpha) {
		Product newProduct = registerProduct(product, alpha, recipe);

		registerAcquisition(newProduct, _partners.get(partner), price, amount, true);
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

		Visitor visitor = new IsAcquisition();
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
		if (product.getStock() < amount) {
			throw new NoStockException(product.getStock(), amount);
		}
		
		Partner partner = _partners.get(partnerID);
		
		product.update(-amount);
		updateBatches(product, amount);
		
		double basePrice = product.getMinPrice();
		
		partner.setAcquisitionsValue(partner.getAcquisitionsValue() + amount * basePrice);

		int id = _transactions.size();

		Sale newSale = new Sale(id, product, _partners.get(partnerID),
									amount, basePrice, date);
		

		_transactions.put(id, newSale);
	}

	/**
	 * Updates batches stock when a product is bought
	 * 
	 * @param product Product thats being sold
	 * @param amount units of the product being sold
	 */
	public void updateBatches(Product product, int amount) {
		List<Batch> batches = getBatchesOfProduct(product);

		Collections.sort(batches, new Batch.PriceComparator());

		for (Batch batch: batches) {
			int batchStock = batch.getStock();
			if (batchStock - amount < 0) {
				batch.setStock(0);
				amount = -(batchStock - amount);
			} else {
				batch.setStock(batchStock - amount);
				break;
			}
		}
	}

	/**
	 * Receive a payment from a partner on a sale
	 * 
	 * @param tID transaction ID
	 */
	public void receivePayment(int tID) throws UnknownTransactionException {
		if (!checkTransaction(tID))
			throw new UnknownTransactionException(tID);
		Visitor makePayment = new MakePayment();

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

		Visitor visitor = new Paid();
		visitTransactions(visitor);
		List<Transaction> transactions = visitor.getTransactions().stream()
										.filter(t -> t.getPartner().getID() == pID)
										.collect(Collectors.toList());

		String info = "";
		for (Transaction t: transactions) {
			info += t.toString() + '\n';
		}

		if (info.length() > 0)
			info = info.substring(0, info.length() - 1);

		return info;
	}
}
