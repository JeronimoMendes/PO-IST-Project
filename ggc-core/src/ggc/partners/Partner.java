package ggc.partners;

import java.io.Serializable;


public class Partner implements Serializable {
	/** Unique id of Partner */
	private String _id;

	/** Name of Partner */
	private String _name;

	/** Address of Partner */
	private String _address;

	/** Transaction score of Partner */
	private int _score = 0;

	/**
	 * Partner's main constructor
	 * 
	 * @param id unique id
	 * 
	 * @param name name
	 * 
	 * @param address address 
	 */
	public Partner(String id, String name, String address) {
		super();
		_id = id;
		_name = name;
		_address = address;
	}

	@Override
	public String toString() {
		return "PARTNER|" + _id + "|" + _name + "|" + _address;
	}
}
