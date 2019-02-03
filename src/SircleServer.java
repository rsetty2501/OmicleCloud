import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import com.google.maps.errors.ApiException;

public class SircleServer {

	private static LinkedList<Integer> demandList = new LinkedList<>(); // for only the customers
	private static int vehicleCapacity = 200;

	private static LinkedList<DepotUserLocatn> depotUserLocList = new LinkedList<>();
	private static Map<String, Collection<Integer>> mapClusterNodes = new HashMap<>();
	private static Map<String, LinkedList<DepotUserLocatn>> mapClusterOptRoute = new HashMap<>();
	private static Map<String, Metrics> mapRouteMetrics = new HashMap<>();

	private static Map<String, LinkedList<DepotUserLocatn>> mapClusterOptRouteReverse = new HashMap<>();
	private static Map<String, Metrics> mapRouteMetricsReverse = new HashMap<>();

	public static void main(String args[]) throws ApiException, InterruptedException, IOException {

		LinkedList<DepotUserInfo> infoList = new LinkedList<>();
		LinkedList<LocationUpdated> locList = new LinkedList<>();
		Map<String, LinkedList<Integer>> updatedCusterNodes = new HashMap<>();
		
		long start = System.currentTimeMillis();
		createListUserDepotInfo(infoList);
		fillDemandUser(infoList);

		geoCodingService(infoList, locList);
		
		// Testing the threading in this
//		Thread thread1 = new Thread() {
//			public void run() {
//				sweepClustering(locList, updatedCusterNodes);
//			}
//		};
		
//		Thread thread2 = new Thread() {
//			public void run() {
//				sweepClusteringReverse(locList, updatedCusterNodes);
//			}
//		};
		
		//thread1.start();
		//thread2.start();
		
		//thread1.join();
		//thread2.join();

		sweepClustering(locList, updatedCusterNodes);

		sweepClusteringReverse(locList, updatedCusterNodes);

		compareSweepClusteringMetrics();
		
		long end = System.currentTimeMillis();
		long time = end - start;
		System.out.println("Time taken : " + time);

	}

	private static void compareSweepClusteringMetrics() {

		double sumSeg = 0.0, sumSegRev = 0.0;

		for (Map.Entry<String, Metrics> entry : mapRouteMetrics.entrySet()) {
			sumSeg = sumSeg + entry.getValue().getDistance();
		}

		for (Map.Entry<String, Metrics> entry : mapRouteMetricsReverse.entrySet()) {
			sumSegRev = sumSegRev + entry.getValue().getDistance();
		}

		if (sumSeg < sumSegRev) {
			pushClusterOptRouteData(mapClusterOptRoute, mapRouteMetrics);
		} else {
			pushClusterOptRouteData(mapClusterOptRouteReverse, mapRouteMetricsReverse);
		}

	}

	private static void printMetrics(Map<String, Metrics> metrics) {

		for (Map.Entry<String, Metrics> entry : metrics.entrySet()) {
			String c = entry.getKey();
			Metrics m = entry.getValue();
			System.out.println("Metrics : " + c + " : " + m.getDistance() + " " + m.getTime());
		}

	}

	private static void sweepClustering(LinkedList<LocationUpdated> locList,
			Map<String, LinkedList<Integer>> updatedCusterNodes) {

		mapClusterNodes = Clustering.sweepAlgoclustering(locList, demandList, vehicleCapacity);

		CustomerSegmentation customerSegment = new CustomerSegmentation(updatedCusterNodes, locList, mapClusterNodes,
				depotUserLocList);
		customerSegment.getOptData();
		mapRouteMetrics = customerSegment.getOptimizedRouteCluster();
		mapClusterOptRoute = customerSegment.mapClusterOptimizedRoutes();
		printMetrics(mapRouteMetrics);
	}

	private static void sweepClusteringReverse(LinkedList<LocationUpdated> locList,
			Map<String, LinkedList<Integer>> updatedCusterNodes) {

		mapClusterNodes = Clustering.sweepAlgoclusteringReverse(locList, demandList, vehicleCapacity);

		CustomerSegmentation customerSegment = new CustomerSegmentation(updatedCusterNodes, locList, mapClusterNodes,
				depotUserLocList);
		customerSegment.getOptData();
		mapRouteMetricsReverse = customerSegment.getOptimizedRouteCluster();
		mapClusterOptRouteReverse = customerSegment.mapClusterOptimizedRoutes();
		printMetrics(mapRouteMetricsReverse);
	}

