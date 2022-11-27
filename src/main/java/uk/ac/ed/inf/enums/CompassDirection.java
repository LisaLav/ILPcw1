package uk.ac.ed.inf.enums;

/**
 * An enumeration for each of the directions the drone can move in
 * Has 16 compass directions, each of which relate to a specific degree it is equal to
 * East starts off at 0, then North is 90, etc.
 */
public enum CompassDirection {

    N(90),
    NNE(67.5),
    NE(45),
    ENE(22.5),
    E(0),
    ESE(337.5),
    SE(315),
    SSE(292.5),
    S(270),
    SSW(247.5),
    SW(225),
    WSW(202.5),
    W(180),
    WNW(157.5),
    NW(135),
    NNW(112.5);

    /**
     * The degree of a compass direction
     */
    private double degree;

    /**
     * Sets the degree for each of the enumerations
     * @param degree degree of compass direction
     */
    CompassDirection(double degree){
        this.degree = degree;
    }

    /**
     * Returns degree of the direction
     * @return degree
     */
    public double getDegree(){
        return degree;
    }

}
