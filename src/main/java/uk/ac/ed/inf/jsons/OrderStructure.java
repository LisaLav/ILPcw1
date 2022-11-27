package uk.ac.ed.inf.jsons;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderStructure {

    @JsonProperty("orderNo")
    public String orderNo;

    @JsonProperty("orderDate")
    public String orderDate;

    @JsonProperty("customer")
    public String customer;

    @JsonProperty("creditCardNumber")
    public String creditCardNumber;

    @JsonProperty("creditCardExpiry")
    public String creditCardExpiry;

    @JsonProperty("cvv")
    public String cvv;

    @JsonProperty("priceTotalInPence")
    public String priceTotalInPence;

    @JsonProperty("orderItems")
    public String[] orderItems;

}
