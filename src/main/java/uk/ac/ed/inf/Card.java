package uk.ac.ed.inf;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.util.regex.*;

/**
 * This deals with the details of credit cards and determining if a card is invalid or not
 */
public class Card {

    /**
     * A formatter for converting the expiry date in an order (of type String) to LocalDate type
     */
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");

    private static boolean checkExpiryDate(LocalDate expiryDate){

        boolean isValid;

        isValid = !expiryDate.isBefore(LocalDate.now());

        return isValid;

    }

    private static boolean checkCVV(String cvv){

        boolean isValid = false;
        String regex = "^[0-9]{3}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cvv);

        if (matcher.matches()){
            isValid = true;
        }

        return isValid;

    }

    private static boolean checkCardNumber(String cardNumber){

        boolean isValid;
        String regex = "^[0-9]{16}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cardNumber);

        if (!(matcher.matches())){
            return false;
        }

        int cardType = Integer.parseInt(cardNumber.substring(0,2));

        //check if valid with mastercard
        if (51 <= cardType && cardType <= 55){

        }

        cardType = Integer.parseInt(cardNumber.substring(0,4));

        //also if valid with mastercard
        if (2221 <= cardType && cardType <= 2720){

        }

        cardType = Integer.parseInt(cardNumber.substring(0,1));

        //check if valid with visa
        if (cardType == 4){

        }

    }

    public static boolean checkCreditCard(ArrayList<String> cardDetails){

        boolean isValid = false;
        LocalDate expiryDate = null;
        String cvv = cardDetails.get(2);
        String cardNumber = cardDetails.get(0);

        if ((cvv == null) || (cardNumber == null)){
            return false;
        }

        try{
            expiryDate = LocalDate.parse(cardDetails.get(1), formatter);
        } catch(DateTimeParseException e){
            return false;
        }

        //check if card is past expiry date
        if (checkExpiryDate(expiryDate)){
            isValid = true;
        } else if (checkCVV(cvv)) {
            isValid = true;
        } else if (checkCardNumber(cvv)){
            isValid = true;
        }

        return isValid;

    }

}
