package com.crd.rebalancer;

public class Security {

        private final String ticker;
        private final double targetPct;
        private final double currentPct;
        private final double unitPrice;

        public Security(String ticker, double targetPct, double currentPct, double unitPrice) {
            this.ticker = ticker;
            this.targetPct = targetPct;
            this.currentPct = currentPct;
            this.unitPrice = unitPrice;
        }

        public String getTicker() {
            return ticker;
        }
        public double getTargetPct()
        { return targetPct;
        }
        public double getCurrentPct() {
            return currentPct;
        }
        public double getUnitPrice() {
            return unitPrice;
        }

        // Target Variance = Current% - Target%
        public double getVariance() {
            return this.currentPct - this.targetPct;
        }
    }

