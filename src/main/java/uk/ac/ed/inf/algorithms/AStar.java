package uk.ac.ed.inf.algorithms;

import uk.ac.ed.inf.Drone;
import uk.ac.ed.inf.jsons.JSONPoint;
import uk.ac.ed.inf.records.LngLat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class AStar {

    private static double heuristic(LngLat a, LngLat b){

        int verticalCost = 10;
        int diagonalCost = 14;
        int diagonalDiagonalCost = 20;

        //calculate the octile distance between two points
        double dx = Math.abs(a.longitude() - b.longitude());
        double dy = Math.abs(a.latitude() - b.latitude());

        double distance = diagonalDiagonalCost*(Math.min(dx,dy)) + diagonalCost*(Math.min(dx,dy)) + verticalCost*(dx - dy);

        return distance;


    }

    private static ArrayList<LngLat> reconstructPath(AStarEntry start){

        ArrayList<LngLat> path = new ArrayList<LngLat>();

        //add each LngLat parent to the path
        for (AStarEntry entry = start; entry != null; entry = entry.getParent()) {
            path.add(entry.getCoords());
        }

        //add the start node to the path
        path.add(start.getCoords());

        return path;

    }

    public static ArrayList<LngLat> astar(LngLat start, LngLat goal){

        //variables for A* algorithm
        PriorityQueue<AStarEntry> openNodes = new PriorityQueue<>();
        HashMap<LngLat, Double> gScore = new HashMap<>();
        HashMap<LngLat, Double> fScore = new HashMap<>();
        ArrayList<LngLat> closedNodes = new ArrayList<>();
        ArrayList<LngLat> cameFrom = new ArrayList<>();
        AStarEntry startEntry = new AStarEntry(start, 0.0, null);

        //variables to ensure path doesn't go over no-fly zones or into central area too many times
        JSONPoint[] noFlyZone = Drone.getNoFlyZones();
        JSONPoint[] centralArea = Drone.getCentralArea();

        //add initial values to the lists
        openNodes.add(startEntry);
        gScore.put(start, 0.0);
        //f(n) = g(n) + h(n)
        fScore.put(start, heuristic(start, goal));

        while (!openNodes.isEmpty()){

            //get the next node with the lowest f score & remove from openNodes
            AStarEntry current = openNodes.poll();
            LngLat currentCoords = current.getCoords();
            double oldGScore;

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

                //skip if the neighbour is already in openNodes, and that neighbour has a better f score than this one
                if ((openNodes.contains(neighbour) || closedNodes.contains(neighbour)) && neighbourFScore >= fScore.get(neighbour)){
                    continue;
                } else if (neighbour.inNoFlyZone()){ //we skip if the neighbour is in a no-fly zone
                    continue;
                }

                //else it is a better path, so add it to openNodes, update g and f scores, and add to/replace cameFrom
                AStarEntry neighbourEntry = new AStarEntry(neighbour, neighbourFScore, current);

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
