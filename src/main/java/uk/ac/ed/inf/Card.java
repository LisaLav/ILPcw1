package uk.ac.ed.inf;

import uk.ac.ed.inf.algorithms.Luhns;

import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.util.regex.*;

/**
 * This deals with the details of credit cards and determining if a card is invalid or not
 */
public class Card {

    private static boolean checkExpiryDate(YearMonth expiryDate){

        boolean isValid;

        isValid = !expiryDate.isBefore(YearMonth.now());

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

        boolean isValid = false;
        String regex = "^[0-9]{16}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cardNumber);

        if (!(matcher.matches())){
            return false;
        }

        int cardType = Integer.parseInt(cardNumber.substring(0,2));

        //check if valid with mastercard
        if (51 <= cardType && cardType <= 55){
            if (Luhns.validateCardNumber(cardNumber)){
                isValid = true;
            }
        }

        cardType = Integer.parseInt(cardNumber.substring(0,4));

        //also if valid with mastercard
        if (2221 <= cardType && cardType <= 2720){
            if (Luhns.validateCardNumber(cardNumber)){
                isValid = true;
            }
        }

        cardType = Integer.parseInt(cardNumber.substring(0,1));

        //check if valid with visa
        if (cardType == 4){
            if (Luhns.validateCardNumber(cardNumber)){
                isValid = true;
            }
        }

        return isValid;

    }

    public static int checkCreditCard(ArrayList<String> cardDetails){

        int isValid = 0;
        YearMonth expiryDate = null;
        String cvv = cardDetails.get(2);
        String cardNumber = cardDetails.get(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMyy");

        if ((cvv == null) || !checkCVV(cvv)){
            isValid = 1;
            return isValid;
        }

        try{
            expiryDate = YearMonth.parse(cardDetails.get(1), formatter);
        } catch(DateTimeParseException e){
            isValid = 2;
            return isValid;
        }

        //check that card is valid
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
