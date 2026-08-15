package com.hdfclife.strategy;

public class TermPremiumStrategy implements PremiumStrategy {
    @Override
    public int calculate(int basePremium) {
        return basePremium * 100/100;
    }
}
