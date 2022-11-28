package uk.ac.ed.inf.exceptions;

public class InvalidPizzaNotDefined extends RuntimeException{

    public InvalidPizzaNotDefined(String errorMessage){
        super(errorMessage);
    }

}
