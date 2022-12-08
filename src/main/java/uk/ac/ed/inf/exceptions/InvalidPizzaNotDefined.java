package uk.ac.ed.inf.exceptions;

/**
 * Exists for when an order asks for a pizza that is not on a restaurant's menu
 */
public class InvalidPizzaNotDefined extends RuntimeException{

    /**
     * The custom error message
     * @param errorMessage error message to display
     */
    public InvalidPizzaNotDefined(String errorMessage){
        super(errorMessage);
    }

}
