package ggc.partners.notifications;

public class Default implements NotificationMode {
	@Override
	public Notification notify(String event, String pID) {
		return new Notification(event, pID);
	}
}
