package edu.illinois.util;

import java.util.Objects;
import java.util.logging.Logger;

/**
 * Created by John Seebauer (seebaue2) on 9/24/16.
 */
public class Triple<T1, T2, T3> {
	private final static Logger logger = Logger.getLogger(Triple.class.getName());
	private final T1 arg1;
	private final T2 arg2;
	private final T3 arg3;
	
	public Triple(T1 arg1, T2 arg2, T3 arg3) {
		
		this.arg1 = arg1;
		this.arg2 = arg2;
		this.arg3 = arg3;
	}
	
	public T1 getOne() {
		return arg1;
	}
	
	public T2 getTwo() {
		return arg2;
	}
	
	public T3 getThree() {
		return arg3;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Triple)) return false;
		Triple<?, ?, ?> triple = (Triple<?, ?, ?>) o;
		return Objects.equals(arg1, triple.arg1) &&
				Objects.equals(arg2, triple.arg2) &&
				Objects.equals(arg3, triple.arg3);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(arg1, arg2, arg3);
	}

}
