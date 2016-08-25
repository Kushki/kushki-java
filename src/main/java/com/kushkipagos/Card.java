package com.kushkipagos;

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

    public Card(String name, String number, String cvv, String expiryMonth, String expiryYear) {
        this.name = name;
        this.number = number;
        this.cvv = cvv;
        this.expiryYear = expiryYear;
        this.expiryMonth = expiryMonth;
    }
}
