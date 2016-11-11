package edu.illinois.util;

import java.util.Objects;
import java.util.logging.Logger;

/**
 * Created by John Seebauer (seebaue2) on 9/24/16.
 */
public class Pair<T1, T2> {
	private final static Logger logger = Logger.getLogger(Pair.class.getName());
	private final T1 arg1;
	private final T2 arg2;
	
	public Pair(T1 arg1, T2 arg2) {
		
		this.arg1 = arg1;
		this.arg2 = arg2;
	}
	
	public T1 getOne() {
		return arg1;
	}
	
	public T2 getTwo() {
		return arg2;
	}
	
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Pair)) return false;
		Pair<?, ?> pair = (Pair<?, ?>) o;
		return Objects.equals(arg1, pair.arg1) &&
				Objects.equals(arg2, pair.arg2);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(arg1, arg2);
	}
}
