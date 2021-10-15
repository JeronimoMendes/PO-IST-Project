package ggc;

import ggc.exceptions.*;
import java.io.IOException;
import java.io.Serializable;
import java.io.BufferedReader;
import java.io.FileReader;

import java.util.regex.Pattern;
import java.util.TreeMap;
import java.util.Map;

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
	private Map<String, Partner> _partners = new TreeMap<String, Partner>(String.CASE_INSENSITIVE_ORDER);

	/** Warehouse's batches */
	private Map<String, Batch> _batches = new TreeMap<String, Batch>(String.CASE_INSENSITIVE_ORDER);

	/** Warehouse's product */
	private Map<String, Product> _products = new TreeMap<String, Product>(String.CASE_INSENSITIVE_ORDER);

	// FIXME define contructor(s)
	// FIXME define methods

	/**
	 * ################################# File #################################
	 */

	/**
	 * @param txtfile filename to be loaded.
	 * @throws IOException
	 * @throws BadEntryException
	 */
	void importFile(String txtfile) throws IOException, BadEntryException /* FIXME maybe other exceptions */ {
		// TODO: Add exceptions that come from parseline()
		
		BufferedReader input = new BufferedReader(new FileReader(txtfile));

		String line;
		while ((line = input.readLine()) != null) {
			String fields[] = line.split("\\|");
			parseLine(fields);
		}

		input.close();
	}

	void parseLine(String fields[]) {
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
	 * ############################### Partners ###############################
	 */

	/**
	 * Create new partner
	 * 
	 * 
	*/
	void registerPartner(String id, String name, String Address) {
		Partner newPartner = new Partner(id, name, Address);
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

		return info;
	}

	/**
	 * Checks if a given partner's key corresponds to a registered partner
	 */
	boolean checkPartner(String partnerKey) {
		return _partners.containsKey(partnerKey);
	}

	/**
	 * ############################### Product & Batches ###############################
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
		_batches.put(pID, newBatch);
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
			registerProduct(pID, recipe);
		}
		
		Batch newBatch = new Batch(sID, pID, stock, price);
		_batches.put(pID, newBatch);
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
	}

	/**
	 * Registers a composed Product
	 * 
	 * @param pID Product's ID
	 */
	void registerProduct(String pID, String recipe) {
		// TODO: parse the recipe

		Product newProduct = new Product(pID);
	}

}
