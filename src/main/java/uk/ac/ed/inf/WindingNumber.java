package uk.ac.ed.inf;

public class WindingNumber {

    public static double isLeft(JSONPoint point1, JSONPoint point2, LngLat point3){

        return ( (point2.longitude - point1.longitude) * (point3.latitude() - point1.latitude) - (point3.longitude() - point1.longitude) * (point2.latitude - point1.latitude));

    }

    public static boolean isOnBoundary(JSONPoint point1, JSONPoint point2, LngLat point3){

        //check distance from point1 to point 3
        double distance1 = Math.sqrt(Math.pow((point1.longitude - point3.longitude()),2) + Math.pow((point1.latitude - point3.latitude()),2));
        //same for point 2 to point 3
        double distance2 = Math.sqrt(Math.pow((point2.longitude - point3.longitude()),2) + Math.pow((point2.latitude - point3.latitude()),2));

        double distance3 = Math.sqrt(Math.pow((point2.longitude - point1.longitude),2) + Math.pow((point2.latitude - point1.latitude),2));

        if (distance1 + distance2 == distance3){
            return true;
        } else{
            return false;
        }

    }

    public static boolean isInPolygon(LngLat point, JSONPoint[] polygon, int n){

        int windingNumber = 0;
        int onBoundary = 0;

        for (int i=0; i<n; i++){

            //edges make the single edge of polygon
            JSONPoint edge1 = polygon[i];
            JSONPoint edge2;

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
                    onBoundary += 1;
                }
            } else {

                if (edge2.latitude <= point.latitude()) {
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
