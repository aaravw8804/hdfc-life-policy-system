package com.hdfclife.strategy;

public class EndowmentPremiumStrategy implements PremiumStrategy {
    @Override
    public int calculate(int basePremium) {
        return basePremium * 108/100;
    }
}
