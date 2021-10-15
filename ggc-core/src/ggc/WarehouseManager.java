package ggc;

import ggc.exceptions.*;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;



//FIXME import classes (cannot import from pt.tecnico or ggc.app)

/** Façade for access. */
public class WarehouseManager {

	/** Name of file storing current store. */
	private String _filename = "";


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
	public void save() throws IOException, FileNotFoundException, MissingFileAssociationException {
		//FIXME implement serialization method
	}

	/**
	 * @@param filename
	 * @@throws MissingFileAssociationException
	 * @@throws IOException
	 * @@throws FileNotFoundException
	 */
	public void saveAs(String filename) throws MissingFileAssociationException, FileNotFoundException, IOException {
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
		} catch (IOException | BadEntryException /* FIXME maybe other exceptions */ e) {
			throw new ImportFileException(textfile);
		}
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
}
