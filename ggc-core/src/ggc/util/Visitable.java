package ggc.util;

import java.io.Serializable;

public interface Visitable extends Serializable {
	void accept(Visitor visitor);
}
