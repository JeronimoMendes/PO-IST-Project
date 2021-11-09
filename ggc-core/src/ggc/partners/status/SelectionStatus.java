package ggc.partners.status;

import ggc.partners.Partner;
import ggc.transactions.Sale;


public class SelectionStatus extends Status {
	private static final long serialVersionUID = 202110272100L;
	
	public SelectionStatus(Partner partner) {
		super(partner);
	}

	@Override
	public String toString() {
		return "SELECTION";
	}

	public double p2Price(int interval) {
		return interval < 2 ? 1 : 0.95;
	}

	public double p3Price(int interval) {
		return interval < -1 ? 1 : 1 - 0.02 * interval;
	}

	public double p4Price(int interval) {
		return 1 - 0.05 * interval;
	};

	public void pay(Sale sale) {
		int interval = sale.getDateInterval();
		Partner partner = getPartner();

		if (interval >= 0) {
			partner.setScore(partner.getScore() + 10 * (int)sale.getTotalPrice());
		}

		if (interval < -2) {
			partner.setScore(partner.getScore() / 10);
			partner.setStatus(new NormalStatus(partner));
		}

		if (partner.getScore() > 25000) {
			partner.setStatus(new EliteStatus(partner));
		}
	}
}
