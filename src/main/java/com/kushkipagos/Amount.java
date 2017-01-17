package com.kushkipagos;

import java.util.HashMap;
import java.util.Map;

public class Amount {
    private Double subtotalIVA;
    private Double iva;
    private Double subtotalIVA0;
    private Double ice;
    private Tax tax;

    public Amount(Double subtotalIVA, Double iva, Double subtotalIVA0, Double ice) {
        this.subtotalIVA = subtotalIVA;
        this.iva = iva;
        this.subtotalIVA0 = subtotalIVA0;
        this.ice = ice;
    }

    public Amount(Double subtotalIVA, Double iva, Double subtotalIVA0, Tax tax) {
        this.subtotalIVA = subtotalIVA;
        this.iva = iva;
        this.subtotalIVA0 = subtotalIVA0;
        this.tax = tax;
    }

    public Double getTotalAmount() {
        return subtotalIVA + subtotalIVA0 + iva + ice ;
    }

    public Double getTotalAmountColombia() {
        return subtotalIVA + subtotalIVA0 + iva + tax.getTotalTax() ;
    }

    public Map<String, String> toHash() throws KushkiException {
        String validatedSubtotalIVA = Validations.validateNumber(subtotalIVA, 0, 12, "El subtotal IVA");
        String validatedSubtotalIVA0 = Validations.validateNumber(subtotalIVA0, 0, 12, "El subtotal IVA 0");
        String validatedIva = Validations.validateNumber(iva, 0, 12, "El IVA");
        String validatedIce = Validations.validateNumber(ice , 0, 12, "El ICE");
        String validatedTotal = Validations.validateNumber(getTotalAmount(), 0, 12, "El total");
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("Subtotal_IVA", validatedSubtotalIVA);
        hashMap.put("Subtotal_IVA0", validatedSubtotalIVA0);
        hashMap.put("IVA", validatedIva);
        hashMap.put("ICE", validatedIce);
        hashMap.put("Total_amount", validatedTotal);
        return hashMap;
    }

    public Map<String, String> toHashColombia() throws KushkiException {
        String validatedSubtotalIVA = Validations.validateNumber(subtotalIVA, 0, 12, "El subtotal IVA");
        String validatedSubtotalIVA0 = Validations.validateNumber(subtotalIVA0, 0, 12, "El subtotal IVA 0");
        String validatedIva = Validations.validateNumber(iva, 0, 12, "El IVA");
        String validatedTax = Validations.validateNumber(tax.getTotalTax(), 0, 12, "El Total tax");
        String validatedTotal = Validations.validateNumber(getTotalAmountColombia(), 0, 12, "El total");
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("Subtotal_IVA", validatedSubtotalIVA);
        hashMap.put("Subtotal_IVA0", validatedSubtotalIVA0);
        hashMap.put("IVA", validatedIva);
        hashMap.put("TAX", validatedTax);
        hashMap.put("Total_amount", validatedTotal);
        return hashMap;
    }
}
