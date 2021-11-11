package ggc.partners.status;

import java.io.Serializable;

import ggc.partners.Partner;
import ggc.transactions.Sale;
import ggc.transactions.Breakdown;

public abstract class Status implements Serializable{
	private static final long serialVersionUID = 202110272100L;

	private Partner _partner;

	public Status(Partner partner) {
		_partner = partner;
	}

	public Partner getPartner() {
		return _partner;
	}

	public double p1Price() {
		return 0.9;
	};

	public abstract double p2Price(int gap);

	public abstract double p3Price(int gap);

	public abstract double p4Price(int gap);

	public abstract void pay(Sale sale);

	public void pay(Breakdown breakdown) {
		_partner.setScore(_partner.getScore() + 10 * (int)breakdown.getTotalPrice());
	}
}
