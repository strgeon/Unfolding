package EarthquakeTest;

import java.util.*;
import processing.core.*;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.*;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.providers.Microsoft;
//import de.fhpotsdam.unfolding.providers.OpenStreetMap;
//import de.fhpotsdam.unfolding.providers.MapQuestProvider;
//import de.fhpotsdam.unfolding.providers.Yahoo;
//import de.fhpotsdam.unfolding.providers.MapBox;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class EarthquakeCityMap extends PApplet {
	private UnfoldingMap map;
	
	public void setup() {
		size (1300,600,OPENGL);
		//map = new UnfoldingMap(this,200,50,700,500, new Google.GoogleMapProvider());
		map = new UnfoldingMap(this,200,50,1000,500, new Microsoft.RoadProvider());
		//map = new UnfoldingMap(this,200,50,700,500, new OpenStreetMap.OpenStreetMapProvider());
		//map = new UnfoldingMap(this,200,50,700,500, new MapQuestProvider());
		//map = new UnfoldingMap(this,200,50,700,500, new Yahoo.RoadProvider());
		//map = new UnfoldingMap(this,200,50,700,500, new MapBox.WorldLightProvider());
		map.zoomToLevel(2);
		MapUtils.createDefaultEventDispatcher(this, map);
		
		List<PointFeature> bigEqs = new ArrayList<PointFeature>();
		List<Marker> markers = new ArrayList<Marker>();
		PointFeature eqfea;
		
		eqfea = new PointFeature(new Location(-38.14f,-73.03f));
		eqfea.addProperty("title", "Valdivia, Chile");
		eqfea.addProperty("magnitude", "9.5");
		eqfea.addProperty("date", "May 22, 1960");
		eqfea.addProperty("year", "1960");
		bigEqs.add(eqfea);
		
		eqfea = new PointFeature(new Location(61.02f,-147.65f));
		eqfea.addProperty("title", "1964 Great Alaska Earthquake");
		eqfea.addProperty("magnitude", "9.2");
		eqfea.addProperty("date", "March 28, 1964");
		eqfea.addProperty("year", "1964");
		bigEqs.add(eqfea);
		
		eqfea = new PointFeature(new Location(3.30f,95.78f));
		eqfea.addProperty("title", "Off the West Coast of Northern Sumatra");
		eqfea.addProperty("magnitude", "9.1");
		eqfea.addProperty("date", "December 26, 2004");
		eqfea.addProperty("year", "2004");
		bigEqs.add(eqfea);
		
		eqfea = new PointFeature(new Location(38.322f,142.369f));
		eqfea.addProperty("title", "Near the East Coast of Honshu, Japan");
		eqfea.addProperty("magnitude", "9.0");
		eqfea.addProperty("date", "March 11, 2011");
		eqfea.addProperty("year", "2011");
		bigEqs.add(eqfea);
		
		eqfea = new PointFeature(new Location(52.76f,160.06f));
		eqfea.addProperty("title", "Kamchafka");
		eqfea.addProperty("magnitude", "9.0");
		eqfea.addProperty("date", "November 4, 1952");
		eqfea.addProperty("year", "1952");
		bigEqs.add(eqfea);
	
		for (PointFeature eq : bigEqs) {
			markers.add(new SimplePointMarker(eq.getLocation(),eq.getProperties()));
		}
		
		map.addMarkers(markers);
				
	}
	
	public void draw () {
		background(10);
		map.draw();
		//addKey();
	}
}
