package com.hdfclife.model;

public class Policy {
    private String policyNo;
    private String customerName;
    private String type;
    private int basePremium;
    private String status;

    public Policy(String policyNo, String customerName, String type, int basePremium, String status) {
        this.policyNo = policyNo;
        this.customerName = customerName;
        this.type = type;
        this.basePremium = basePremium;
        this.status = status;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getType() {
        return type;
    }

    public int getBasePremium() {
        return basePremium;
    }

    public String getStatus() {
        return status;
    }

    public String toString() {
        return policyNo + "|" + customerName + "|" + type + "|" + basePremium + "|" + status;
    }
}
