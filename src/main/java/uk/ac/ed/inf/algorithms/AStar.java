package uk.ac.ed.inf.algorithms;

import uk.ac.ed.inf.Drone;
import uk.ac.ed.inf.enums.CompassDirection;
import uk.ac.ed.inf.jsons.NoFlyZoneJSON;
import uk.ac.ed.inf.records.LngLat;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * A* algorithm for calculating the moves of the drone for a single order
 * This implements the A* algorithm from <a href="https://en.wikipedia.org/wiki/A*_search_algorithm">Wikipedia</a>
 */
public class AStar {

    //how much a vertical/horizontal move costs
    final static private int verticalCost = 1;
    //how much a diagonal move costs
    final static private double diagonalCost = Math.sqrt(2);

    /**
     * This is the heuristic of the A* algorithm. It uses the octile distance heuristic.
     * @param a the first point
     * @param b the second point
     * @return the heuristic value according to octile distance
     */
    private static double heuristic(LngLat a, LngLat b){

        //calculate the octile distance between two points
        double dx = Math.abs(a.longitude() - b.longitude());
        double dy = Math.abs(a.latitude() - b.latitude());

        return verticalCost * (dx + dy) + ((diagonalCost-2)*verticalCost) * Math.min(dx,dy);


    }

    /**
     * This method reconstructs the path from the start to the goal.
     * It also adds on the return journey from the goal to the start.
     * @param start the point to start constructing the path from (the goal)
     * @return the path from the start to the goal
     */
    private static ArrayList<AStarEntry> reconstructPath(AStarEntry start){

        //note that the array is built backwards, so the final element is where the *actual* start is
        ArrayList<AStarEntry> path = new ArrayList<>();

        //the drone has to hover at its destination, so we add a null
        path.add(new AStarEntry(null, start.fValue(), start, start.getTimeToCompute(), null));

        //add each LngLat parent to the path
        for (AStarEntry entry = start; entry != null; entry = entry.getParent()) {
            path.add(entry);
        }

        //and we need to add the path back to Appleton Tower too
        Collections.reverse(path);

        //so now we can keep on adding the paths as before
        for (AStarEntry entry = start; entry != null; entry = entry.getParent()){
            path.add(entry);
        }

        //and final null value for the hover at Appleton Tower
        path.add(new AStarEntry(null, null, null, path.get(path.size()-1).getTimeToCompute(), null));

        return path;

    }

    /**
     * intersectNoFlyZone checks if the drone is going to intersect a no-fly zone
     * It uses the algorithm from <a href="https://www.geeksforgeeks.org/check-if-two-given-line-segments-intersect/">GeeksForGeeks</a>
     * @param from the point the drone is moving from
     * @param to the point the drone is moving to
     * @param noFlyZone the list of no-fly zones to check against
     * @return if the drone is going to intersect a no-fly zone
     */
    private static boolean intersectNoFlyZone(LngLat from, LngLat to, NoFlyZoneJSON[] noFlyZone){

        //perform the check for each no-fly zone
        for (NoFlyZoneJSON polygon : noFlyZone){

            double[][] coordinates = polygon.lnglat;

            for (int i = 0; i < coordinates.length-1; i = i+2){

                LngLat p1 = new LngLat(coordinates[i][0], coordinates[i][1]);
                LngLat p2 = new LngLat(coordinates[i+1][0], coordinates[i+1][1]);

                if (orientation(from, to, p1) != orientation(from, to, p2) && orientation(p1, p2, from) != orientation(p1, p2, to)){
                    return true;
                }

            }

        }

        return (false);

    }

    /**
     * Helper method for intersectNoFlyZone
     * @param a point a
     * @param b point b
     * @param c point c
     * @return the orientation of the points
     */
    private static int orientation(LngLat a, LngLat b, LngLat c){

        double value = (b.latitude() - a.latitude()) * (c.longitude() - b.longitude()) - (b.longitude() - a.longitude()) * (c.latitude() - b.latitude());

        if (value == 0){
            return 0;
        } else if (value > 0){
            return 1;
        } else {
            return 2;
        }

    }

