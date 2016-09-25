package edu.illinois.util;

/**
 * Created by John Seebauer (seebaue2) on 9/24/16.
 */
public class Triple<T1, T2, T3> {
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
	
}
