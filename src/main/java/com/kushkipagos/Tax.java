package com.kushkipagos;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by acabrera on 17/01/17.
 */
public class Tax {
    private Double propina;
    private Double tasaAeroportuaria;
    private Double agenciaDeViaje;
    private Double iac;

    public Tax(Double propina, Double tasaAeroportuaria, Double agenciaDeViaje, Double iac) {
        this.propina = propina;
        this.tasaAeroportuaria = tasaAeroportuaria;
        this.agenciaDeViaje = agenciaDeViaje;
        this.iac = iac;

    }

    public Double getTotalTax() {return propina + tasaAeroportuaria + agenciaDeViaje + iac;}

    public Map<String, String> toHash() throws KushkiException {
        String validatedPropina = Validations.validateNumber(propina,0, 12, "La propina");
        String validatedTasaAeroportuaria = Validations.validateNumber(getTotalTax(), 0, 12, "La tasa aeroportuaria");
        String validatedAgenciaDeViaje= Validations.validateNumber(agenciaDeViaje, 0, 12, "La agencia de viaje");
        String validatedIac = Validations.validateNumber(iac, 0, 12, "El IAC");
        String validatedTotalTax = Validations.validateNumber(getTotalTax(), 0, 12, "El tax total");
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("Propina", validatedPropina);
        hashMap.put("Tasa_Aeroportuaria", validatedTasaAeroportuaria);
        hashMap.put("Agencia_De_Viaje", validatedAgenciaDeViaje);
        hashMap.put("Iac", validatedIac);
        hashMap.put("Total_Tax", validatedTotalTax);
        return hashMap;
    }
}
