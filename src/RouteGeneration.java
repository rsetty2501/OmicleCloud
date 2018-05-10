import java.util.LinkedList;
import java.util.Stack;

public final class RouteGeneration {

	private static double distance[][];
	private static double time[][];
	private static Stack<Integer> stack = new Stack<>();

	private RouteGeneration() {

	}

	public static LinkedList<Integer> OptimalRoute(LinkedList<LocationUpdated> address) {

		distance = new double[address.size()][address.size()];
		time = new double[address.size()][address.size()];
		LinkedList<Integer> minCostArray = new LinkedList<>();

		fillDistanceMatrix(address, distance, time);
		tspNearestNeighbor(minCostArray);
		return minCostArray;

	}
	
	public static Metrics getCoordMetrics(LinkedList<Integer> minCostArray1) {
		double sumDistance = 0.0;
		double sumDuration = 0.0;
        for(int k = 0; k < minCostArray1.size(); k++) {
        	
        	if(k == minCostArray1.size() -1 ) {
        		sumDistance = sumDistance + distance[minCostArray1.get(k)][minCostArray1.get(0)];
        		sumDuration = sumDuration + time[minCostArray1.get(k)][minCostArray1.get(0)];
        	}
        	else {
        		sumDistance = sumDistance + distance[minCostArray1.get(k)][minCostArray1.get(k + 1)];
        		sumDuration = sumDuration + time[minCostArray1.get(k)][minCostArray1.get(k + 1)];
        	}
        	
        }
        Metrics metric = new Metrics(sumDistance, sumDuration);
        return metric;
        
	}

	private static void tspNearestNeighbor(LinkedList<Integer> minCostArray) {

		int numberOfNodes = distance.length - 1;
		int[] visited = new int[distance.length];
		visited[0] = 1;
		minCostArray.add(0);
		stack.push(0);
		int element, dst = 0, i;
		double min = Integer.MAX_VALUE;
		boolean minFlag = false;

		while (!stack.isEmpty()) {
			element = stack.peek();
			i = 0;
			min = Integer.MAX_VALUE;
			while (i <= numberOfNodes) {
				if (distance[element][i] > 1 && visited[i] == 0) {
					if (min > distance[element][i]) {
						min = distance[element][i];
						dst = i;
						minFlag = true;
					}
				}
				i++;
			}
			if (minFlag) {
				visited[dst] = 1;
				stack.push(dst);
				minCostArray.add(dst);
				minFlag = false;
				continue;
			}
			stack.pop();
		}

	}

	private static void fillDistanceMatrix(LinkedList<LocationUpdated> address, double[][] distance, double[][] time) {

		// Assign the distance between the Geo points to the distance matrix
		for (int i = 0; i < address.size(); i++) {
			for (int j = 0; j < address.size(); j++) {

				if (i == j) {
					distance[i][j] = 0;
					time[i][j] = 0;
				} else {
					SircleDirection sircleDir = new SircleDirection(address.get(i).getLatitude(),
							address.get(i).getLongitude(), address.get(j).getLatitude(), address.get(j).getLongitude());
					Metrics metric = sircleDir.getDistanceTimeCoord();
					distance[i][j] = metric.getDistance();
					time[i][j] = metric.getTime();

				}
			}
		}
	}

}
