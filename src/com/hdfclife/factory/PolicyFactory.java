package com.hdfclife.factory;

import com.hdfclife.exception.UnknownPolicyTypeException;
import com.hdfclife.model.*;

public class PolicyFactory {
    public static Policy create(String policyNo, String customerName, String type, int basePremium, String status) {
        return switch (type.toUpperCase()) {
            case "TERM" -> new TermLifePolicy(policyNo, customerName, "TERM", basePremium, status);
            case "ULIP" -> new UlipPolicy(policyNo, customerName,"ULIP", basePremium, status);
            case "ENDOWMENT" -> new EndowmentPolicy(policyNo, customerName, "ENDOWMENT",basePremium, status);
            default -> throw new UnknownPolicyTypeException("Unknown policy type: " + type);
        };
    }
}
