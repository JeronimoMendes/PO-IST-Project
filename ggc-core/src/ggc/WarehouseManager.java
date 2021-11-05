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
		return _warehouse.getBatchesOfProduct(pID);
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
	 * returns a String representation of a transaction
	 * 
	 * @param tID Transaction's ID
	 * 
	 * @return String representing a transaction
	 */
	public String getTransacation(int tID) throws UnknownTransactionException {
		return _warehouse.getTransacation(tID);
	}
}