	private static void pushClusterOptRouteData(Map<String, LinkedList<DepotUserLocatn>> mapCluster,
			Map<String, Metrics> metrics) {

		FirebaseDatabaseOp firebaseOp = new FirebaseDatabaseOp(mapCluster, metrics);
		firebaseOp.setUpFierbaseSDK();
		firebaseOp.pushDataInFirebase();

	}

	private static void fillDemandUser(LinkedList<DepotUserInfo> infoList) {

		for (int i = 0; i < infoList.size() - 1; i++) {
			demandList.add(40);
		}
	}

	private static void createListUserDepotInfo(LinkedList<DepotUserInfo> infoList) {

		DepotUserInfo depotuser1 = new DepotUserInfo("Kvarnbolund 9, 752 63 Uppsala", "Ragn-Sells", null);
		DepotUserInfo depotuser2 = new DepotUserInfo("Herrhagsv�gen 50, 752 67 Uppsala", "Patrick", "Yes"); // 1
		DepotUserInfo depotuser3 = new DepotUserInfo("Stenhuggarv�gen 46, 75267 Uppsala", "Micheal", "Yes"); // 2
		DepotUserInfo depotuser4 = new DepotUserInfo("Pimpstensv�gen 5-11, 75267 Uppsala", "Tom", "Yes"); // 3
		DepotUserInfo depotuser5 = new DepotUserInfo("Sandstensv�gen 3, 752 67 Uppsala", "Dick", "Yes"); // 4
		DepotUserInfo depotuser6 = new DepotUserInfo("Sandstensv�gen 2, 752 67 Uppsala", "Harry", "Yes"); // 5

		// DepotUserInfo depotuser7 = new DepotUserInfo("Stenhagsv�gen 223, 752 66
		// Uppsala", "Samuel", "Yes"); // 6
		DepotUserInfo depotuser8 = new DepotUserInfo("Stenhagsv�gen 213, 752 66 Uppsala", "Julia", "Yes"); // 7
		DepotUserInfo depotuser9 = new DepotUserInfo("Stenhagsv�gen 103, 752 60 Uppsala", "Daniel", "Yes"); // 8
		DepotUserInfo depotuser10 = new DepotUserInfo("Kullerstensv�gen 2, 752 60 Uppsala", "Philip", "Yes"); // 9
		DepotUserInfo depotuser11 = new DepotUserInfo("Stenhagsv�gen 89, 752 60 Uppsala", "Martin", "Yes"); // 10

		DepotUserInfo depotuser12 = new DepotUserInfo("Stenhagsv�gen 11, 752 60 Uppsala", "Emanuel", "Yes"); // 11

		infoList.add(depotuser1);
		infoList.add(depotuser2);
		infoList.add(depotuser3);
		infoList.add(depotuser4);
		infoList.add(depotuser5);
		infoList.add(depotuser6);
		// infoList.add(depotuser7);
		infoList.add(depotuser8);
		infoList.add(depotuser9);
		infoList.add(depotuser10);
		infoList.add(depotuser11);
		infoList.add(depotuser12);

	}

	private static void geoCodingService(LinkedList<DepotUserInfo> infoList, LinkedList<LocationUpdated> locList)
			throws UnsupportedEncodingException, IOException {

		for (int i = 0; i < infoList.size(); i++) {

			DepotUserInfo info = infoList.get(i);
			geoCode(info, locList);
		}

	}

	private static void geoCode(DepotUserInfo info, LinkedList<LocationUpdated> locList)
			throws UnsupportedEncodingException, IOException {
		ResponseFromGoogle res = new GeoCodingSircle().convertAddresstoLatLon(info.getAddress());
		if (res.getStatus().equals("OK")) {
			for (Result result : res.getResults()) {
				String latitude = result.getGeometry().getLocation().getLat();
				String longitude = result.getGeometry().getLocation().getLng();
				double lat = Double.parseDouble(latitude);
				double lng = Double.parseDouble(longitude);

				LocationUpdated loc = new LocationUpdated(lat, lng);
				DepotUserLocatn depotUserLoc = new DepotUserLocatn(info, loc);
				depotUserLocList.add(depotUserLoc);

				locList.add(loc);

				// System.out.println("Lattitude of address is :" +
				// result.getGeometry().getLocation().getLat());
				// System.out.println("Longitude of address is :" +
				// result.getGeometry().getLocation().getLng());
				// System.out.println("Location is " + info.getAddress());
			}
		} else {
			System.out.println(res.getStatus());
		}
	}

}
