package LifeExpect;

import java.util.*;
import processing.core.*;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.*;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class LifeExpectancy extends PApplet {

	UnfoldingMap map;
	Map<String, Float> lifeExpByCountry;
	List<Feature> countries;
	List<Marker> countryMarkers;
	
	
	public void setup() {
		size (1300,600,OPENGL);
		//map = new UnfoldingMap(this,200,50,700,500, new Google.GoogleMapProvider());
		map = new UnfoldingMap(this,200,50,1000,500, new Microsoft.RoadProvider());
		MapUtils.createDefaultEventDispatcher(this, map);
		
		lifeExpByCountry = loadLifeExpectancyfromCSV("LifeExpectancyWorldBankModule3.csv");
		countries = GeoJSONReader.loadData(this,"countries.geo.json");
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		map.addMarkers(countryMarkers);
		shadeCountries();
	}
	
	public void draw() {
		map.draw();
	}
	
	private void shadeCountries() {
		for (Marker marker : countryMarkers) {
			String countryId = marker.getId();
			
			if (lifeExpByCountry.containsKey(countryId)) {
				float lifeExp = lifeExpByCountry.get(countryId);
				int colorLevel = (int) map(lifeExp,40,90,0,255);
				marker.setColor(color(255-colorLevel,100,colorLevel));
			}
			else {
				marker.setColor(color(150,150,150));
			}
		}
	}
	
	private Map<String, Float> loadLifeExpectancyfromCSV(String fileName) {
		
		Map<String, Float> lifeExpMap = new HashMap<String, Float>();
		String[] rows = loadStrings(fileName);
		//String quot = "\"";
		int quot = (int)'"';
		String badnum = "..";
		Float value = 0f;
		String country = "BAD";
		for (String row : rows) {
			value = 0f;
			country = "";
			if (row.charAt(0) == '"') {
				String[] columns = row.split(",");
				country = columns[4];
				if (columns[5].charAt(0) != '.') {
					//println(country,columns[5].indexOf(quot));
					if (columns[4].charAt(0) == ' ' ) {
						country = columns[5];
						value = Float.parseFloat(columns[6]);
						//println("it worked\n");
					}
					else {
						value = Float.parseFloat(columns[5]);
					}
				}
							
				lifeExpMap.put(country, value);
				//println(country.trim(),value);
			}
		}
		
		return lifeExpMap;
		
	}
}
