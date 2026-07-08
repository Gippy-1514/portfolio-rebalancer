package com.crd.util;

import com.crd.rebalancer.Security;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ConfigReader {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("Sorry, unable to find config.properties in resources folder");
            }
            properties.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("Error loading config.properties configuration file", ex);
        }
    }

    public static double getTotalAssets() {
        return Double.parseDouble(properties.getProperty("portfolio.totalAssets"));
    }

    public static List<Security> getSecuritiesFromConfig() {
        List<Security> securities = new ArrayList<>();
        String[] tickers = {"IBM", "MSFT", "ORCL", "AAPL", "HD"};

        for (String ticker : tickers) {
            String propertyValue = properties.getProperty("security." + ticker);
            if (propertyValue != null) {
                String[] tokens = propertyValue.split(",");
                double targetPct = Double.parseDouble(tokens[0]);
                double currentPct = Double.parseDouble(tokens[1]);
                double unitPrice = Double.parseDouble(tokens[2]);

                securities.add(new Security(ticker, targetPct, currentPct, unitPrice));
            }
        }
        return securities;
    }

    public static double getRequiredTargetAllocationTotal() {
        String val = properties.getProperty("validation.requiredTargetAllocationTotal");
        return val != null ? Double.parseDouble(val) : 100.0; // Fallback default if missing
    }

    public static double getMinimumAllowableUnitPrice() {
        String val = properties.getProperty("validation.minimumAllowableUnitPrice");
        return val != null ? Double.parseDouble(val) : 0.0; // Fallback default if missing
    }

    public static Map<String, Double> getExpectedTrades() {
        Map<String, Double> expectedMap = new HashMap<>();
        for (String key : properties.stringPropertyNames()) {
            if (key.startsWith("expected.trade.")) {
                String ticker = key.replace("expected.trade.", "");
                double expectedShares = Double.parseDouble(properties.getProperty(key));
                expectedMap.put(ticker, expectedShares);
            }
        }
        return expectedMap;
    }
    public static List<Security> getSecuritiesWithBadSum() {
        List<Security> list = new ArrayList<>();
        for (String key : properties.stringPropertyNames()) {
            if (key.startsWith("badsum.security.")) {
                String ticker = key.replace("badsum.security.", "");
                String[] values = properties.getProperty(key).split(",");
                double targetPct = Double.parseDouble(values[0]);
                double currentPct = Double.parseDouble(values[1]);
                double unitPrice = Double.parseDouble(values[2]);
                list.add(new Security(ticker, targetPct, currentPct, unitPrice));
            }
        }
        return list;
    }

    public static List<Security> getSecuritiesWithBadPrice() {
        List<Security> list = new ArrayList<>();
        for (String key : properties.stringPropertyNames()) {
            if (key.startsWith("badprice.security.")) {
                String ticker = key.replace("badprice.security.", "");
                String[] values = properties.getProperty(key).split(",");
                double targetPct = Double.parseDouble(values[0]);
                double currentPct = Double.parseDouble(values[1]);
                double unitPrice = Double.parseDouble(values[2]);
                list.add(new Security(ticker, targetPct, currentPct, unitPrice));
            }
        }
        return list;
    }
}
