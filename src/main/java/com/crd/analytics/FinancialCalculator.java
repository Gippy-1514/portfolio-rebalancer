package com.crd.analytics;

public class FinancialCalculator {

    private FinancialCalculator() {
        throw new IllegalStateException("Utility / Analytics class should not be instantiated");
    }

     // Value Delta = Total Capital * (Target Variance / 100)
    public static double calculateValueDelta(double totalAssets, double variance) {
        return totalAssets * (variance / 100.0);
    }

    /**
      Shares = Value Delta / Unit Price
     */
    public static double calculateRoundedShares(double valueDelta, double unitPrice) {
        double sharesToTrade = valueDelta / unitPrice;
        return Math.round(sharesToTrade * 10000.0) / 10000.0;
    }
}
