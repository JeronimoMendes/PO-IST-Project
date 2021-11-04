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
import ggc.transactions.Transaction;
import ggc.util.Visitor;

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
	public void registerProduct(String pID) {
		Product newProduct = new Product(pID, new ArrayList<Observer>(_partners.values()));
		_products.put(pID, newProduct);
	}

	/**
	 * Registers a composed Product
	 * 
	 * @param pID Product's ID
	 */
	public void registerProduct(String pID, double alpha, String recipe) {
		// TODO: parse the recipe

		Product newProduct = new ComposedProduct(pID, new ArrayList<Observer>(_partners.values()), alpha, recipe);
		_products.put(pID, newProduct);
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
	public String getBatchesOfProduct(String pID) throws UnknownProductException {
		if (!checkProduct(pID)) {
			throw new UnknownProductException(pID);
		}

		List<Batch> list = _batches.stream().filter(batch -> batch.getProduct().equals(pID))
											.collect(Collectors.toList());

		return getStrOfBatches(list);
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

		List<Batch> list = _batches.stream().filter(batch -> batch.getSupplier().equals(pID))
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
		List<Batch> list = _batches.stream().filter(batch -> batch.getPrice() < price)
											.collect(Collectors.toList());

		return getStrOfBatches(list);
	}

	/**
	 * Returns list of all available batches (stock > 0)
	 * 
	 * @return String info of all available batches
	 */
	public String listAvailableBatches() {
		return getStrOfBatches(_batches); // remove the last \n
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

}
