package com.hdfclife.strategy;

public class UlipPremiumStrategy implements PremiumStrategy {
    @Override
    public int calculate(int basePremium) {
        return basePremium * 112/100;
    }
}
