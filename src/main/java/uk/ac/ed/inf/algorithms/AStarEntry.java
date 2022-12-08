package uk.ac.ed.inf.algorithms;

import uk.ac.ed.inf.enums.CompassDirection;
import uk.ac.ed.inf.records.LngLat;

import java.time.Instant;

/**
 * This class helps the AStar class in implementing the priority queues according to a LngLat's f values
 * @param coords
 * @param fValue
 */
public record AStarEntry(LngLat coords, Double fValue, AStarEntry parent, Instant timeToCompute, CompassDirection angle) implements Comparable<AStarEntry>{

    /**
     *
     * @param entry the object to be compared.
     * @return -1 if this entry is smaller than the passed in entry, 0 if equal, 1 if greater than
     */
    @Override
    public int compareTo(AStarEntry entry) {

        return this.fValue.compareTo(entry.getFValue());

    }

    /**
     * Returns the LngLat of the coordinates
     * @return the coordinates
     */
    public LngLat getCoords(){ return this.coords; }

    /**
     * Returns the f value
     * @return f value
     */
    public Double getFValue(){ return this.fValue; }

    /**
     * Returns the parent LngLat of this entry
     * @return the parent LngLat
     */
    public AStarEntry getParent(){ return this.parent; }

    /**
     * Returns the time it took to compute this entry
     * @return the time to compute
     */
    public Instant getTimeToCompute(){ return this.timeToCompute; }

    /**
     * Returns the angle of the entry according to its parent
     * @return the angle
     */
    public CompassDirection getAngle(){ return this.angle; }

}
