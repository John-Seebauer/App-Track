package edu.illinois.backend.services;


import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by John Seebauer (seebaue2) on 12/5/16.
 */
public class PosterFetchService {
	
	
	public String getURLString(Integer movie_id) {
		try {
			URL url = new URL(String.format("http://www.omdbapi.com/?i=%d", movie_id));
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			BufferedReader stream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			StringBuilder builder = new StringBuilder();
			String line;
			while ((line = stream.readLine()) != null) {
				builder.append(line);
			}
			stream.close();
			
			InputSource inputSource = new InputSource(new StringReader(builder.toString()));
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			saxParserFactory.setNamespaceAware(true);
			SAXParser parser = saxParserFactory.newSAXParser();
			
			XMLReader xmlReader = parser.getXMLReader();
			
			
			SAXTagHandler handler = new SAXTagHandler();
			xmlReader.setContentHandler(handler);
			xmlReader.parse(inputSource);
			
			return handler.getPosterURL();
		} catch (IOException | SAXException | ParserConfigurationException e) {
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
		
		public String getPosterURL(String title, Integer year) {
			String url = "http://www.omdbapi.com/?t=";
			String[] splitTitle = title.split(" ");
			
			if (splitTitle.length > 1) {
				//concatenate with plus signs
			}
			return posterURL;
		}
	}
}


