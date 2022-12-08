package uk.ac.ed.inf;

import uk.ac.ed.inf.algorithms.Luhns;

import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.util.regex.*;

/**
 * This deals with the details of credit cards and determining if a credit card is invalid or not
 */
public class Card {

    /**
     * This method checks if the expiry date is valid or not
     * @param expiryDate the expiry date of the card
     * @return true if the expiry date is valid, false otherwise
     */
    private static boolean checkExpiryDate(YearMonth expiryDate){

        //the expiry date is valid if it is after the current year & month
        return !expiryDate.isBefore(YearMonth.now());

    }

    /**
     * This method checks if the CVV is valid or not by using regex
     * @param cvv the CVV of the card
     * @return true if the CVV is valid, false otherwise
     */
    private static boolean checkCVV(String cvv){

        boolean isValid = false;
        //the CVV is valid if it is 3 digits long and if it only contains digits
        String regex = "^[0-9]{3}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cvv);

        if (matcher.matches()){
            isValid = true;
        }

        return isValid;

    }

    /**
     * This method checks if the card number is valid or not by using Luhns algorithm
     * @param cardNumber the card number of the card
     * @return true if the card number is valid, false otherwise
     */
    private static boolean checkCardNumber(String cardNumber){

        boolean isValid = false;
        //the card number is valid if it is 16 digits long and if it only contains digits
        String regex = "^[0-9]{16}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cardNumber);

        //initial check if the card number fits the regex
        if (!(matcher.matches())){
            return false;
        }

        //we need to take the first 2 digits to determine if the card fits mastercard
        int cardType = Integer.parseInt(cardNumber.substring(0,2));

        //check if valid with mastercard
        if (51 <= cardType && cardType <= 55){
            if (Luhns.validateCardNumber(cardNumber)){
                isValid = true;
            }
        }

        //now we check if it's valid with mastercard other range
        cardType = Integer.parseInt(cardNumber.substring(0,4));

        //also if valid with mastercard
        if (2221 <= cardType && cardType <= 2720){
            if (Luhns.validateCardNumber(cardNumber)){
                isValid = true;
            }
        }

        //now we check if it's valid with visa
        cardType = Integer.parseInt(cardNumber.substring(0,1));

        //check if valid with visa
        if (cardType == 4){
            if (Luhns.validateCardNumber(cardNumber)){
                isValid = true;
            }
        }

        return isValid;

    }

    /**
     * This method checks if the credit card is valid or not
     * @param cardDetails contains the expiry date, cvv and card number of the card
     * @return 0 if card is valid, 1 if cvv is invalid, 2 if expiry date is invalid, 3 if card number is invalid
     */
    public static int checkCreditCard(ArrayList<String> cardDetails){

        //isValid is an int to help determine why the card is invalid
        int isValid = 0;
        YearMonth expiryDate;
        String cvv = cardDetails.get(2);
        String cardNumber = cardDetails.get(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");

        //check if cvv is invalid
        if ((cvv == null) || !checkCVV(cvv)){
            isValid = 1;
            return isValid;
        }

        //check if the expiry date matches the format
        try{
            expiryDate = YearMonth.parse(cardDetails.get(1), formatter);
        } catch(DateTimeParseException e){
            isValid = 2;
            return isValid;
        }

        //check if card expiry date or number is invalid
        if (!checkExpiryDate(expiryDate)){
            isValid = 2;
            return isValid;
        } else if (!checkCardNumber(cardNumber)){
            isValid = 3;
            return isValid;
        }

        return isValid;

    }

}
