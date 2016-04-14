package com.kushkipagos;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dvillaci on 4/14/16.
 */
public class Amount {
    private Double subtotalIVA;
    private Double iva;
    private Double subtotalIVA0;
    private Double subtotalICE;
    private Double ice;

    public Amount(Double subtotalIVA, Double iva, Double subtotalIVA0, Double subtotalICE, Double ice) {
        this.subtotalIVA = subtotalIVA;
        this.iva = iva;
        this.subtotalIVA0 = subtotalIVA0;
        this.subtotalICE = subtotalICE;
        this.ice = ice;
    }

    public Map<String, Double> toHash() {
        Double total = subtotalIVA + subtotalIVA0 + subtotalICE + iva + ice;
        Map<String, Double> hashMap = new HashMap<>();
        hashMap.put("Subtotal_IVA", subtotalIVA);
        hashMap.put("Subtotal_IVA0", subtotalIVA0);
        hashMap.put("Subtotal_ICE", subtotalICE);
        hashMap.put("IVA", iva);
        hashMap.put("ICE", ice);
        hashMap.put("Total_amount", total);
        return hashMap;
    }
}
