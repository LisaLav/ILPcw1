package uk.ac.ed.inf;

public class WindingNumber {

    private static int isLeft(JSONPoint point1, JSONPoint point2, JSONPoint point3){

        return 1;

    }

    public static boolean isInPolygon(JSONPoint point, JSONPoint[] polygon, int n){

        int windingNumber = 0;

        for (int i=0; i<n; i++){

            //edges make the single edge of polygon
            JSONPoint edge1 = polygon[i];
            JSONPoint edge2 = polygon[i+1];

            //checking first point in edge latitude is less than target point's latitude
            if (edge1.latitude <= point.latitude){
                //checking second point in edge is strictly greater than target point's latitude
                if (edge2.latitude > point.latitude){
                    if (isLeft(edge1, edge2, point) > 0){
                        windingNumber += 1;
                    }
                }
            } else{

                if (edge2.latitude <= point.latitude){
                    if (isLeft(edge1, edge2, point) < 0){
                        windingNumber -= 1;
                    }
                }

            }

        }

        //true if winding number is bigger than 0
        return (windingNumber > 0);

    }

}
