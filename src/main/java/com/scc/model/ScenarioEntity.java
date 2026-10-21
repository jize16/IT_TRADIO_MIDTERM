
package com.scc.model;

public record ScenarioEntity(
        int id,
        String patient_name,
        String treatment,
        double amount_due,
        String payment
) {
}
