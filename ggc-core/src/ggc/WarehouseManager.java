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



//FIXME import classes (cannot import from pt.tecnico or ggc.app)

/** Façade for access. */
public class WarehouseManager {

	/** Name of file storing current store. */
	public String _filename = "";


	/** The warehouse itself. */
	private Warehouse _warehouse = new Warehouse();

	//FIXME define other attributes
	//FIXME define constructor(s)
	//FIXME define other methods


	/**
	 * ################################# File #################################
	 */

	/**
	 * @@throws IOException
	 * @@throws FileNotFoundException
	 * @@throws MissingFileAssociationException
	 */
	public void save() throws IOException, MissingFileAssociationException {
		try {
			ObjectOutputStream output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(_filename)));
			output.writeObject(_warehouse);
			output.close();
		} catch(IOException e) {
			throw new MissingFileAssociationException();
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
		_warehouse.advanceDate(days);
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
		_warehouse.registerPartner(id, name, address);
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

}
