package com.kushkipagos.unit;

import com.kushkipagos.KushkiException;
import com.kushkipagos.Tax;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class TaxTest {

    @Test
    public void shouldTransformToHash() throws KushkiException {
        String id = "3";
        String name = "Propina";
        Double amount = 0.0;
        Tax tax = new Tax(id, name, amount);
        Map<String, String> result = tax.toHash();
        Map<String, String> expectedTax = new HashMap<>();
        expectedTax.put("taxId", "3");
        expectedTax.put("taxAmount", "0.00");
        expectedTax.put("taxName", "Propina");
        assertThat(result, is(expectedTax));
    }

    @Test
    public void shouldThrowKushkiExceptionIfTaxIsInvalid() {
        Exception exception = null;
        try {
            new Tax("1", "name", -2d).toHash();
        } catch (KushkiException e) {
            exception = e;
        }
        String exceptionMessage = "El Amount debe ser superior o igual a 0";
        assertNotNull("Exception should not be null", exception);
        assertThat("Tax should be invalid because: " + exceptionMessage, exception.getMessage(),
                is(exceptionMessage));
    }

}
