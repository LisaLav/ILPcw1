package uk.ac.ed.inf.algorithms;

import uk.ac.ed.inf.records.LngLat;

/**
 * This class helps the AStar class in implementing the priority queues according to a LngLat's f values
 * @param coords
 * @param fValue
 */
public record AStarEntry(LngLat coords, Double fValue, AStarEntry parent) implements Comparable<AStarEntry>{

    /**
     *
     * @param entry the object to be compared.
     * @return -1 if this entry is smaller than the passed in entry, 0 if equal, 1 if greater than
     */
    @Override
    public int compareTo(AStarEntry entry) {

        if (entry.getFValue() > this.fValue){
            return -1;
        } else if (entry.getFValue() == this.fValue){
            return 0;
        } else{
            return 1;
        }

    }

    public LngLat getCoords(){ return this.coords; }
    public Double getFValue(){ return this.fValue; }
    public AStarEntry getParent(){ return this.parent; }

}
