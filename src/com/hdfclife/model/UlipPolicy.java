package com.hdfclife.model;

public class UlipPolicy extends Policy {
    public UlipPolicy(String policyNo, String customerName, String type, int basePremium, String status) {
        super(policyNo, customerName, "ULIP", basePremium, status);
    }
}
