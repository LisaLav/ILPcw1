package uk.ac.ed.inf.exceptions;

public class InvalidPizzaCount extends RuntimeException{

    public InvalidPizzaCount(String errorMessage){
        super(errorMessage);
    }

}
