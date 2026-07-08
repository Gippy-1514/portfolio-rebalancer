package com.crd.rebalance;

import com.crd.rebalancer.RebalancerEngine;
import com.crd.rebalancer.Security;
import com.crd.util.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;

public class RebalancerEngineTest {
    @Test(priority = 1, description = "Verify Happy Path Rebalancing via Config Properties File")

    public void testHappyPathAccountABC() {

        System.out.println("Positive TEST: Happy Path Rebalancing (Account ABC): Result - ");

        double totalAssets = ConfigReader.getTotalAssets();

        List<Security> securities = ConfigReader.getSecuritiesFromConfig();
        Map<String, Double> expectedTrades = ConfigReader.getExpectedTrades();

        // 2. Execute calculation engine
        Map<String, Double> actualTrades = RebalancerEngine.calculateTrades(totalAssets, securities);

        System.out.println(" Processing and verification");

        for (Map.Entry<String, Double> entry : actualTrades.entrySet()) {
            String ticker = entry.getKey();
            double actualShares = entry.getValue();
            double expectedShares = expectedTrades.getOrDefault(ticker, 0.0);

            // Print beautifully dynamically
            String action = (actualShares < 0) ? "BUY" : (actualShares > 0) ? "SELL" : "HOLD";
            System.out.printf("   Asset: %-5s | Shares Generated: %-9.4f (%s)\n", ticker, actualShares, action);

            // TestNG loop assertion checking (actual, expected, tolerance)
            Assert.assertEquals(actualShares, expectedShares, 0.0001, "Mismatch found on ticker: " + ticker);
            System.out.println(" ");
        }

    }

    @Test(priority = 2, description = "Should throw exception when target allocation total doesn't equal 100%")

    public void testInvalidTargetAllocationSum() {
        System.out.println("NEGATIVE Test: Result - ");

        double totalAssets = ConfigReader.getTotalAssets();

        List<Security> invalidSecurities = ConfigReader.getSecuritiesWithBadSum();

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            RebalancerEngine.calculateTrades(totalAssets, invalidSecurities);
        });

        System.out.println("IllegalArgumentException was thrown. \n");
    }

    @Test(priority = 3, description = "Should throw exception if price hits minimum threshold")

    public void testInvalidUnitPrice() {

        System.out.println("Edge TEST: Invalid Unit Price Validation- Result - ");

        double totalAssets = ConfigReader.getTotalAssets();
        List<Security> badPriceSecurities = ConfigReader.getSecuritiesWithBadPrice();

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            RebalancerEngine.calculateTrades(totalAssets, badPriceSecurities);
        });

        System.out.println("Price compliance threshold exception successfully caught. \n");

    }

}
