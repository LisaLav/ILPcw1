package uk.ac.ed.inf;

import uk.ac.ed.inf.records.LngLat;
import com.mapbox.geojson.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * GeoJSON helps to create valid geojson objects
 */
public class GeoJSON {

    /**
     * generateGeoJson converts the parameters into a geojson format
     * @param path the points along a path to be converted
     * @return a geojson string of the path
     */
    public static String generateGeoJson(Vector<LngLat> path) {

        //initialise a list of points used by geojson
        List<Point> points = new ArrayList<>();

        //loop through the path, convert each LngLat to a point and add it to the list
        for (LngLat lngLat : path) {
            points.add(Point.fromLngLat(lngLat.longitude(), lngLat.latitude()));
        }

        //create a LineString from the points
        LineString lineString = LineString.fromLngLats(points);

        //create a feature from the LineString
        Feature feature = Feature.fromGeometry(lineString);

        //create a FeatureCollection/geojson from the feature
        FeatureCollection featureCollection = FeatureCollection.fromFeature(feature);

        //return the geojson as a string
        return featureCollection.toJson();

    }

}
