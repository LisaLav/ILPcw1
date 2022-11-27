package uk.ac.ed.inf;

import org.apache.commons.validator.routines.UrlValidator;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;
import static java.lang.System.exit;

/**
 * Hello world!
 *
 */
public class App 
{

    public static void main( String[] args ){

        String date = null;
        String restUrl = null;
        String hash = null;

        try{
            date = args[0];
            restUrl = args[1];
            hash = args[2];
        } catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Not enough arguments put in");
            exit(1);
        }


        //validate and set up REST url instance
        if (validUrl(restUrl)){
            RESTUrl.getInstance(restUrl);
        } else{
            System.out.println("REST url is not a valid url");
            exit(1);
        }

        //ensure date is valid
        if (convertDate(date) == null){
            System.out.println("Invalid date put in. Format is YYYY-MM-DD");
            exit(1);
        }

    }

    private static boolean validUrl(String url){

        UrlValidator validator = new UrlValidator();

        return validator.isValid(url);
    }

    private static String convertDate(String date){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date1;

        try{
            date1 = LocalDate.parse(date, formatter);
        } catch(DateTimeParseException e){
            return null;
        }

        return date;

    }

}
