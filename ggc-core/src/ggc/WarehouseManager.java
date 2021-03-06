package ggc;

import ggc.exceptions.*;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.util.DuplicateFormatFlagsException;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import ggc.exceptions.DuplicatePartnerException;


/** Façade for access. */
public class WarehouseManager {

	/** Name of file storing current store. */
	private String _filename = "";

	/** The warehouse itself. */
	private Warehouse _warehouse = new Warehouse();

	/** A flag to keep track if the warehouse changed */
	private boolean _changed = true;

	/**
	 * ################################# File #################################
	 */

	/**
	 * @@throws IOException
	 * @@throws FileNotFoundException
	 * @@throws MissingFileAssociationException
	 */
	public void save() throws IOException, MissingFileAssociationException {
		// We only want to save if the warehouse has changed
		if (_changed) {
			try {
				ObjectOutputStream output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(_filename)));
				output.writeObject(_warehouse);
				output.close();
				_changed = false;
			} catch(IOException e) {
				throw new MissingFileAssociationException();
			}
		}
	}

	/**
	 * @@param filename
	 * @@throws MissingFileAssociationException
	 * @@throws IOException
	 * @@throws FileNotFoundException
	 */
	public void saveAs(String filename) throws MissingFileAssociationException, IOException {
		_filename = filename;
		save();
	}

	/**
	 * 	Loads a file from a previous session
	 * 
	 * @@param filename
	 * @@throws UnavailableFileException
	 */
	public void load(String filename) throws UnavailableFileException {
		try {
			ObjectInputStream input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)));
			_warehouse = (Warehouse) input.readObject();
			input.close();
			_filename = filename;
		} catch (IOException | ClassNotFoundException e) {
			throw new UnavailableFileException(filename);
		}
	}

	/**
	 * 	Imports a file to initialize the app
	 * 
	 * @param textfile
	 * @throws ImportFileException
	 */
	public void importFile(String textfile) throws ImportFileException {
		try {
			_warehouse.importFile(textfile);
		} catch (IOException | BadEntryException | DuplicatePartnerException /* FIXME maybe other exceptions */ e) {
			throw new ImportFileException(textfile);
		}
	}

	/**
	 * Returns _filename
	 * 
	 * @return filename String
	 */
	public String getFilename() {
		return _filename;
	}

	/**
	 *  ################################ Date ################################
	 */

	/**
	 * 	Returns warehouse's date
	 * 
	 * @return days warehouse's date
	 */
	public int getDate() {
		return _warehouse.getDate();
	}
	
	/**
	 * 	Advance warehouse's date
	 * 
	 * @param days number of days to advance
	 * @throws InvalidDaysException number of days was negative or null
	 */
	public void advanceDate(int days) throws InvalidDaysException{
		_changed = true;
		_warehouse.advanceDate(days);
	}

	/**
	 * Returns the accounting budget of the warehouse
	 * 
	 * @return Warehouse's accounting budget
	 */
	public double getAccountingBudget() {
		return _warehouse.getAccountingBudget();
	}  

	/**
	 * Returns the ready budget of the warehouse
	 * 
	 * @return Warehouse's ready budget
	 */
	public double getReadyBudget() {
		return _warehouse.getReadyBudget();
	}  
	

	/**
	 *  ###############################* Partners ###############################*
	 */

	/**
	 * Retrieves information about a partner
	 * 
	 * @param partnerKey partner's id
	 * 
	 * @return partnerInfo toString() of Partner
	 */
	public String getPartner(String partnerKey) throws UnknownPartnerException {
		return _warehouse.getPartner(partnerKey);
	}

	/**
	 * returns a list of products in the form of a String
	 * 
	 * @return String representing a list of products
	 */
	public String listPartners() {
		return _warehouse.listPartners();
	}

	/**
	 * Registers a partner in the _warehouse
	 */
	public void registerPartner(String id, String name, String address) throws DuplicatePartnerException {
		_changed = true;
		_warehouse.registerPartner(id, name, address);
	}

	/**
	 * Toggles product notifications of partner
	 * 
	 * @param partnerID partner's ID
	 * @param productID product's ID
	 */
	public void toggleNotifications(String partnerID, String productID)
			throws UnknownPartnerException, UnknownProductException {
		_warehouse.toggleNotifications(partnerID, productID);
	}

	/**
	 * ############################### Product & Batches ###############################
	 */

	/**
	 * returns a list of products in the form of a String
	 * 
	 * @return String representing a list of products
	 */
	public String listProducts() {
		return _warehouse.listProducts();
	}

	/**
	 * returns a list of batches in the form of a String
	 * 
	 * @return String representing a list of batches
	 */
	public String listAvailableBatches() {
		return _warehouse.listAvailableBatches();
	}

	/**
	 * returns a String representation of a list of batches of a particular product
	 * 
	 * @param pID Product's ID
	 * 
	 * @return String representing a list of batches
	 */
	public String getBatchesOfProduct(String pID) throws UnknownProductException {
		return _warehouse.getStrOfBatches(_warehouse.getBatchesOfProduct(pID));
	}

	/**
	 * returns a String representation of a list of batches of a particular Partner
	 * 
	 * @param pID Partner's ID
	 * 
	 * @return String representing a list of batches
	 */
	public String getBatchesOfPartner(String pID) throws UnknownPartnerException {
		return _warehouse.getBatchesOfPartner(pID);
	}

	/**
	 * returns a String representation of a list of batches under a certain price
	 * 
	 * @param pID Partner's ID
	 * 
	 * @return String representing a list of batches
	 */
	public String getBatchesUnderPrice(int price) {
		return _warehouse.getBatchesUnderPrice(price);
	}

	/**
	 * #################################################################################
	 * ################################# Transactions ##################################
	 * #################################################################################
	 */

	/**
	 * Register a new acquisition
	 * 
	 */
	public void registerAcquisition(String productID, String partnerID, double price, int amount)
		throws UnknownPartnerException, UnknownProductException {
		_warehouse.registerAcquisition(productID, partnerID, price, amount);
	}

	/**
	 * Register a new simple Product and Transaction
	 */
	public void registerProductInAcquisition(String product, String partner, double price, int amount) {
		_warehouse.registerProductInAcquisition(product, partner, price, amount);
	}

	/**
	 * Register a new complex Product and Transaction
	 */
	public void registerProductInAcquisition(String product, String partner,
											double price, int amount, String recipe, double alpha) {
		_warehouse.registerProductInAcquisition(product, partner, price, amount, recipe, alpha);
	}

	/**
	 * returns a String representation of a transaction
	 * 
	 * @param tID Transaction's ID
	 * 
	 * @return String representing a transaction
	 */
	public String getTransacation(int tID) throws UnknownTransactionException {
		return _warehouse.getTransacation(tID);
	}

	/**
	 * Returns a string representing all the acquisitions with a given partner
	 * 
	 * @param pID partner's id
	 * 
	 * @return String representation of transactions
	 */
	public String getAcquisitionsWithPartner(String pID) throws UnknownPartnerException {
		return _warehouse.getAcquisitionsWithPartner(pID);
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
		_warehouse.registerSale(partnerID, productID, date, amount);
	}

	/**
	 * Receive a payment from a partner on a sale
	 * 
	 * @param tID transaction ID
	 */
	public void receivePayment(int tID) throws UnknownTransactionException {
		_warehouse.receivePayment(tID);
	}

	/**
	 * Returns all the transaction paid by a partner
	 * 
	 * @param pID Partner's ID
	 */
	public String lookupPaymentsByPartner(String pID) throws UnknownPartnerException {
		return _warehouse.lookupPaymentsByPartner(pID);
	}

	/**
	 * Returns a string will all the sales and breakdowns of a given partner
	 * 
	 * @param pID Partner's ID
	 * 
	 * @return string
	 */
	public String showPartnerSalesAndBreakdowns(String pID) throws UnknownPartnerException {
		return _warehouse.getPartnerSalesAndBreakdowns(pID);
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
		_warehouse.registerBreakdown(partnerID, productID, amount);
	}
}
