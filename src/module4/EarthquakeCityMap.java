package module4;

//import java.util.ArrayList;
//import java.util.List;
import java.util.*;
import java.lang.*;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 * Date: July 17, 2015
 * */
public class EarthquakeCityMap extends PApplet {
	
	// We will use member variables, instead of local variables, to store the data
	// that the setUp and draw methods will need to access (as well as other methods)
	// You will use many of these variables, but the only one you should need to add
	// code to modify is countryQuakes, where you will store the number of earthquakes
	// per country.
	
	// You can ignore this.  It's to get rid of eclipse warnings
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFILINE, change the value of this variable to true
	private static final boolean offline = false;
	
	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	

	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";
	
	// The files containing city names and info and country names and info
	private String cityFile = "city-data.json";
	private String countryFile = "countries.geo.json";
	
	// The map
	private UnfoldingMap map;
	
	// Markers for each city
	private List<Marker> cityMarkers;
	// Markers for each earthquake
	private List<Marker> quakeMarkers;

	// A List of country markers
	private List<Marker> countryMarkers;

    int yellow = color(255, 255, 0);
    int red = color(255, 0, 0);
    int blue = color(0, 0, 255);

	
	public void setup() {		
		// (1) Initializing canvas and map tiles
		size(900, 700, OPENGL);
		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 650, 600, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom";  // The same feed, but saved August 7, 2015
		}
		else {
			//map = new UnfoldingMap(this, 200, 50, 650, 600, new Google.GoogleMapProvider());
			map = new UnfoldingMap(this, 200, 50, 650, 600, new Microsoft.RoadProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
		    //earthquakesURL = "2.5_week.atom";
		}
		MapUtils.createDefaultEventDispatcher(this, map);
		
		// FOR TESTING: Set earthquakesURL to be one of the testing files by uncommenting
		// one of the lines below.  This will work whether you are online or offline
		//earthquakesURL = "test1.atom";
		//earthquakesURL = "test2.atom";
		
		// WHEN TAKING THIS QUIZ: Uncomment the next line
		earthquakesURL = "quiz1.atom";
		
		
		// (2) Reading in earthquake data and geometric properties
	    //     STEP 1: load country features and markers
		List<Feature> countries = GeoJSONReader.loadData(this, countryFile);
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		for (Marker cm : countryMarkers) {
			cm.setProperty("numquakes", "0");
		}
			
		//     STEP 2: read in city data
		List<Feature> cities = GeoJSONReader.loadData(this, cityFile);
		cityMarkers = new ArrayList<Marker>();
		for(Feature city : cities) {
		  cityMarkers.add(new CityMarker(city));
		}
	    
		//     STEP 3: read in earthquake RSS feed
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    quakeMarkers = new ArrayList<Marker>();

		//sdg debug code
		/*
	    for(Feature cn : countries) {
			println(cn.getProperties());
		}
		for(Feature ct: cities) {
			println(ct.getProperties());
		}  
		for(PointFeature eq: earthquakes) {
			println(eq.getProperties());
		}
	    */
	  
		/*
	    for(Feature ct: cities) {
			println(ct.getProperties());
		}  
		for(PointFeature eq: earthquakes) {
			println(eq.getProperties());
		}
		*/
		
	    for(PointFeature feature : earthquakes) {
		  //check if LandQuake
		  if(isLand(feature)) {
		    quakeMarkers.add(new LandQuakeMarker(feature));
		  }
		  // OceanQuakes
		  else {
		    quakeMarkers.add(new OceanQuakeMarker(feature));
		  }
	    }

	    //sdg debug
	   // for(Marker qm : quakeMarkers) {
		//	println(qm.getLocation(),qm.getProperties());
		//}
	    // could be used for debugging
	    printQuakes();
	 		
	    // (3) Add markers to map
	    //     NOTE: Country markers are not added to the map.  They are used
	    //           for their geometric properties
	    map.addMarkers(quakeMarkers);
	    map.addMarkers(cityMarkers);
	    
	}  // End setup
	
	
	public void draw() {
		background(0);
		map.draw();
		addKey();
		
	}
	
	// helper method to draw key in GUI
	// TODO: Update this method as appropriate
	private void addKey() {	
		// Remember you can use Processing's graphics methods here
		fill(255, 250, 240);
		rect(25, 50, 150, 250);
		
		fill(0);
		textAlign(LEFT, CENTER);
		textSize(12);
		text("Earthquake Key", 50, 75);
		
		fill(color(150,30,30));
		triangle(50, 95, 45, 105, 55, 105);
		fill(color(255,255,255));
		ellipse(50,125,15,15);
		rect(40,140,20,20);
		rect(40,165,20,20);
		//pg.strokeWeight(2);
		line(50-12, 175-12, 50+12, 175+12);
		line(50-12, 175+12,	50+12, 175-12);
		fill(red);
		ellipse(50, 200, 15, 15);
		fill(yellow);
		ellipse(50, 225, 15, 15);
		fill(blue);
		ellipse(50, 250, 15, 15);

		
		fill(0, 0, 0);
		text("City Marker",75,100);
		text("Land Quake", 75,125);
		text("Ocean Quake",75,150);
		text("Past Day",75,175);
		text("Deep", 75, 200);
		text("Intermediate", 75, 225);
		text("Shallow", 75, 250);
		text("Size - Magnitude",40,275);
	}

	
	
