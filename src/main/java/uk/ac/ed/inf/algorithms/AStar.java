package uk.ac.ed.inf.algorithms;

import uk.ac.ed.inf.records.LngLat;

import java.util.ArrayList;
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

    private static ArrayList<LngLat> reconstructPath(LngLat start, LngLat goal, ArrayList<LngLat> cameFrom){

        ArrayList<LngLat> path = new ArrayList<LngLat>();
        LngLat current = goal;

        //loop through LngLats in cameFrom and add them to the path
        for (int i = cameFrom.size(); i > 0; i--){

            path.add(current);
            current = cameFrom.get(i);

        }

        //then add the start LngLat
        path.add(start);

        return path;

    }


    public static ArrayList<LngLat> astar(LngLat start, LngLat goal){

        PriorityQueue<LngLat> openNodes = new PriorityQueue<>();
        ArrayList<LngLat> closedNodes = new ArrayList<>();
        ArrayList<LngLat> cameFrom = new ArrayList<>();
        ArrayList<Double> gPathScore = new ArrayList<>();
        ArrayList<Double> fPathScore = new ArrayList<>();

        //add initial values to the lists
        openNodes.add(start);
        gPathScore.add(0.0);
        fPathScore.add(heuristic(start, goal));

        while (!openNodes.isEmpty()){

            //get the next node with the lowest f score
            LngLat current = openNodes.poll();

            //if current = goal then we can stop
            if (current.equals(goal)){
                return reconstructPath(start, goal, cameFrom);
            }

            //as current has now been checked, add it to closedNodes
            closedNodes.add(current);

            //loop through the neighbours of current LngLat and calculate the scores for each one
            for (int i = 0; i < current.getNeighbours().length; i++){

                LngLat neighbour = current.getNeighbours()[i];

                if (closedNodes.contains(neighbour)){
                    continue;
                }

                double tentativeGScore = gPathScore.get(closedNodes.indexOf(current)) + heuristic(current, neighbour);

                if (!openNodes.contains(neighbour)){
                    openNodes.add(neighbour);
                } else if (tentativeGScore < gPathScore.get(closedNodes.indexOf(neighbour))){

                    cameFrom.set(closedNodes.indexOf(neighbour), current);
                    gPathScore.set(closedNodes.indexOf(neighbour), tentativeGScore);
                    fPathScore.set(closedNodes.indexOf(neighbour), gPathScore.get(closedNodes.indexOf(neighbour)) + heuristic(neighbour, goal));

                }

            }

        }

    }

}
