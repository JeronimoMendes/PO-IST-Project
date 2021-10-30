package ggc.partners.notifications;

import java.io.Serializable;

public class Notification implements Serializable {
	private String _event;
	private String _pID;

	public Notification(String event, String pID) {
		_event = event;
		_pID = pID;
	}
}
