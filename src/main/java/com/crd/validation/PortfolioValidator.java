package com.crd.validation;

import com.crd.rebalancer.Security;
import com.crd.util.ConfigReader;

import java.util.List;

public class PortfolioValidator {

    public static void validate(double totalAssets, List<Security> securities) {
        if (totalAssets < 0) {
            throw new IllegalArgumentException("Total assets cannot be negative");
        }

        // 1. DYNAMIC CHECK: Allocation sum limits are now fetched from config
        double allowedAllocationSum = ConfigReader.getRequiredTargetAllocationTotal();
        double totalTargetPct = securities.stream().mapToDouble(Security::getTargetPct).sum();

        if (Math.abs(totalTargetPct - allowedAllocationSum) > 0.0001) {
            throw new IllegalArgumentException("Total target allocation must equal " + allowedAllocationSum + "%");
        }

        // 2. DYNAMIC CHECK: Price boundaries are now fetched from config
        double minimumPriceLimit = ConfigReader.getMinimumAllowableUnitPrice();
        for (Security s : securities) {
            if (s.getUnitPrice() <= minimumPriceLimit) {
                throw new IllegalArgumentException("Invalid unit price for " + s.getTicker() + ". Must be above $" + minimumPriceLimit);
            }
        }
    }
}

