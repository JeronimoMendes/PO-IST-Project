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
		else if (simpleBatch.matcher(fields[0]).matches()) {}
		else if (complexBatch.matcher(fields[0]).matches()) {}
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
	 * ################################# File #################################
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

	String listPartners() {
		String list = "";

		for(Map.Entry<String, Partner> entry : _partners.entrySet()) {
				list += entry.getValue().toString();
				list += "\n";
		}

		return list;
	}
}
