package uk.ac.ed.inf.jsons;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This helps convert the JSON of a single order into a POJO using Jackson
 */
public class OrderStructure {

    /**
     * The order number
     */
    @JsonProperty("orderNo")
    public String orderNo;

    /**
     * The date of the order
     */
    @JsonProperty("orderDate")
    public String orderDate;

    /**
     * The name of the customer
     */
    @JsonProperty("customer")
    public String customer;

    /**
     * The number of the credit card
     */
    @JsonProperty("creditCardNumber")
    public String creditCardNumber;

    /**
     * The expiry date of the credit card
     */
    @JsonProperty("creditCardExpiry")
    public String creditCardExpiry;

    /**
     * The CVV of the credit card
     */
    @JsonProperty("cvv")
    public String cvv;

    /**
     * The price of the order in pence, according to the JSON
     */
    @JsonProperty("priceTotalInPence")
    public String priceTotalInPence;

    /**
     * The items the customer wants ordered
     */
    @JsonProperty("orderItems")
    public String[] orderItems;

}
