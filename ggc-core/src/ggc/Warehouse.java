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

import ggc.partners.Partner;
import ggc.products.Batch;
import ggc.products.Product;
import ggc.products.ComposedProduct;


/**
 * Class Warehouse implements a warehouse.
 */
public class Warehouse implements Serializable {

	/** Serial number for serialization. */
	private static final long serialVersionUID = 202109192006L;

	// FIXME define attributes

	/** Warehouse's date */
	private int _date = 0;

	/** Warehouse's partners */
	private Map<String, Partner> _partners = new TreeMap<String, Partner>(new CollatorWrapper());

	/** Warehouse's batches */
	private Map<String, Batch> _batches = new TreeMap<String, Batch>(new CollatorWrapper());

	/** Warehouse's product */
	private Map<String, Product> _products = new TreeMap<String, Product>(new CollatorWrapper());
	
	// FIXME define contructor(s)
	// FIXME define methods

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
			registerSimpleBatch(fields[2], fields[1], stock, price);
		}
		else if (complexBatch.matcher(fields[0]).matches()) {
			int stock = Integer.parseInt(fields[4]);
			double price = Double.parseDouble(fields[3]);
			double alpha = Double.parseDouble(fields[5]);
			registerComplexBatch(fields[2], fields[1], stock, price, alpha, fields[6]);
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
	 * ########################################################################
	 * ############################### Partners ###############################
	 * ########################################################################
	 */

	/**
	 * Create new partner
	 * 
	 * 
	*/
	void registerPartner(String id, String name, String address) throws DuplicatePartnerException {
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
	String getPartner(String partnerKey) throws UnknownPartnerException {
		if (!checkPartner(partnerKey)) {
			throw new UnknownPartnerException(partnerKey);
		}
		
		String info = "";

		Partner partner = _partners.get(partnerKey);

		info += partner.toString(); // + "\n";\

		// TODO: Include partner's notifications

		return info;
	}

	/**
	 * Returns all the registered partners
	 * 
	 * @return partners list of all the partners
	 */
	String listPartners() {
		String info = "";

		for (String key: _partners.keySet()) {
			info += _partners.get(key).toString() + '\n';
		}

		return info; // remove the last \n
	}

	/**
	 * Checks if a given partner's key corresponds to a registered partner
	 */
	boolean checkPartner(String partnerKey) {
		return _partners.containsKey(partnerKey);
	}

	/**
	 * #################################################################################
	 * ############################### Product & Batches ###############################
	 * #################################################################################
	 */

	/**
	 * Registers a new Batch composed of a simple Product
	 * 
	 * @param sID Supplier's string ID
	 * @param pID Product's string ID
	 * @param stock number of units that will be sold in the batch
	 * @param price price of 1 unit of product
	 */
	void registerSimpleBatch(String sID, String pID, int stock, double price) {
		if (!productExists(pID)) {
			registerProduct(pID);
		}

		Batch newBatch = new Batch(sID, pID, stock, price);
		_batches.put(pID + sID + price + stock, newBatch);
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
	void registerComplexBatch(String sID, String pID, int stock, double price, double alpha, String recipe) {
		if (!productExists(pID)) {
			registerProduct(pID, alpha, recipe);
		}

		Batch newBatch = new Batch(sID, pID, stock, price);
		_batches.put(String.valueOf(_batches.size()), newBatch);
	}

	/**
	 * Checks if there's a registered product with a given ID		
	 * 
	 * @param pID Product's ID
	 * @return boolean 
	 */
	boolean productExists(String pID) {
		return _products.containsKey(pID);
	}

	/**
	 * Registers a simple Product
	 * 
	 * @param pID Product's ID
	 */
	void registerProduct(String pID) {
		Product newProduct = new Product(pID);
		_products.put(pID, newProduct);
	}

	/**
	 * Registers a composed Product
	 * 
	 * @param pID Product's ID
	 */
	void registerProduct(String pID, double alpha, String recipe) {
		// TODO: parse the recipe

		Product newProduct = new ComposedProduct(pID, alpha, recipe);
		_products.put(pID, newProduct);
	}

	/**
	 * Returns the list of all products
	 * 
	 * @return info String with info of all registered Products
	 */
	String listProducts() {
		String info = "";

		for (String key: _products.keySet()) {
			Product product = _products.get(key);

			List<Batch> batches = getBatchesOfProduct(product);
			product.setMaxPrice(getMaxPrice(batches));
			product.setStock(getStockOfProduct(batches));
			
			String productInfo = product.toString() + "\n";

			info += productInfo;
		}

		return info; // remove the last \n
	}

	/**
	 * Returns maximum price of list of batches
	 * 
	 * @param batches List of batches to iterate
	 * 
	 * @return max maximum price of the given batches
	 */
	double getMaxPrice(List<Batch> batches) {
		double max = 0;
		for (Batch batch: batches) {
			if (batch.getPrice() > max) {
				max = batch.getPrice();
			}
		}
		
		return max;
	}

	/**
	 * Returns the stock of a product in a given list of batches
	 * 
	 * @param batches List of batches
	 * 
	 * @return int stock
	 */
	int getStockOfProduct(List<Batch> batches) {
		int stock = 0;
		for (Batch batch: batches) {
			stock += batch.getStock();
		}
		
		return stock;
	}

	/**
	 * Returns an array of batchs of a given Product
	 * 
	 * @param product Product to filter the array of batchs
	 * 
	 * @return batches list of batches of the given product
	 */
	List<Batch> getBatchesOfProduct(Product product) {
		List<Batch> batches = new ArrayList();

		for (String key: _batches.keySet()) {
			Batch batch = _batches.get(key);
			Product batchProduct = _products.get(batch.getProduct());

			if (batchProduct.equals(product)) {
				batches.add(batch);
			}
		}

		return batches;
	}

	/**
	 * Returns list of all available batches (stock > 0)
	 * 
	 * @return String info of all available batches
	 */
	String listAvailableBatches() {
		String info = "";

		for (String key: _batches.keySet()) {
			Batch batch = _batches.get(key);
			if (batch.getStock() > 0) {
				info += batch.toString() + '\n';
			}
		}

		return info; // remove the last \n
	}

}