    /**
     * This is the A* algorithm. It calculates the path from the start to the goal and returns it.
     * @param start the start point
     * @param goal the end point
     * @param clock the clock to find out how long it took to calculate each point
     * @return the path from the start to the goal and back again
     */
    public static ArrayList<AStarEntry> astar(LngLat start, LngLat goal, Clock clock){

        //variables for A* algorithm
        PriorityQueue<AStarEntry> openNodes = new PriorityQueue<>();
        HashMap<LngLat, Double> gScore = new HashMap<>();
        HashMap<LngLat, Double> fScore = new HashMap<>();
        ArrayList<LngLat> closedNodes = new ArrayList<>();
        AStarEntry startEntry = new AStarEntry(start, 0.0, null, Clock.tick(clock, Duration.ofMillis(1)).instant(), null);

        //variables to ensure path doesn't go over no-fly zones or into central area too many times
        NoFlyZoneJSON[] noFlyZone = Drone.getNoFlyZones();
        int noTimesCrossedCentralArea = 0;

        //add initial values to the lists
        openNodes.add(startEntry);
        gScore.put(start, 0.0);
        //f(n) = g(n) + h(n)
        fScore.put(start, heuristic(start, goal));

        while (!openNodes.isEmpty()){

            //get the next node with the lowest f score & remove from openNodes
            AStarEntry current = openNodes.poll();
            LngLat currentCoords = current.getCoords();

            //if current is close to the goal then we can stop
            if (currentCoords.closeTo(goal)){
                return reconstructPath(current);
            }

            //as current has now been checked, add it to closedNodes
            closedNodes.add(currentCoords);

            //loop through the neighbours of current and check if we have found a better g score
            for (LngLat neighbour : currentCoords.getNeighbours()){

                //calculate the g score for the neighbour
                double neighbourGScore = gScore.get(currentCoords) + currentCoords.getDroneMovement();
                double neighbourHScore = heuristic(neighbour, goal);
                double neighbourFScore = neighbourGScore + neighbourHScore;
                Instant ticksSinceCalculation = Clock.tick(clock, Duration.ofMillis(1)).instant();
                //this is the angle that the drone must take from the current coordinates to the neighbour
                CompassDirection angle = currentCoords.getNeighbourAngle(neighbour);

                //skip if the neighbour is already in openNodes, and that neighbour has a better f score than this one
                if ((openNodes.contains(neighbour) || closedNodes.contains(neighbour)) && neighbourFScore >= fScore.get(neighbour)){
                    continue;
                } else if (neighbour.inNoFlyZone()){ //we skip if the neighbour is in a no-fly zone
                    continue;
                } else if (intersectNoFlyZone(currentCoords, neighbour, noFlyZone)){
                    continue;
                } else if ((currentCoords.inCentralArea() && !neighbour.inCentralArea()) || (!currentCoords.inCentralArea() && neighbour.inCentralArea())){ //here we're crossing the central area boundary in -> out OR out -> in

                    noTimesCrossedCentralArea++;

                    if (noTimesCrossedCentralArea > 2){ //here if it's > 2 then this neighbour will make us cross over the central area too many times
                        noTimesCrossedCentralArea--;
                        continue;
                    }

                }

                //else it is a better path, so add it to openNodes, update g and f scores, and add to/replace cameFrom
                AStarEntry neighbourEntry = new AStarEntry(neighbour, neighbourFScore, current, ticksSinceCalculation, angle);

                gScore.put(neighbour, neighbourGScore);
                fScore.put(neighbour, neighbourFScore);

                if (!openNodes.contains(neighbour)){
                    openNodes.add(neighbourEntry);
                }

            }

        }

        //if we reach here then we've failed to find a path
        return null;

    }

}
