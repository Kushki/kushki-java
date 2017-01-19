package com.kushkipagos;

import java.util.HashMap;
import java.util.Map;

public class Tax {
    private String id;
    private String name;
    private Double amount;

    public Tax(String id, String name, Double amount) {
        this.id = id;
        this.name = name;
        this.setAmount(amount);
    }

    public Map<String, String> toHash() throws KushkiException {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("taxId", this.id);
        hashMap.put("taxAmount", Validations.validateNumber(this.getAmount(), 0, 12, "El Amount"));
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
