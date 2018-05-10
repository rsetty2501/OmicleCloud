
public class DepotUserLocatn {
	
	private DepotUserInfo depotuser;
	private LocationUpdated location_coord;
	
	public DepotUserLocatn(DepotUserInfo depotuser, LocationUpdated location_coord) {
		this.depotuser = depotuser;
		this.location_coord = location_coord;
	}

	public DepotUserInfo getDepotuser() {
		return depotuser;
	}

	public void setDepotuser(DepotUserInfo depotuser) {
		this.depotuser = depotuser;
	}

	public LocationUpdated getLocation_coord() {
		return location_coord;
	}

	public void setLocation_coord(LocationUpdated location_coord) {
		this.location_coord = location_coord;
	}

}
