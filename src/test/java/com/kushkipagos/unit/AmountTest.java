package com.kushkipagos.unit;

import com.kushkipagos.Amount;
import com.kushkipagos.commons.TestsHelpers;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.kushkipagos.commons.TestsHelpers.getRandomDouble;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by dvillaci on 4/14/16.
 */
public class AmountTest {

    @Test
    public void shouldTransformToHash() {
        Amount amount = new Amount(0d, 0d, 0d, 0d, 0d);
        Map<String, Double> result = amount.toHash();
        Map<String, Double> expectedResult = new HashMap<>();
        expectedResult.put("Subtotal_IVA", 0d);
        expectedResult.put("Subtotal_IVA0", 0d);
        expectedResult.put("Subtotal_ICE", 0d);
        expectedResult.put("IVA", 0d);
        expectedResult.put("ICE", 0d);
        expectedResult.put("Total_amount", 0d);
        assertThat(result, is(expectedResult));
    }

    @Test
    public void shouldTransformToHashWithValidInputs() {
        Double subtotalIVA = getRandomDouble(1d, 50d);
        Double iva = getRandomDouble(1d, 50d);
        Double subtotalIVA0 = getRandomDouble(1d, 50d);
        Double subtotalICE = getRandomDouble(1d, 50d);
        Double ice = getRandomDouble(1d, 50d);
        Double total = subtotalIVA + iva + subtotalIVA0 + subtotalICE + ice;

        Amount amount = new Amount(subtotalIVA, iva, subtotalIVA0, subtotalICE, ice);
        Map<String, Double> result = amount.toHash();
        Map<String, Double> expectedResult = new HashMap<>();

        expectedResult.put("Subtotal_IVA", subtotalIVA);
        expectedResult.put("Subtotal_IVA0", subtotalIVA0);
        expectedResult.put("Subtotal_ICE", subtotalICE);
        expectedResult.put("IVA", iva);
        expectedResult.put("ICE", ice);
        expectedResult.put("Total_amount", total);
        assertThat(result, is(expectedResult));
    }
}
