package uk.ac.ed.inf;

import uk.ac.ed.inf.records.LngLat;
import com.mapbox.geojson.*;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class GeoJSON {
    // Method which takes Vector of LngLat and writes it to a geojson file
    public static String generateGeoJson(Vector<LngLat> path) {
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

        return geoJson;

    }

}
