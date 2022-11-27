package uk.ac.ed.inf.algorithms;

public class Luhns {

    public static boolean validateCardNumber(String cardNumber){

        int sum = 0;
        char checkDigit = cardNumber.charAt(15);
        //hard coding in 15 as all card numbers are of length 15, and need to drop off last digit
        cardNumber = cardNumber.substring(0,15);
        int[] doubledDigits = {0, 0, 0, 0, 0, 0, 0};

        //calculate the sum for Luhn
        for (int i=cardNumber.length()-1; i >= 0; i = i-2){



            doubledDigits[i%2] = Integer.parseInt(String.valueOf(cardNumber.charAt(i))) * 2;

            if (i != 0){
                sum += Integer.parseInt(String.valueOf(cardNumber.charAt(i)));
            }

        }

        //go through the digits we have doubled and sum
        for (int i=0; i < doubledDigits.length; i++){

            if (doubledDigits[i] >= 10){

                doubledDigits[i] = doubledDigits[i] - 9;

            }

            sum += doubledDigits[i];

        }

    }

}
