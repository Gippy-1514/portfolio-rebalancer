package com.crd.rebalance;

import com.crd.rebalancer.RebalancerEngine;
import com.crd.rebalancer.Security;
import com.crd.util.ConfigReader;
import com.crd.reporting.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import java.util.List;
import java.util.Map;

@Listeners(ExtentReportManager.class)
public class RebalancerEngineTest {
    @Test(priority = 1, description = "Verify Happy Path Rebalancing via Config Properties File")

    public void testHappyPathAccountABC() {

        System.out.println("Positive Test: Happy Path Rebalancing (Account ABC) \n");

        double totalAssets = ConfigReader.getTotalAssets();

        List<Security> securities = ConfigReader.getSecuritiesFromConfig();
        Map<String, Double> expectedTrades = ConfigReader.getExpectedTrades();

        Map<String, Double> actualTrades = RebalancerEngine.calculateTrades(totalAssets, securities);

        for (Map.Entry<String, Double> entry : actualTrades.entrySet()) {
            String ticker = entry.getKey();
            double actualShares = entry.getValue();
            double expectedShares = expectedTrades.getOrDefault(ticker, 0.0);

            String action = (actualShares < 0) ? "BUY" : (actualShares > 0) ? "SELL" : "HOLD";
            System.out.printf("   Asset: %-5s | Shares Generated: %-9.4f (%s)\n", ticker, actualShares, action);

            Assert.assertEquals(actualShares, expectedShares, 0.0001, "Mismatch found on ticker: " + ticker);
            System.out.println(" ");
        }
    }

    @Test(priority = 2, description = "Should throw exception when target allocation total doesn't equal 100%")

    public void testInvalidTargetAllocationSum() {
        System.out.println("Negative Test: Invalid Target Allocation Sum Validation");

        double totalAssets = ConfigReader.getTotalAssets();

        List<Security> invalidSecurities = ConfigReader.getSecuritiesWithBadSum();

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            RebalancerEngine.calculateTrades(totalAssets, invalidSecurities);
        });

        System.out.println("IllegalArgumentException was thrown. \n");
    }

    @Test(priority = 3, description = "Should throw exception if price hits minimum threshold")

    public void testInvalidUnitPrice() {

        System.out.println("Edge Test: Invalid Unit Price Validation");

        double totalAssets = ConfigReader.getTotalAssets();
        List<Security> badPriceSecurities = ConfigReader.getSecuritiesWithBadPrice();

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            RebalancerEngine.calculateTrades(totalAssets, badPriceSecurities);
        });

        System.out.println("Price compliance threshold exception successfully caught. \n");

    }

}
