package module6;

public class Airport {

	private String city;
	private String country;
	private String code3;
	
	public String getCity() { return this.city;	}
	public String getCountry() { return this.country; }
	public String getCode() { return this.code3; }

	public static String findAirportCode(String toFind, Airport[] airports) {
		for (Airport ap : airports) {
			if (ap.getCity().equals(toFind)) {
					return ap.getCode();
			}
		}
		return null;
	}
	
	public static String findAirportCodeBS(String toFind, Airport[] airports) {
		int low = 0;
		int high = airports.length;
		int mid;
		while (low <= high) {
			mid = low + (high-low)/2;
			int compare = toFind.compareTo(airports[mid].getCity());
			if (compare > 0) {
				low = mid + 1;
			}
			else if (compare < 0) {
				high = mid - 1;
			}
			else {
				return airports[mid].getCity();
			}
		}
		return null;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
