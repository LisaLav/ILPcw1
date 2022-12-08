package uk.ac.ed.inf.exceptions;

/**
 * Exists for when an order is asking for pizzas from more than 1 restaurant
 */
public class InvalidPizzaCombinationMultipleSuppliers extends RuntimeException{

    /**
     * The custom error message
     * @param errorMessage error message to display
     */
    public InvalidPizzaCombinationMultipleSuppliers(String errorMessage){ super(errorMessage); }

}
