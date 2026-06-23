package com.shecan.lab21.scenario1_ecommerce_analytics.exercise23_custom_collector;

public class RevenueReport {
    private final long itemCount;
    private final double totalRevenue;
    private final double maxSingleItemRevenue;

    public RevenueReport(long itemCount, double totalRevenue, double maxSingleItemRevenue) {
        this.itemCount = itemCount;
        this.totalRevenue = totalRevenue;
        this.maxSingleItemRevenue = maxSingleItemRevenue;
    }

    public long getItemCount() { return itemCount; }
    public double getTotalRevenue() { return totalRevenue; }
    public double getMaxSingleItemRevenue() { return maxSingleItemRevenue; }

    @Override
    public String toString() {
        return "RevenueReport{" +
                "itemCount=" + itemCount +
                ", totalRevenue=" + totalRevenue +
                ", maxSingleItemRevenue=" + maxSingleItemRevenue +
                '}';
    }
}