package uk.ac.ed.inf.exceptions;

/**
 * Exists to throw a custom exception for orders that do not follow the guidelines
 */
public class InvalidPizzaCombinationException extends RuntimeException{

    /**
     * The custom error message
     * @param errorMessage error message to display
     */
    public InvalidPizzaCombinationException(String errorMessage){
        super(errorMessage);
    }

}