package uk.ac.ed.inf.algorithms;

import uk.ac.ed.inf.jsons.JSONPoint;
import uk.ac.ed.inf.records.LngLat;

/**
 * WindingNumber is an algorithm that calculates whether a point is contained by a polygon
 * This has been helped by <a href="https://web.archive.org/web/20130126163405/http://geomalgorithms.com/a03-_inclusion.html">...</a>, using its pseudocode
 * It has been converted to Java by myself
 */
public class WindingNumber {

    /**
     * Using a trigonometric calculation, this checks if point 3 is either left of, on top of, or to the right of the line
     * made by point 1 and point 2
     * @param point1 first point of the line
     * @param point2 second point of the line
     * @param point3 the point to check where it is relative to the line
     * @return >0 if it is to the left, 0 if it is on top, <0 if it is to the right of the line
     */
    public static double isLeft(JSONPoint point1, JSONPoint point2, LngLat point3){

        return ( (point2.longitude - point1.longitude) * (point3.latitude() - point1.latitude) - (point3.longitude() - point1.longitude) * (point2.latitude - point1.latitude));

    }

    /**
     * isOnBoundary checks if point 3 is somewhere on the line between point 1 and 2
     * @param point1 first point of the line
     * @param point2 second point of the line
     * @param point3 point to check if is on the line
     * @return whether point 3 is on the line
     */
    public static boolean isOnBoundary(JSONPoint point1, JSONPoint point2, LngLat point3){

        //check distance from point1 to point 3
        double distance1 = Math.sqrt(Math.pow((point1.longitude - point3.longitude()),2) + Math.pow((point1.latitude - point3.latitude()),2));
        //same for point 2 to point 3
        double distance2 = Math.sqrt(Math.pow((point2.longitude - point3.longitude()),2) + Math.pow((point2.latitude - point3.latitude()),2));

        double distance3 = Math.sqrt(Math.pow((point2.longitude - point1.longitude),2) + Math.pow((point2.latitude - point1.latitude),2));

        //if the distance from point 3 to points 1 and 2 both add up to be the distance between point 2 and 1
        //then the point is on the line
        return distance1 + distance2 == distance3;

    }

    /**
     * This method checks if the LngLat point is contained by some polygon
     * @param point point to check if inside polygon
     * @param polygon the list of points that make up a polygon
     * @param n the number of points in the polygon
     * @return whether point is contained by polygon or not
     */
    public static boolean isInPolygon(LngLat point, JSONPoint[] polygon, int n){

        int windingNumber = 0;
        int onBoundary = 0;

        for (int i=0; i<n; i++){

            //2 points in polygon list make the single edge of polygon
            JSONPoint edge1 = polygon[i];
            JSONPoint edge2;

            //ensure that we create a closed polygon with all the edges (so the last point connects to first point)
            if (i == n-1){
                edge2 = polygon[0];
            } else{
                edge2 = polygon[i+1];
            }



            //checking first point in edge latitude is less than target point's latitude
            if (edge1.latitude <= point.latitude()){
                //checking second point in edge is strictly greater than target point's latitude
                if (edge2.latitude > point.latitude()){
                    //if the point we're checking is to the left of the edge, then it is contained by polygon
                    if (isLeft(edge1, edge2, point) > 0){
                        windingNumber += 1;
                    }
                } else if (isOnBoundary(edge1, edge2, point)) {
                    //check if the point is on the boundary of the edge
                    onBoundary += 1;
                }
            } else {

                if (edge2.latitude <= point.latitude()) {
                    //if the below is true, then the polygon has winded away from the point
                    //so the point is no longer contained by the polygon
                    if (isLeft(edge1, edge2, point) < 0) {
                        windingNumber -= 1;
                    }
                } else if (isOnBoundary(edge1, edge2, point)) {
                    onBoundary += 1;
                }

            }

        }

        //true if winding number is bigger than 0
        return ((windingNumber > 0) || (onBoundary > 0));

    }

}
