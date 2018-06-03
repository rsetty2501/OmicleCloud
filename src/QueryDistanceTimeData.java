import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class QueryDistanceTimeData {
	
	private QueryDistanceTimeData() {
		
	}
	
	public static Metrics extractDistanceTime(String jsonResponse) {
		
		String status = null;
		String distance = null;
		String time = null;
		
		double Distance = 0.0;
		double Time = 0.0;
		Metrics metric = null;
		
		try {

            // 1. Create a root JSONObject
            JSONObject root = new JSONObject(jsonResponse);
            status = root.getString("status");
            if(status.equalsIgnoreCase("OK")) {
            	JSONArray rows = root.getJSONArray("rows"); 
                JSONObject zero = rows.getJSONObject(0);
                JSONArray element = zero.getJSONArray("elements");
                JSONObject zero2 = element.getJSONObject(0);
                JSONObject dist = zero2.getJSONObject("distance");
                distance = dist.getString("text");
                System.out.println("Distance: " + distance);
                
                if(distance.contains("km")) {
                	distance = distance.replaceAll("km", "");
                	Distance = Double.parseDouble(distance);
                }
                else {
                	distance = distance.replaceAll("m", "");
                    Distance = Double.parseDouble(distance);
                    Distance = Distance/1000;
                }
                
                JSONObject duration = zero2.getJSONObject("duration");
                time = duration.getString("text");
//                System.out.println("Duration: " + time);
                time = time.replaceAll("mins", "");
                time = time.replaceAll("min", "");
                Time = Double.parseDouble(time);
                
                metric = new Metrics(Distance, Time);
                             
            }
            else {
            	metric = new Metrics(0.0, 0.0);
                
            }
            
            
		} catch(JSONException e) {
			e.printStackTrace();
		}
		return metric;
	}

}
