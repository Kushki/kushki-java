package com.kushki.to;

import com.kushki.KushkiException;
import com.kushki.Validations;

import java.util.HashMap;
import java.util.Map;

public class Tax {
    private String id;
    private String name;
    private Double amount;
    private String amountName;

    public Tax(String id, String name, Double amount, String amountName) {
        this.id = id;
        this.name = name;
        this.setAmount(amount);
        this.amountName = amountName;
    }

    public Map<String, String> toHash() throws KushkiException {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("taxId", this.id);
        hashMap.put("taxAmount", Validations.validateNumber(this.getAmount(), 0, 12, amountName));
        hashMap.put("taxName", this.name);
        return hashMap;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getAmount() {
        return this.amount;
    }
}
