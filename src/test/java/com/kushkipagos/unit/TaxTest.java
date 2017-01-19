package com.kushkipagos.unit;

import com.kushkipagos.Tax;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class TaxTest {
    @Test
    public void shouldCalculateTotalTax() throws Exception {
        Tax tax = new Tax(1d, 4d, 5d, 7d);
        Double expectedTotal = 17d;
        assertEquals(tax.getTotalTax(), expectedTotal);
    }

    @Test
    public void shouldTransformTaxToHash() throws Exception {
        Tax tax = new Tax(0d, 0d, 0d, 0d);
        Map<String, String> expectedTax = new HashMap<>();
        expectedTax.put("Propina", "0.00");
        expectedTax.put("Tasa_Aeroportuaria", "0.00");
        expectedTax.put("Agencia_De_Viaje", "0.00");
        expectedTax.put("Iac", "0.00");
        expectedTax.put("Total_Tax", "0.00");
        assertThat(tax.toHash(), is(expectedTax));
    }

}