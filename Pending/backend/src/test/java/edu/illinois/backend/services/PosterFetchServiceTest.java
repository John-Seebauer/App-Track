package edu.illinois.backend.services;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by John Seebauer (seebaue2) on 12/5/16.
 */
public class PosterFetchServiceTest {
	PosterFetchService service;
	
	@Before
	public void setUp() {
		service = new PosterFetchService();
	}
	
	@Test
	public void getURLString() throws Exception {
		System.out.println(service.getURLString(3332844));
	}
	
}