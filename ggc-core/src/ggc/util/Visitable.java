package ggc.util;

import java.io.Serializable;

public interface Visitable<T> extends Serializable {
	void accept(T visitor);
}