	// Checks whether this quake occurred on land.  If it did, it sets the 
	// "country" property of its PointFeature to the country where it occurred
	// and returns true.  Notice that the helper method isInCountry will
	// set this "country" property already.  Otherwise it returns false.
	private boolean isLand(PointFeature earthquake) {
		
		// IMPLEMENT THIS: loop over all countries to check if location is in any of them
		
		// TODO: Implement this method using the helper method isInCountry
		
		// not inside any country

	    for (Marker cn : countryMarkers) {
	    	if (isInCountry(earthquake,cn)) {  		
	    		//println(cn.getProperties(),earthquake.getProperties());
	    		return true;
	    	}
	    }
	    
		return false;
	}
	
	// prints countries with number of earthquakes
	// You will want to loop through the country markers or country features
	// (either will work) and then for each country, loop through
	// the quakes to count how many occurred in that country.
	// Recall that the country markers have a "name" property, 
	// And LandQuakeMarkers have a "country" property set.
	private void printQuakes() 
	{
		int numq = 0;
		String numquakes;
		for (Marker eq : quakeMarkers) {
			if (eq.getProperty("isOnLand") == "true") {
				//println(eq.getProperties());
				for (Marker cn : countryMarkers){
					if (cn.getProperty("name") == eq.getProperty("country")) {
						//numquakes = cn.getStringProperty("numquakes");
						//numq = Integer.parseInt(numquakes) + 1;
						//numquakes = String.valueOf(numq);
						cn.setProperty("numquakes",String.valueOf(Integer.parseInt(cn.getStringProperty("numquakes")) + 1));
						//println(eq.getProperties(),cn.getProperties());
						//numq = (Integer.parseInt((String cn.getStringProperty("numquakes")))) + 1;
					//	cn.setProperty("numquakes", .(String.valueOf(cn.getProperty("numquakes")))
						//.setproperty("numquakes")
					}
				}
			}
		}
		
		for (Marker cn : countryMarkers) {
			if (Integer.parseInt(cn.getStringProperty("numquakes")) > 0) {
				println(cn.getProperties());
			}
		}
		
		numq = 0;
		for (Marker eq : quakeMarkers) {
			if (eq.getProperty("isOnLand") == "false") {
				++numq;
				println(eq.getProperties());
			}
		}
		println("Number of Ocean Quakes: ",numq);
		// TODO: Implement this method
		
	    // Here is an example of how to use Processing's color method to generate 
	    // an int that represents the color yellow.  
	    
	    //TODO: Add code here as appropriate
	  
		/*
	    for (PointFeature eq : earthquakes) {
	    	mark = (new SimplePointMarker(eq.getLocation(),eq.getProperties()));
	    	
	    	if ((Float)mark.getProperty("magnitude")>THRESHOLD_MODERATE) {
	    		mark.setRadius(30);
	    		mark.setColor(red);
	    	}
	    	else if ((Float)mark.getProperty("magnitude")>THRESHOLD_LIGHT) {
	    		mark.setRadius(15);
	    		mark.setColor(yellow);
	    	}
	    	else {
	    		mark.setRadius(5);
	    		mark.setColor(blue);
	    	}
			markers.add(mark);
			
		}
		map.addMarkers(markers);
	}
	*/
	}
	
	
	
	// helper method to test whether a given earthquake is in a given country
	// This will also add the country property to the properties of the earthquake 
	// feature if it's in one of the countries.
	// You should not have to modify this code
	private boolean isInCountry(PointFeature earthquake, Marker country) {
		// getting location of feature
		Location checkLoc = earthquake.getLocation();

		// some countries represented it as MultiMarker
		// looping over SimplePolygonMarkers which make them up to use isInsideByLoc
		if(country.getClass() == MultiMarker.class) {
				
			// looping over markers making up MultiMarker
			for(Marker marker : ((MultiMarker)country).getMarkers()) {
					
				// checking if inside
				if(((AbstractShapeMarker)marker).isInsideByLocation(checkLoc)) {
					earthquake.addProperty("country", country.getProperty("name"));
						
					// return if is inside one
					return true;
				}
			}
		}
			
		// check if inside country represented by SimplePolygonMarker
		else if(((AbstractShapeMarker)country).isInsideByLocation(checkLoc)) {
			earthquake.addProperty("country", country.getProperty("name"));
			
			return true;
		}
		return false;
	}

}
