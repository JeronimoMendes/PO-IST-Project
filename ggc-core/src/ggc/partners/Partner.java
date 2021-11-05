package ggc.partners;

import java.io.Serializable;
import ggc.partners.status.Status;
import ggc.partners.status.NormalStatus;
import ggc.partners.status.SelectionStatus;
import ggc.partners.status.EliteStatus;

import ggc.partners.notifications.Notification;
import ggc.partners.notifications.NotificationMode;
import ggc.partners.notifications.Default;

import java.util.ArrayList;
import java.util.List;


public class Partner implements Observer {
	private static final long serialVersionUID = 202110272100L;
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

	/** Total value of acquisitions */
	private double _acquisitionsValue = 0;

	/** Total value of sales */
	private double _salesValue = 0;

	/** Total value of sales paid */
	private double _salesPaidValue = 0;

	/** List of notifications */
	private List<Notification> _notifications = new ArrayList<Notification>();

	/** Partner's prefered notification mode */
	private NotificationMode _notificationMode = new Default();

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
	public void update(String event, String pID) {
		_notifications.add(_notificationMode.notify(event, pID));
	}

	public String showPartner() {
		String res = toString() + '\n';

		for (Notification notification: _notifications) {
			res += notification.toString() + '\n';
		}

		if (res.length() > 0)
			res = res.substring(0, res.length() - 1);

		_notifications.clear();

		return res;
	}

	@Override
	public String toString() {
		String res = String.format(
			"%s|%s|%s|%s|%d|%d|%d|%d",
			_id, _name, _address,
			_status.toString(), _score,
			(int)_acquisitionsValue, (int)_salesValue, (int)_salesPaidValue
		);

		return res;
	}
}
