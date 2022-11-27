package uk.ac.ed.inf.algorithms;

public class Luhns {

    public static boolean validateCardNumber(String cardNumber){

        boolean isValid = false;
        int sum = 0;
        char checkDigit = cardNumber.charAt(15);
        int[] doubledDigits = {0, 0, 0, 0, 0, 0, 0, 0};
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

        isValid = (sum % 10) == 0;

        return isValid;
    }

}
