package ggc.partners.status;

import ggc.partners.Partner;
import ggc.transactions.Sale;

public class EliteStatus extends Status {
	private static final long serialVersionUID = 202110272100L;
	
	public EliteStatus(Partner partner) {
		super(partner);
	}

	@Override
	public String toString() {
		return "ELITE";
	}

	public double p2Price(int interval) {
		return 0.90;
	}

	public double p3Price(int interval) {
		return 0.95;
	}

	public double p4Price(int interval) {
		return 1;
	};

	public void pay(Sale sale) {
		int interval = sale.getDateInterval();
		Partner partner = getPartner();

		if (interval >= 0) {
			partner.setScore(partner.getScore() + 10 * (int)(sale.getTotalPrice()));
		}

		if (interval < -15) {
			partner.setScore(partner.getScore() / 4);
			partner.setStatus(new SelectionStatus(partner));
		}
	}
}
