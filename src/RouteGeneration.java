import java.util.LinkedList;
import java.util.Stack;

public final class RouteGeneration {

	private static double distance[][];
	private static double time[][];
	private static Stack<Integer> stack = new Stack<>();

	// Variables for Tsp using dynamic programming
	static int n;
	static int START_NODE = 0;
	static int VISITED_ALL;
	// So, in Bitmasking (1 << n) is 2^n
	static double[][] dp;
	static int[][] tour;

	private RouteGeneration() {

	}

	public static LinkedList<Integer> OptimalRoute(LinkedList<LocationUpdated> address) {

		n = address.size();
		distance = new double[n][n];
		time = new double[n][n];
		LinkedList<Integer> minCostArray = new LinkedList<>();

		// Dynamic programming
		VISITED_ALL = (1 << n) - 1;
		dp = new double[n][(1 << n)];
		tour = new int[n][(1 << n)];

		// Init the dp array
		// Init the dp array
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < (1 << n); j++) {
				dp[i][j] = -1;
			}
		}

		fillDistanceMatrix(address, distance, time);

		if (address.size() <= 3) {
			tspNearestNeighbor(minCostArray);
		} else {
			tspDynamicProg(START_NODE, 1); // send the start node and the bit mask is 1 (0001) means visiting the first
											// node
			getMinpath(START_NODE, 1, minCostArray);
		}

		return minCostArray;

	}

	private static void getMinpath(int node, int visitedState, LinkedList<Integer> minCostArray) {

		int index = node;
		while (true) {
			minCostArray.add(index);
			int nextIndex = tour[index][visitedState];
			if (nextIndex == 0)
				break;
			int nextMask = visitedState | (1 << nextIndex);
			visitedState = nextMask;
			index = nextIndex;
		}

	}

	public static double tspDynamicProg(int node, int visitedState) {

		if (visitedState == VISITED_ALL) {
			return distance[node][0];
		}

		if (dp[node][visitedState] != -1) {
			return dp[node][visitedState];
		}

		double ans = 100000;
		int index = -1;

		// Visit all unvisited cities
		for (int city = 0; city < n; city++) {

			if ((visitedState & (1 << city)) == 0) {

				System.out.println("city: " + city);
				double newAns = distance[node][city] + tspDynamicProg(city, visitedState | (1 << city));

				if (newAns < ans) {
					ans = newAns;
					index = city;
				}
			}
		}

		tour[node][visitedState] = index;
		return dp[node][visitedState] = ans;

	}

	public static Metrics getCoordMetrics(LinkedList<Integer> minCostArray1) {
		double sumDistance = 0.0;
		double sumDuration = 0.0;
		for (int k = 0; k < minCostArray1.size(); k++) {

			if (k == minCostArray1.size() - 1) {
				sumDistance = sumDistance + distance[minCostArray1.get(k)][minCostArray1.get(0)];
				sumDuration = sumDuration + time[minCostArray1.get(k)][minCostArray1.get(0)];
			} else {
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
					System.out.println("Route distance: " + i + " " + j + "=" + metric.getDistance());
					// System.out.println("Route time: " + i + " " + j + "=" + metric.getTime());
					distance[i][j] = metric.getDistance();
					time[i][j] = metric.getTime();

				}
			}
		}
	}

}
