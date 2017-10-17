package com.kushki.to;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Card {

    private String name;
    private String number;
    private String cvv;

    @JsonProperty("expiry_month")
    private String expiryMonth;

    @JsonProperty("expiry_year")
    private String expiryYear;

    @JsonProperty("card_present")
    private String cardPresent = "1";

    /**
     * @param name The cardholder name.
     * @param number The card number.
     * @param cvv The card security code, also known as CVC.
     * @param expiryMonth The two digits of the expiry month from 01 to 12 (<i>e.g.</i> "08").
     * @param expiryYear The last two digits of the expiry year (<i>e.g.</i> "21").
     */
    public Card(String name, String number, String cvv, String expiryMonth, String expiryYear) {
        this.name = name;
        this.number = number;
        this.cvv = cvv;
        this.expiryYear = expiryYear;
        this.expiryMonth = expiryMonth;
    }
}
