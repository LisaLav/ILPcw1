package uk.ac.ed.inf;

import org.junit.Test;
import uk.ac.ed.inf.algorithms.AStar;
import uk.ac.ed.inf.records.LngLat;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Vector;

public class AStarTest {

    @Test
    public void testAStarFindPathToSodebergRestaurantFromAT() throws MalformedURLException {

        LngLat appletonTower = new LngLat(-3.186874, 55.944494);
        LngLat restaurant = new LngLat(-3.1940174102783203, 55.94390696616939);
        RESTUrl.getInstance("https://ilp-rest.azurewebsites.net/");
        Drone.StartDay("2022-04-20");

        ArrayList<LngLat> path = AStar.astar(appletonTower,restaurant);
        Vector<LngLat> path2 = new Vector<LngLat>(path);

        GeoJSON.generateGeoJson("src/test/java/uk/ac/ed/inf/astarTest.json",path2);

    }

    @Test
    public void testAStarFindPathToDominosRestaurantFromAT() throws MalformedURLException {

        LngLat appletonTower = new LngLat(-3.186874, 55.944494);
        LngLat restaurant = new LngLat(-3.1838572025299072, 55.94449876875712);
        RESTUrl.getInstance("https://ilp-rest.azurewebsites.net/");
        Drone.StartDay(null);

        ArrayList<LngLat> path = AStar.astar(appletonTower,restaurant);
        Vector<LngLat> path2 = new Vector<LngLat>(path);

        GeoJSON.generateGeoJson("src/test/java/uk/ac/ed/inf/astarTest.json",path2);

    }

    @Test
    public void testAStarFindPathToSoraLellaRestaurantFromAT() throws MalformedURLException {

        LngLat appletonTower = new LngLat(-3.186874, 55.944494);
        LngLat restaurant = new LngLat(-3.202541470527649, 55.943284737579376);
        RESTUrl.getInstance("https://ilp-rest.azurewebsites.net/");
        Drone.StartDay(null);

        ArrayList<LngLat> path = AStar.astar(appletonTower,restaurant);
        Vector<LngLat> path2 = new Vector<LngLat>(path);

        GeoJSON.generateGeoJson("src/test/java/uk/ac/ed/inf/astarTest.json",path2);

    }

    @Test
    public void testAStarFindPathToCiverinosRestaurantFromAT() throws MalformedURLException {

        LngLat appletonTower = new LngLat(-3.186874, 55.944494);
        LngLat restaurant = new LngLat(-3.1912869215011597, 55.945535152517735);
        RESTUrl.getInstance("https://ilp-rest.azurewebsites.net/");
        Drone.StartDay(null);

        ArrayList<LngLat> path = AStar.astar(appletonTower,restaurant);
        Vector<LngLat> path2 = new Vector<LngLat>(path);

        GeoJSON.generateGeoJson("src/test/java/uk/ac/ed/inf/astarTest.json",path2);

    }


}
