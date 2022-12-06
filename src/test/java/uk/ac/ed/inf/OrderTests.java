package uk.ac.ed.inf;

import org.junit.Test;
import uk.ac.ed.inf.algorithms.Luhns;
import uk.ac.ed.inf.jsons.OrderStructure;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class OrderTests {

    @Test
    public void testOrderGetDayOrders(){

        String url = "https://ilp-rest.azurewebsites.net/";
        RESTUrl.getInstance(url);

        String date = "2023-05-30";
        OrderStructure[] orders = Order.getDayOrders(date);

        System.out.println(orders.length);

    }

    @Test
    public void testCardCheckCreditCardGood(){

        ArrayList<String> cardDetails = new ArrayList<String>();

        cardDetails.add("4216153867511288");
        cardDetails.add("0323");
        cardDetails.add("123");

        int result = Card.checkCreditCard(cardDetails);

        //assertTrue(Card.checkCreditCard(cardDetails));

    }

    @Test
    public void testCardCheckCreditCardBadNumberNull(){

        ArrayList<String> cardDetails = new ArrayList<String>();

        cardDetails.add("");
        cardDetails.add("03/50");
        cardDetails.add("123");

        //assertFalse(Card.checkCreditCard(cardDetails));

    }

    @Test
    public void testCardCheckCreditCardBadNumberNonNumeric(){

        ArrayList<String> cardDetails = new ArrayList<String>();

        cardDetails.add("hi");
        cardDetails.add("03/50");
        cardDetails.add("123");

        //assertFalse(Card.checkCreditCard(cardDetails));

    }

    @Test
    public void testCardCheckCreditCardExpiryDateNull(){

        ArrayList<String> cardDetails = new ArrayList<String>();

        cardDetails.add("4216153867511288");
        cardDetails.add("");
        cardDetails.add("123");

       // assertFalse(Card.checkCreditCard(cardDetails));

    }

    @Test
    public void testCardCheckCreditCardExpired(){

        ArrayList<String> cardDetails = new ArrayList<String>();

        cardDetails.add("4216153867511288");
        cardDetails.add("03/09");
        cardDetails.add("123");

        //assertFalse(Card.checkCreditCard(cardDetails));

    }

    @Test
    public void testCardCheckCreditCardBadCVVNonNumeric(){

        ArrayList<String> cardDetails = new ArrayList<String>();

        cardDetails.add("4216153867511288");
        cardDetails.add("03/50");
        cardDetails.add("heck");

        //assertFalse(Card.checkCreditCard(cardDetails));

    }

    @Test
    public void testCardCheckCreditCardBadCVV4Digits(){

        ArrayList<String> cardDetails = new ArrayList<String>();

        cardDetails.add("4216153867511288");
        cardDetails.add("03/50");
        cardDetails.add("1234");

       // assertFalse(Card.checkCreditCard(cardDetails));

    }

    @Test
    public void testCardCheckCreditCardBadCVV2Digits(){

        ArrayList<String> cardDetails = new ArrayList<String>();

        cardDetails.add("4216153867511288");
        cardDetails.add("03/50");
        cardDetails.add("34");

        //assertFalse(Card.checkCreditCard(cardDetails));

    }

    @Test
    public void testCardCheckCreditCardBadCVVNull(){

        ArrayList<String> cardDetails = new ArrayList<String>();

        cardDetails.add("4216153867511288");
        cardDetails.add("03/50");
        cardDetails.add("");

        //assertFalse(Card.checkCreditCard(cardDetails));

    }

    @Test
    public void testLuhnsValidateCardNumber(){

        String cardNumber = "4216153867511288";

        assertTrue(Luhns.validateCardNumber(cardNumber));

    }

    @Test
    public void testLuhnsValidateCardNumber2(){

        String cardNumber = "2221325720627173";

        assertTrue(Luhns.validateCardNumber(cardNumber));

    }

    @Test
    public void testLuhnsValidateCardNumberInvalid(){

        String cardNumber = "2720686984791345";

        assertFalse(Luhns.validateCardNumber(cardNumber));

    }

    @Test
    public void testLuhnsValidateCardNumberInvalid2(){

        String cardNumber = "2222222222222222";

        assertFalse(Luhns.validateCardNumber(cardNumber));

    }

}
