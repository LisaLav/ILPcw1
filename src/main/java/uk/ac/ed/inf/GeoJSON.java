package uk.ac.ed.inf;

import uk.ac.ed.inf.records.LngLat;
import com.mapbox.geojson.*;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class GeoJSON {
    // Method which takes Vector of LngLat and writes it to a geojson file
    public static void generateGeoJson(String filename, Vector<LngLat> path) {
        // Initialise a List of Points type used by geojson
        List<Point> points = new ArrayList<>();
        // Loop through the path, convert each LngLat to a Point and add it to the List
        for (LngLat lngLat : path) {
            points.add(Point.fromLngLat(lngLat.longitude(), lngLat.latitude()));
        }
        // Create a LineString from the List of Points
        LineString lineString = LineString.fromLngLats(points);
        // Create a Feature from the LineString
        Feature feature = Feature.fromGeometry(lineString);
        // Create a FeatureCollection from the Feature
        FeatureCollection featureCollection = FeatureCollection.fromFeature(feature);

        // Convert the FeatureCollection to a String
        String geoJson = featureCollection.toJson();
        // call writeGeoJson
        writegeojson(filename, geoJson);
    }
    // given geojson string write to file
    public static void writegeojson(String filename, String geoJson) {
        // Write to file with try and catch exception
        try {
            FileWriter file = new FileWriter(filename);
            file.write(geoJson);
            file.close();
        } catch (Exception e) {
            System.out.println("Error writing to file");
        }
    }

    // main file to test the code with longitudes in the range -3.184319 and -3.192473 and latitudes in the range 55.946233 and 55.942617
    public static void main(String[] args) {
        // Create a Vector of LngLat
        Vector<LngLat> path = new Vector<>();
        // Add LngLat to the Vector
        path.add(new LngLat(-3.184319, 55.946233));
        path.add(new LngLat(-3.192473, 55.942617));
        // Call writeGeoJson
        generateGeoJson("test.geojson", path);
    }
}
