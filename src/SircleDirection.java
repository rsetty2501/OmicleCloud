import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class SircleDirection {

	private double src_latitude;
	private double src_longitude;
	private double des_latitude;
	private double des_longitude;
	private static final String REQUEST_DISTANCE_URL = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=";
	private Metrics metric = null;
	

	public SircleDirection(double src_latitude, double src_longitude, double des_latitude, double des_longitude) {
		this.src_latitude = src_latitude;
		this.src_longitude = src_longitude;
		this.des_latitude = des_latitude;
		this.des_longitude = des_longitude;
	}

	public Metrics getDistanceTimeCoord() {

		// Create URL object
		URL url = createUrl(REQUEST_DISTANCE_URL);

		// Perform HTTP request to the URL and receive a JSON response back
		String jsonResponse = "";
		try {
			jsonResponse = makeHttpRequest(url);
		} catch (IOException e) {
			e.printStackTrace();
		}

		metric = QueryDistanceTimeData.extractDistanceTime(jsonResponse);
		return metric;

	}

	private URL createUrl(String stringUrl) {
		URL url = null;
		try {
			url = new URL(stringUrl + src_latitude + "," + src_longitude + "&destinations=" + des_latitude + ","
					+ des_longitude + "&key=AIzaSyAcpn4zmrc7yjeXDpv5Gv-lLkAZqYDxoOg");
		} catch (MalformedURLException exception) {
			exception.printStackTrace();
			return null;
		}
		return url;
	}

	/**
	 * Make an HTTP request to the given URL and return a String as the response.
	 */
	private String makeHttpRequest(URL url) throws IOException {
		String jsonResponse = "";
		HttpURLConnection urlConnection = null;
		InputStream inputStream = null;
		try {
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setReadTimeout(10000 /* milliseconds */);
			urlConnection.setConnectTimeout(15000 /* milliseconds */);
			urlConnection.connect(); // This is where we actually establish a connection
			inputStream = urlConnection.getInputStream();
			jsonResponse = readFromStream(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// handles exception and used to clean up regardless of what happens in the try
			// block
			// Major use : closing files, release system resources such as database/network
			// connections etc
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
			if (inputStream != null) {
				inputStream.close();
			}
		}
		return jsonResponse;
	}

	/**
	 * Convert the {@link InputStream} into a String which contains the whole JSON
	 * response from the server.
	 */
	private String readFromStream(InputStream inputStream) throws IOException {
		StringBuilder output = new StringBuilder();
		if (inputStream != null) {
			// The input stream reader converts the raw data into human readable chars
			// It allows to extract data one at a time which will take a lot of time. This
			// can be avoided by
			// wrapping the InputStreamReader in a buffered reader
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
			BufferedReader reader = new BufferedReader(inputStreamReader);
			String line = reader.readLine();
			while (line != null) {
				output.append(line);
				line = reader.readLine();
			}
		}
		return output.toString();
	}

}
