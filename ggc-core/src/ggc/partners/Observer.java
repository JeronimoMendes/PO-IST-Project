package ggc.partners;

import java.io.Serializable;

public interface Observer extends Serializable {
	/**
	 * Adds a new notification to the Observer's notification array.
	 * @param event description of the notification
	 * @param pID product's ID
	 */
	public void update(String event, String pID);
}
