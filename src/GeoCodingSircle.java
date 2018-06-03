import java.net.URLConnection;
import java.net.URLEncoder;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;

public class GeoCodingSircle {

	private static final String URL = "https://maps.googleapis.com/maps/api/geocode/json";
	
	
	public ResponseFromGoogle convertAddresstoLatLon(String address) throws IOException, UnsupportedEncodingException {
		  
		URL url = new URL(URL + "?address="
				    + URLEncoder.encode(address, "UTF-8") + "&sensor=false&key=<API-KEY>");
		
		
		  // Open the Connection
		  URLConnection conn = url.openConnection();

		  InputStream in = conn.getInputStream() ;
		  ObjectMapper mapper = new ObjectMapper();
		  ResponseFromGoogle response = (ResponseFromGoogle)mapper.readValue(in,ResponseFromGoogle.class);
		  in.close();
		  return response;
	}
}
