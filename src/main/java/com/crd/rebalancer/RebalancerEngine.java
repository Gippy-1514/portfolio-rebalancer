package com.crd.rebalancer;

import com.crd.analytics.FinancialCalculator;
import com.crd.validation.PortfolioValidator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RebalancerEngine {

    public static Map<String, Double> calculateTrades(double totalAssets, List<Security> securities) {

        PortfolioValidator.validate(totalAssets, securities);

        Map<String, Double> results = new HashMap<>();

        for (Security s : securities) {
            // Calculate financial cash deviation for asset
            double valueDelta = FinancialCalculator.calculateValueDelta(totalAssets, s.getVariance());

            // Convert cash deviation into a share count
            double finalShares = FinancialCalculator.calculateRoundedShares(valueDelta, s.getUnitPrice());

            results.put(s.getTicker(), finalShares);
        }

        return results;
    }
}
