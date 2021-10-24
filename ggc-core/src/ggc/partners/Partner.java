package ggc.partners;

import java.io.Serializable;
import ggc.partners.status.Status;
import ggc.partners.status.NormalStatus;
import ggc.partners.status.SelectionStatus;
import ggc.partners.status.EliteStatus;


public class Partner implements Serializable {
	/** Unique id of Partner */
	private String _id;

	/** Name of Partner */
	private String _name;

	/** Address of Partner */
	private String _address;

	/** Transaction score of Partner */
	private int _score = 0;

	/** Status that distinguishs parterns */
	private Status _status = new NormalStatus();;

	/** Total value of accquisitions */
	private double _accquisitionsValue = 0;

	/** Total value of sales */
	private double _salesValue = 0;

	/** Total value of sales paid */
	private double _salesPaidValue = 0;

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
		String res = String.format(
			"%s|%s|%s|%s|%d|%d|%d|%d",
			_id, _name, _address,
			_status.toString(), _score,
			(int)_accquisitionsValue, (int)_salesValue, (int)_salesPaidValue
		);
		return res;
	}
}
