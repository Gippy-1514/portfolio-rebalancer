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
        String val = properties.getProperty("totalAssets");
        if (val == null) {
            throw new RuntimeException("totalAssets' key not found in config.properties.");
        }
        return Double.parseDouble(val.trim());
    }

    public static double getRequiredTargetAllocationTotal() {
        String val = properties.getProperty("validation.requiredTargetAllocationTotal");
        return val != null ? Double.parseDouble(val.trim()) : 100.0;
    }

    public static double getMinimumAllowableUnitPrice() {
        String val = properties.getProperty("validation.minimumAllowableUnitPrice");
        return val != null ? Double.parseDouble(val.trim()) : 0.0;
    }

    public static List<Security> getSecuritiesFromConfig() {
        List<Security> securities = new ArrayList<>();
        String[] tickers = {"IBM", "MSFT", "ORCL", "AAPL", "HD"};

        for (String ticker : tickers) {
            String propertyValue = properties.getProperty("security." + ticker);
            if (propertyValue != null) {
                String[] tokens = propertyValue.split(",");
                securities.add(new Security(
                        ticker,
                        Double.parseDouble(tokens[0].trim()),
                        Double.parseDouble(tokens[1].trim()),
                        Double.parseDouble(tokens[2].trim())
                ));
            }
        }
        return securities;
    }

    public static List<Security> getSecuritiesWithBadSum() {
        return parseSecuritiesByPrefix("badsum.security.");
    }

    public static List<Security> getSecuritiesWithBadPrice() {
        return parseSecuritiesByPrefix("badprice.security.");
    }

    public static Map<String, Double> getExpectedTrades() {
        Map<String, Double> expectedMap = new HashMap<>();
        for (String key : properties.stringPropertyNames()) {
            if (key.startsWith("expected.trade.")) {
                String ticker = key.replace("expected.trade.", "").trim();
                double value = Double.parseDouble(properties.getProperty(key).trim());
                expectedMap.put(ticker, value);
            }
        }
        return expectedMap;
    }

    private static List<Security> parseSecuritiesByPrefix(String prefix) {
        List<Security> list = new ArrayList<>();
        for (String key : properties.stringPropertyNames()) {
            if (key.startsWith(prefix)) {
                String[] values = properties.getProperty(key).split(",");
                String ticker = key.replace(prefix, "").trim();
                list.add(new Security(
                        ticker,
                        Double.parseDouble(values[0].trim()),
                        Double.parseDouble(values[1].trim()),
                        Double.parseDouble(values[2].trim())
                ));
            }
        }
        return list;
    }
}