package uk.ac.ed.inf;

import org.apache.commons.validator.routines.UrlValidator;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;
import static java.lang.System.exit;

/**
 * This class is the main class of the application that starts the program.
 * It contains the main method, which is the entry point of the application.
 * It also contains the methods that are used to validate the input arguments.
 */
public class App 
{

    /**
     * This sets up all the files, validates the args, and starts the Drone.
     * @param args arguments from the command line.
     */
    public static void main( String[] args ){

        String date = null;
        String restUrl = null;

        //check if enough args are given
        try{
            date = args[0];
            restUrl = args[1];
        } catch (ArrayIndexOutOfBoundsException e){
            System.err.println("Not enough arguments put in");
            exit(1);
        }


        //validate and set up REST url instance
        if (validUrl(restUrl)){
            RESTUrl.getInstance(restUrl);
        } else{
            System.err.println("REST url is not a valid url");
            exit(1);
        }

        //ensure date is valid
        if (convertDate(date) == null){
            System.err.println("Invalid date put in. Format is YYYY-MM-DD");
            exit(1);
        }

        //start the drone
        try {
            Drone.StartDay(date);
        } catch (MalformedURLException e){
            System.err.println("Malformed URL Exception when starting Drone");
            exit(1);
        }

    }

    /**
     * This method checks if the url is valid.
     * @param url the url to be checked.
     * @return true if the url is valid, false otherwise.
     */
    private static boolean validUrl(String url){

        UrlValidator validator = new UrlValidator();

        return validator.isValid(url);
    }

    /**
     * This method tries converting the date string into a LocalDate object to check if the date is in a valid format.
     * @param date the date string to be validated.
     * @return the date string if it is valid, null if it isn't.
     */
    private static String convertDate(String date){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try{
            LocalDate.parse(date, formatter);
        } catch(DateTimeParseException e){
            return null;
        }

        return date;

    }

}
