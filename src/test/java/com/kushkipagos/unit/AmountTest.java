package com.kushkipagos.unit;

import com.kushkipagos.Amount;
import com.kushkipagos.KushkiException;
import com.kushkipagos.Tax;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.kushkipagos.commons.TestsHelpers.getRandomDouble;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(JUnitParamsRunner.class)
public class AmountTest {

    @Test
    public void shouldTransformToHash() throws KushkiException {
        Amount amount = new Amount(0d, 0d, 0d, 0d);
        Map<String, String> result = amount.toHash();
        Map<String, String> expectedResult = new HashMap<>();
        expectedResult.put("Subtotal_IVA", "0.00");
        expectedResult.put("Subtotal_IVA0", "0.00");
        expectedResult.put("IVA", "0.00");
        expectedResult.put("ICE", "0.00");
        expectedResult.put("Total_amount", "0.00");
        assertThat(result, is(expectedResult));
    }

    @Test
    public void shouldTransformToHashColombian() throws KushkiException {
        Tax tax = new Tax(0d, 0d, 0d, 0d);
        Map<String, String> resultTax = tax.toHash();
        Map<String, String> expectedResultTax = new HashMap<>();
        expectedResultTax.put("Propina", "0.00");
        expectedResultTax.put("Tasa_Aeroportuaria", "0.00");
        expectedResultTax.put("Agencia_De_Viaje", "0.00");
        expectedResultTax.put("Iac", "0.00");
        expectedResultTax.put("Total_Tax", "0.00");
        assertThat(resultTax, is(expectedResultTax));

        Amount amount = new Amount(0d, 0d, 0d, tax);
        Map<String, String> result = amount.toHashColombia();
        Map<String, String> expectedResult = new HashMap<>();
        expectedResult.put("Subtotal_IVA", "0.00");
        expectedResult.put("Subtotal_IVA0", "0.00");
        expectedResult.put("IVA", "0.00");
        expectedResult.put("TAX", "0.00");
        expectedResult.put("Total_amount", "0.00");
        assertThat(result, is(expectedResult));
    }

    @Test
    public void shouldTransformToHashWithValidInputs() throws KushkiException {
        Double subtotalIVA = getRandomDouble(1d, 50d);
        Double iva = getRandomDouble(1d, 50d);
        Double subtotalIVA0 = getRandomDouble(1d, 50d);
        Double ice = getRandomDouble(1d, 50d);
        Double total = subtotalIVA + iva + subtotalIVA0 + ice;

        Amount amount = new Amount(subtotalIVA, iva, subtotalIVA0, ice);
        Map<String, String> result = amount.toHash();
        Map<String, String> expectedResult = new HashMap<>();

        expectedResult.put("Subtotal_IVA", getStringValue(subtotalIVA));
        expectedResult.put("Subtotal_IVA0", getStringValue(subtotalIVA0));
        expectedResult.put("IVA", getStringValue(iva));
        expectedResult.put("ICE", getStringValue(ice));
        expectedResult.put("Total_amount", getStringValue(total));
        assertThat(result, is(expectedResult));
    }

    @Test
    public void shouldTransformToHashWithValidInputsColombia() throws KushkiException {
        Double subtotalIVA = getRandomDouble(1d, 50d);
        Double iva = getRandomDouble(1d, 50d);
        Double subtotalIVA0 = getRandomDouble(1d, 50d);
        Double propina = getRandomDouble(1d, 50d);
        Double tasaAeroportuaria = getRandomDouble(1d, 50d);
        Double agenciaDeViaje = getRandomDouble(1d, 50d);
        Double iac = getRandomDouble(1d, 50d);
        Tax tax = new Tax(propina, tasaAeroportuaria, agenciaDeViaje, iac);
        Double total = subtotalIVA + iva + subtotalIVA0 + tax.getTotalTax();

        Amount amount = new Amount(subtotalIVA, iva, subtotalIVA0, tax);
        Map<String, String> result = amount.toHashColombia();
        Map<String, String> expectedResult = new HashMap<>();

        expectedResult.put("Subtotal_IVA", getStringValue(subtotalIVA));
        expectedResult.put("Subtotal_IVA0", getStringValue(subtotalIVA0));
        expectedResult.put("IVA", getStringValue(iva));
        expectedResult.put("TAX", getStringValue(tax.getTotalTax()));
        expectedResult.put("Total_amount", getStringValue(total));
        assertThat(result, is(expectedResult));
    }

    @Test
    @Parameters(method = "invalidAmountsAndExceptionMessages")
    public void shouldThrowKushkiExceptionIfAmountIsInvalid(Amount amount, String exceptionMessage) {
        Exception exception = null;
        try {
            amount.toHash();
        } catch (KushkiException e) {
            exception = e;
        }
        assertNotNull("Exception should not be null", exception);
        assertThat("Amount should be invalid because: " + exceptionMessage, exception.getMessage(), is(exceptionMessage));
    }

    private static String getStringValue(Double amount) {
        return String.format(Locale.ENGLISH, "%.2f", amount);
    }

    @SuppressWarnings("unused")
    private Object[][] invalidAmountsAndExceptionMessages() {
        Amount invalidSubtotalIVA = new Amount(-2d, 0d, 0d, 0d);
        Amount invalidIva = new Amount(0d, -2d, 0d,  0d);
        Amount invalidSubtotalIVA0 = new Amount(0d, 0d, -2d,  0d);
        Amount invalidIce = new Amount(0d, 0d, 0d,  -2d);
        return new Object[][]{
                {invalidSubtotalIVA, "El subtotal IVA debe ser superior o igual a 0"},
                {invalidSubtotalIVA0, "El subtotal IVA 0 debe ser superior o igual a 0"},
                {invalidIva, "El IVA debe ser superior o igual a 0"},
                {invalidIce, "El ICE debe ser superior o igual a 0"},
        };
    }

    @SuppressWarnings("unused")
    private Object[][] invalidAmountsAndExceptionMessagesForColombia() {
        Tax invalidPropina = new Tax(-2d, 0d,0d, 0d);
        Tax invalidTasaAeroportuaria = new Tax(0d, -2d,0d, 0d);
        Tax invalidAgenciaDeViaje = new Tax(0d, 0d,-2d, 0d);
        Tax invalidIac = new Tax(0d, 0d,0d, -2d);

        Amount invalidSubtotalIVA = new Amount(-2d, 0d, 0d, 0d);
        Amount invalidIva = new Amount(0d, -2d, 0d,  0d);
        Amount invalidSubtotalIVA0 = new Amount(0d, 0d, -2d,  0d);

        return new Object[][]{
                {invalidSubtotalIVA, "El subtotal IVA debe ser superior o igual a 0"},
                {invalidSubtotalIVA0, "El subtotal IVA 0 debe ser superior o igual a 0"},
                {invalidIva, "El IVA debe ser superior o igual a 0"},
                {invalidPropina, "El ICE debe ser superior o igual a 0"},
                {invalidTasaAeroportuaria, "El ICE debe ser superior o igual a 0"},
                {invalidAgenciaDeViaje, "El ICE debe ser superior o igual a 0"},
                {invalidIac, "El ICE debe ser superior o igual a 0"},
        };
    }
}
