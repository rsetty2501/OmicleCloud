import java.util.Map;
import java.util.TreeMap;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;


public final class Clustering {
	
	private static Map<String,Collection<Integer>> mapClusterNodes = new HashMap<>();
   
    private Clustering(){

    }
    
    public  static Map<String,Collection<Integer>> sweepAlgoclustering(LinkedList<LocationUpdated> address, 
    		LinkedList<Integer> demandList, int vehicleCapacity){
    	
    	double angle;
        // Map demand to polar angle
    	TreeMap<Double,Integer> treeMapDemandAngle = new TreeMap<>();
    	
    	// First get all the polar angles for the points from the depot which is location 1
        for(int i = 0; i < address.size(); i++){

            if((i + 1) == address.size()){
                break;
            }
            else{
                angle = Math.atan2(address.get(i + 1).getLongitude() - address.get(0).getLongitude(),
                        address.get(i + 1).getLatitude() - address.get(0).getLatitude()) * 180 / Math.PI;

                treeMapDemandAngle.put(angle,i);
            }
        }
        
        // Create a Map to map cluster and the list
        int c = 0, sum = 0;
        String cluster = "Route" + c;
        Multimap<String,Integer> multimap = ArrayListMultimap.create();

        for(Map.Entry<Double,Integer> entry : treeMapDemandAngle.entrySet()){

            sum = sum + demandList.get(entry.getValue());
         

            if( sum <= vehicleCapacity){
                multimap.put(cluster,entry.getValue());
            }
            else{
                c++;
                cluster = "Route" + c;
                sum = demandList.get(entry.getValue());
                multimap.put(cluster,entry.getValue());
            }
        }
        
        for(int i = 0; i < c + 1; i++){
            String clust = "Route" + i;
            mapClusterNodes.put(clust,multimap.get(clust));
        }
   
    	return mapClusterNodes;
    }
    
    public  static Map<String,Collection<Integer>> sweepAlgoclusteringReverse(LinkedList<LocationUpdated> address, 
    		LinkedList<Integer> demandList, int vehicleCapacity){
    	
    	double angle;
        // Map demand to polar angle
    	TreeMap<Double,Integer> treeMapDemandAngle = new TreeMap<>(Collections.reverseOrder());
    	
    	// First get all the polar angles for the points from the depot which is location 1
        for(int i = 0; i < address.size(); i++){

            if((i + 1) == address.size()){
                break;
            }
            else{
                angle = Math.atan2(address.get(i + 1).getLongitude() - address.get(0).getLongitude(),
                        address.get(i + 1).getLatitude() - address.get(0).getLatitude()) * 180 / Math.PI;

                treeMapDemandAngle.put(angle,i);
            }
        }
        
        // Create a Map to map cluster and the list
        int c = 0, sum = 0;
        String cluster = "Route" + c;
        Multimap<String,Integer> multimap = ArrayListMultimap.create();

        for(Map.Entry<Double,Integer> entry : treeMapDemandAngle.entrySet()){

            sum = sum + demandList.get(entry.getValue());
         

            if( sum <= vehicleCapacity){
                multimap.put(cluster,entry.getValue());
            }
            else{
                c++;
                cluster = "Route" + c;
                sum = demandList.get(entry.getValue());
                multimap.put(cluster,entry.getValue());
            }
        }
        
        for(int i = 0; i < c + 1; i++){
            String clust = "Route" + i;
            mapClusterNodes.put(clust,multimap.get(clust));
        }
   
    	return mapClusterNodes;
    }
    

}
