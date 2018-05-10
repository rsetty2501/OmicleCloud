
public class ClusterUserInfo {

	private String cluster;
	private DepotUserLocatn depotuserInfo;
	
	public ClusterUserInfo(String cluster, DepotUserLocatn depotuserInfo) {
		this.cluster = cluster;
		this.depotuserInfo = depotuserInfo;
	}

	public String getCluster() {
		return cluster;
	}

	public void setCluster(String cluster) {
		this.cluster = cluster;
	}

	public DepotUserLocatn getDepotuserInfo() {
		return depotuserInfo;
	}

	public void setDepotuserInfo(DepotUserLocatn depotuserInfo) {
		this.depotuserInfo = depotuserInfo;
	}
}
