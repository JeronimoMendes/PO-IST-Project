package ggc.partners.notifications;

import java.io.Serializable;

public interface NotificationMode extends Serializable {
	public Notification notify(String event, String pID);
}
