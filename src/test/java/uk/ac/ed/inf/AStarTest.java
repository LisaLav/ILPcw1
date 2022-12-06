package uk.ac.ed.inf;

import org.junit.Test;
import uk.ac.ed.inf.algorithms.AStar;
import uk.ac.ed.inf.records.LngLat;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Vector;

public class AStarTest {

    @Test
    public void testAStarFindPathToRestaurantFromAT() throws MalformedURLException {

        LngLat appletonTower = new LngLat(-3.186874, 55.944494);
        LngLat restaurant = new LngLat(-3.202541470527649, 55.943284737579376);
        RESTUrl.getInstance("https://ilp-rest.azurewebsites.net/");
        Drone.StartDay(null);

        ArrayList<LngLat> path = AStar.astar(appletonTower,restaurant);
        Vector<LngLat> path2 = new Vector<LngLat>(path);

        GeoJSON.generateGeoJson("src/test/java/uk/ac/ed/inf/astarTest.json",path2);

    }


}
