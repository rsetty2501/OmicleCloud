import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class CustomerSegmentation {

	LinkedList<DepotUserLocatn> depotUserLocList = new LinkedList<>();
	Map<String, Collection<Integer>> mapClusterNodes = new HashMap<>();
	Map<String, LinkedList<LocationUpdated>> mapRouteAddress = new HashMap<>();
	Map<String, LinkedList<DepotUserLocatn>> mapClusterDepotUserLocatn = new HashMap<>();
	Map<String, LinkedList<Integer>> mapRouteAddressIndexOpt = new HashMap<>();
	Map<String, LinkedList<DepotUserLocatn>> mapClusterOptRoute = new HashMap<>();
	Map<String, Metrics> mapRouteMetrics = new HashMap<>();

	LinkedList<LocationUpdated> locList = new LinkedList<>();
	Map<String, LinkedList<Integer>> updatedCusterNodes = new HashMap<>();

	public CustomerSegmentation(Map<String, LinkedList<Integer>> updatedCusterNodes,
			LinkedList<LocationUpdated> locList, Map<String, Collection<Integer>> mapClusterNodes,
			LinkedList<DepotUserLocatn> depotUserLocList) {

		this.updatedCusterNodes = updatedCusterNodes;
		this.locList = locList;
		this.mapClusterNodes = mapClusterNodes;
		this.depotUserLocList = depotUserLocList;

	}

	public void getOptData() {
		// Preparation for Mapping Route to List of Addresses
		updatedCusterNodes = addDepotToRoute();

		// Map Route to real Addresses with the above location
		mapRouteLocation(updatedCusterNodes, locList);

	}

	public Map<String, LinkedList<DepotUserLocatn>> mapClusterOptimizedRoutes() {

		for (Map.Entry<String, LinkedList<Integer>> entry : mapRouteAddressIndexOpt.entrySet()) {

			String cluster = entry.getKey();
			LinkedList<Integer> list = entry.getValue();
			LinkedList<DepotUserLocatn> dUL_up = new LinkedList<>();

			for (int i = 0; i < list.size(); i++) {
				int index = list.get(i);
				for (Map.Entry<String, LinkedList<DepotUserLocatn>> entry1 : mapClusterDepotUserLocatn.entrySet()) {
					String cluster1 = entry1.getKey();
					LinkedList<DepotUserLocatn> dUL = entry1.getValue();
					if (cluster.equals(cluster1)) {
						dUL_up.add(dUL.get(index));
					}
				}

			}
			mapClusterOptRoute.put(cluster, dUL_up);
		}

		return mapClusterOptRoute;
	}

	public Map<String, Metrics> getOptimizedRouteCluster() {
		for (Map.Entry<String, LinkedList<LocationUpdated>> entry : mapRouteAddress.entrySet()) {

			String cluster = entry.getKey();
			System.out.println("Cluster : " + cluster);
			LinkedList<LocationUpdated> list = entry.getValue();
			LinkedList<Integer> locIndexList = RouteGeneration.OptimalRoute(list);
			Metrics metric = RouteGeneration.getCoordMetrics(locIndexList);

			mapRouteAddressIndexOpt.put(cluster, locIndexList);
			mapRouteMetrics.put(cluster, metric);

		}

		return mapRouteMetrics;

	}

	public void mapRouteLocation(Map<String, LinkedList<Integer>> updatedCusterNodes,
			LinkedList<LocationUpdated> locList) {

		for (Map.Entry<String, LinkedList<Integer>> entry : updatedCusterNodes.entrySet()) {
			String cluster = entry.getKey();
			LinkedList<Integer> list = entry.getValue();
			LinkedList<LocationUpdated> listAddress = new LinkedList<>();
			LinkedList<DepotUserLocatn> listDepotUserLocatn = new LinkedList<>();

			for (int i = 0; i < list.size(); i++) {
				int index = list.get(i);
				LocationUpdated loc = locList.get(index);
				DepotUserLocatn dLoc = depotUserLocList.get(index);
				listAddress.add(loc);
				listDepotUserLocatn.add(dLoc);
			}
			mapRouteAddress.put(cluster, listAddress);
			mapClusterDepotUserLocatn.put(cluster, listDepotUserLocatn);

		}

	}

	public Map<String, LinkedList<Integer>> addDepotToRoute() {

		Map<String, LinkedList<Integer>> updatedCusterNodes = new HashMap<>();

		for (Map.Entry<String, Collection<Integer>> entry : mapClusterNodes.entrySet()) {
			LinkedList<Integer> list = new LinkedList<>(entry.getValue());
			String cluster = entry.getKey();
			LinkedList<Integer> listUpdated = new LinkedList<>();

			listUpdated.add(0);
			for (int i = 0; i < list.size(); i++) {
				listUpdated.add(list.get(i) + 1);
			}
			updatedCusterNodes.put(cluster, listUpdated);
		}

		return updatedCusterNodes;
	}

	// private void print() {
	//
	// // Print the contents of MapCluster Opt route
	// for(Map.Entry<String, LinkedList<DepotUserLocatn>> entry:
	// mapClusterDepotUserLocatn.entrySet()) {
	// String cluster = entry.getKey();
	// LinkedList<DepotUserLocatn> list = entry.getValue();
	//
	// System.out.println(cluster + ":");
	// for(int i = 0; i < list.size(); i++) {
	// DepotUserLocatn u1 = list.get(i);
	// System.out.println(u1.getDepotuser().getAddress());
	// System.out.println(u1.getDepotuser().getName());
	// System.out.println(u1.getDepotuser().getReply());
	// System.out.println(u1.getLocation_coord().getLatitude());
	// System.out.println(u1.getLocation_coord().getLongitude());
	//
	// }
	// System.out.println();
	// }
	//
	// for(Map.Entry<String, Metrics> entry: mapRouteMetrics.entrySet()) {
	// String cluster = entry.getKey();
	// Metrics metric = entry.getValue();
	// System.out.println(cluster + " : " + metric.getDistance() + " " +
	// metric.getTime());
	// }
	// }

}
