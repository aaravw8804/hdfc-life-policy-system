package com.hdfclife.strategy;

public class PremiumCalculator {
    private PremiumStrategy strategy;

    public PremiumCalculator(PremiumStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(PremiumStrategy strategy) {
        this.strategy = strategy;
    }

    public int calculatePremium(int basePremium) {
        return strategy.calculate(basePremium);
    }
}
