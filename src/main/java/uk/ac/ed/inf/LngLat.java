package uk.ac.ed.inf;

public record LngLat(double longitude, double latitude) {

    public LngLat{
        if (longitude >= 0 || latitude <= 0){
            throw new IllegalArgumentException(String.format("Invalid coordinates: longitude %f latitude %f", longitude, latitude));
        }
    }

    public boolean inCentralArea(){
        //replace values with constants of predefined places
        if (-3.192473 <= longitude && longitude <= -3.184319 && 55.942617 <= latitude && latitude <= 55.946233){
            return true;
        }
        return false;
    }

    public double distanceTo(LngLat coordsTo){
        double pythagoreanDistance = Math.sqrt(Math.pow((longitude - coordsTo.longitude),2) + Math.pow((latitude - coordsTo.latitude),2));
        return pythagoreanDistance;
    }

    public boolean closeTo(LngLat coordsTo){
        if (distanceTo(coordsTo) < 0.00015){
            return true;
        }
        return false;
    }

    public LngLat nextPosition(double compass){

    }

}
