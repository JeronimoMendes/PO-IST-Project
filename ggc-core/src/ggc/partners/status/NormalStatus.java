package ggc.partners.status;

import ggc.partners.Partner;
import ggc.transactions.Sale;

public class NormalStatus extends Status {
	private static final long serialVersionUID = 202110272100L;
	
	public NormalStatus(Partner partner) {
		super(partner);
	}

	@Override
	public String toString() {
		return "NORMAL";
	}

	public double p2Price(int interval) {
		return 1;
	}

	public double p3Price(int interval) {
		return 1 - 0.05 * interval;
	}

	public double p4Price(int interval) {
		return 1 - 0.10 * interval;
	};

	public void pay(Sale sale) {
		int interval = sale.getDateInterval();
		Partner partner = getPartner();

		if (interval >= 0) {
			partner.setScore(partner.getScore() + 10 * (int)(sale.getTotalPrice()));
		}
		if (partner.getScore() > 25000) {
			partner.setStatus(new EliteStatus(partner));
		} else if (partner.getScore() > 2000) {
			partner.setStatus(new SelectionStatus(partner));
		}
	}
}
