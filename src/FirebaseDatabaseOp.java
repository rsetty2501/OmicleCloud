import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.Semaphore;

import org.apache.log4j.BasicConfigurator;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDatabaseOp {
	
	private Map<String, LinkedList<DepotUserLocatn>> mapClusterOptRoute = new HashMap<>();
	private Map<String, Metrics> mapRouteMetrics = new HashMap<>();
	
	public FirebaseDatabaseOp(Map<String, LinkedList<DepotUserLocatn>> mapClusterOptRoute, Map<String, Metrics> mapRouteMetrics) {
		this.mapClusterOptRoute = mapClusterOptRoute;
		this.mapRouteMetrics = mapRouteMetrics;
	}
	
	public void setUpFierbaseSDK() {
		
		BasicConfigurator.configure();
		
		// Firebase admin test
		FileInputStream serviceAccount = null;
		try {
			serviceAccount = new FileInputStream("C:/Users/Rahul Setty/Documents/sdkadmintest-firebase-adminsdk-opurj-49457dc294.json");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		// Testing the uid read and write access in Firebase database
		Map<String, Object> auth = new HashMap<String, Object>();
		auth.put("uid", "admin");


		FirebaseOptions options = null;
		try {
			options = new FirebaseOptions.Builder()
			    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
			    .setDatabaseUrl("https://sdkadmintest.firebaseio.com/")
			    .setDatabaseAuthVariableOverride(auth)
			    .build();
		} catch (IOException e) {
			e.printStackTrace();
		}

		FirebaseApp.initializeApp(options);
		
	}
	
	public void pushDataInFirebase() {
		SimpleDateFormat formatter;  
	    Date date; 
	    String currDate = null;
	    String city = null;
	    
	    formatter = new SimpleDateFormat("dd-MM-yyyy");  
	    date = new Date(); 
	    currDate = formatter.format(date);
	    
   
	    // Create a database reference
	    final FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference databaseRef;
		city = "Uppsala";
		databaseRef = database.getReference(city);
		DatabaseReference usersRef = databaseRef.child(currDate);
		DatabaseReference clusterRef = databaseRef.child(currDate).child("Clusters");
		
		// Push the cluster details in the firebase
		for(Map.Entry<String, LinkedList<DepotUserLocatn>> entry : mapClusterOptRoute.entrySet()) {
			String cluster = entry.getKey();
			LinkedList<DepotUserLocatn> dUL = entry.getValue();
			System.out.println("Size list : " + dUL.size());
			DatabaseReference ref = clusterRef.child(cluster);
			
			for(int i = 0; i < dUL.size(); i++) {
				writeUser(ref,dUL.get(i));
			}
		}
		
		// Push the distance details
		for(Map.Entry<String, Metrics> entry : mapRouteMetrics.entrySet()) {
			String cluster = entry.getKey();
			Metrics metric = entry.getValue();
			DatabaseReference ref = usersRef.child("Distance");
			
			Map<String, Double> clustDis = new HashMap<>();
			clustDis.put(cluster, metric.getDistance());
			writeMetrics(ref,clustDis);
		}
		
		// Push the duration details
		for(Map.Entry<String, Metrics> entry : mapRouteMetrics.entrySet()) {
			String cluster = entry.getKey();
			Metrics metric = entry.getValue();
			DatabaseReference ref = usersRef.child("Time");
					
			Map<String, Double> clustTime = new HashMap<>();
			clustTime.put(cluster, metric.getTime());
			writeMetrics(ref,clustTime);
		}
		
	}
	
	private void writeMetrics(DatabaseReference usersRef, Map<String, Double> metric) {
		
		final Semaphore semaphore = new Semaphore(0);
		String key = usersRef.push().getKey();
		usersRef.child(key).setValue(metric, new DatabaseReference.CompletionListener() {
			
			@Override
			public void onComplete(DatabaseError arg0, DatabaseReference arg1) {
				semaphore.release();
			}
		});

		try {
			semaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
	}

	private void writeUser(DatabaseReference usersRef, DepotUserLocatn depotUser) {
		
		final Semaphore semaphore = new Semaphore(0);
		String key = usersRef.push().getKey();
		usersRef.child(key).setValue(depotUser, new DatabaseReference.CompletionListener() {
			
			@Override
			public void onComplete(DatabaseError arg0, DatabaseReference arg1) {
				semaphore.release();
			}
		});

		try {
			semaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
	}
	
	
}
