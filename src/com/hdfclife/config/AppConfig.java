package com.hdfclife.config;

public enum AppConfig {
    INSTANCE;

    private final String companyName = "HDFC Life";
    private final int maxClaimAmount = 500000;

    public String getCompanyName() {
        return companyName;
    }

    public int getMaxClaimAmount(){
        return maxClaimAmount;
    }
}
