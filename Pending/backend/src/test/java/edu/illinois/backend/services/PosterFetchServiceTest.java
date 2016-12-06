package edu.illinois.backend.services;

import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;

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
		String url = service.getURLString("Mean Girls", 2004);
		System.out.println(url);
		BufferedImage read = ImageIO.read(new URL(url));
		
	}
	
}