package uk.ac.ed.inf.algorithms;

/**
 * This class implements the Luhns algorithm by <a href="https://en.wikipedia.org/wiki/Luhn_algorithm">Wikipedia</a>
 * It determines if a card is valid based on the computations in the card number
 */
public class Luhns {

    /**
     * This method checks if a card number is valid by using Luhns algorithm
     * @param cardNumber the card number to be validated
     * @return whether the card number is valid or not
     */
    public static boolean validateCardNumber(String cardNumber){

        boolean isValid;
        //each of the variables needed for Luhn's
        int sum = 0;
        int[] doubledDigits = {0, 0, 0, 0, 0, 0, 0, 0};
        //j is used to double the digits that need doubling in the algorithm in the loop
        int j = 0;

        //calculate the sum for Luhn
        for (int i=cardNumber.length()-2; i >= 0; i = i-2){

            int cardInt = Integer.parseInt(String.valueOf(cardNumber.charAt(i)));

            doubledDigits[j] = cardInt * 2;

            //need to add on the integers we skip without doubling them to the total sum
            sum += Integer.parseInt(String.valueOf(cardNumber.charAt(i+1)));
            j++;

        }

        //go through the digits we have doubled and sum
        for (int i=0; i < doubledDigits.length; i++){

            if (doubledDigits[i] >= 10){

                doubledDigits[i] = doubledDigits[i] - 9;

            }

            sum += doubledDigits[i];

        }

        //the card number is valid if it's divisible by 10
        isValid = (sum % 10) == 0;

        return isValid;
    }

}
