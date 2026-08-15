package com.hdfclife.model;

public class TermLifePolicy extends Policy {
    public TermLifePolicy(String policyNo, String customerName, String type, int basePremium, String status ) {
        super(policyNo, customerName, "TERM", basePremium, status);
    }

}
