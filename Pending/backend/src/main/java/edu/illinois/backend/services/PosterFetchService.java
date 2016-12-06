package edu.illinois.backend.services;


import com.vaadin.server.StreamResource;
import com.vaadin.ui.Image;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by John Seebauer (seebaue2) on 12/5/16.
 */
public class PosterFetchService {
	
	
	public String getURLString(String movieName, Integer year) {
		try {
			URL url = new URL(formatPoserURL(movieName, year));
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			BufferedReader stream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			StringBuilder builder = new StringBuilder();
			String line;
			while ((line = stream.readLine()) != null) {
				builder.append(line);
			}
			stream.close();
			
			/*InputSource inputSource = new InputSource(new StringReader(builder.toString()));
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			saxParserFactory.setNamespaceAware(true);
			SAXParser parser = saxParserFactory.newSAXParser();
			
			XMLReader xmlReader = parser.getXMLReader();
			
			
			SAXTagHandler handler = new SAXTagHandler();
			xmlReader.setContentHandler(handler);
			xmlReader.parse(inputSource);*/
			
			String real = builder.toString();
			Integer index = real.indexOf("poster=");
			index = index + 8;
			Integer last = real.indexOf("\"", index);
			
			
			return real.substring(index, last);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	private String formatPoserURL(String title, Integer year) {
		try {
			return String.format("http://www.omdbapi.com/?t=%s&y=%d&r=xml", URLEncoder.encode(title, "UTF-8"), year);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	private class SAXTagHandler extends DefaultHandler {
		
		private boolean poster = false;
		private String posterURL = "";
		
		public void startElement(String uri, String localName, String qName,
		                         Attributes attributes) throws SAXException {
			
			System.out.println("Start Element :" + qName);
			
			if (qName.equalsIgnoreCase("Poster")) {
				poster = true;
			}
		}
		
		public void endElement(String uri, String localName,
		                       String qName) throws SAXException {
			
			System.out.println("End Element :" + qName);
			
		}

		public void characters(char ch[], int start, int length) throws SAXException {

			if (poster) {
				posterURL = new String(ch, start, length);
				System.out.println("Poster url: " + posterURL);
				poster = false;
			}
		}
		
		String getPosterURL() {
			return posterURL;
		}
	}
	
	public Image getImage(BufferedImage image) {
		return new Image("", new StreamResource((StreamResource.StreamSource) () -> {
			try {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ImageIO.write(image, "png", bos);
				return new ByteArrayInputStream(bos.toByteArray());
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}, "poster.png"));
	}
}


