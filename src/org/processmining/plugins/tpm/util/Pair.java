package org.processmining.plugins.tpm.util;

import org.processmining.plugins.tpm.parameters.TransitivePerformanceMinerParameters;

public class Pair<T, S> {
	
	private T _1;
	private S _2;
	
	
	public Pair(T _1, S _2) {
		this._1 = _1;
		this._2 = _2;
	}

	public T get_1() {
		return _1;
	}

	public void set_1(T _1) {
		this._1 = _1;
	}

	public S get_2() {
		return _2;
	}

	public void set_2(S _2) {
		this._2 = _2;
	}
	
	public boolean equals(Object object) {
		if (object instanceof Pair<?, ?>) {
			Pair<?, ?> anotherPair = (Pair<?, ?>) object;
			return get_1().equals(anotherPair.get_1()) &&
					get_2().equals(anotherPair.get_2());
		}
		return false;
	}
	
	@Override
	public String toString() {
		return String.format("<%s>: <%s>", get_1(), get_2());
	}
}
