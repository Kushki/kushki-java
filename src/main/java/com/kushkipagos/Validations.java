package com.kushkipagos;

import java.util.Locale;

/**
 * Created by lmunda on 1/21/16 14:07.
 */
public final class Validations {

    private Validations() {
    }

    public static String validateAmount(Double amount) throws KushkiException {
        return Validations.validateNumber(amount, 0, 12, "El monto");
    }

    public static String validateInterest(Double interest) throws KushkiException {
        return Validations.validateNumber(interest, 0, 5, "La tasa de interés");
    }

    public static String validateMonths(Integer months) throws KushkiException {
        return Validations.validateNumber(months, 0, 2, "El número de meses");
    }

    private static String validateNumber(Number amount, Integer minValue, Integer maxLength, String amountName) throws KushkiException {
        if (amount == null) {
            throw new KushkiException(amountName + " no puede ser un valor nulo");
        }
        if (amount.doubleValue() <= minValue) {
            throw new KushkiException(amountName + " debe ser superior a " + minValue);
        }
        String validAmount = getStringValue(amount);
        if (validAmount.length() > maxLength) {
            throw new KushkiException(amountName + " debe tener " + maxLength + " o menos dígitos");
        }
        return validAmount;
    }

    private static String getStringValue(Number amount) {
        if (amount instanceof Double) {
            return String.format(Locale.ENGLISH, "%.2f", amount);
        } else if (amount instanceof Integer) {
            return String.valueOf(amount);
        }
        return "";
    }


}
