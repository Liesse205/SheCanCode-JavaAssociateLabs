package com.shecan.lab21.scenario1_ecommerce_analytics.exercise23_custom_collector;

import com.shecan.lab21.scenario1_ecommerce_analytics.model.LineItem;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class CustomRevenueCollector implements Collector<LineItem, CustomRevenueCollector.Accumulator, RevenueReport> {

    /**
     * Internal accumulator class that holds intermediate state
     */
    public static class Accumulator {
        private long itemCount = 0;
        private double totalRevenue = 0.0;
        private double maxSingleItemRevenue = 0.0;

        public void add(LineItem item) {
            itemCount++;
            double revenue = item.getRevenue();
            totalRevenue += revenue;
            if (revenue > maxSingleItemRevenue) {
                maxSingleItemRevenue = revenue;
            }
        }

        public Accumulator combine(Accumulator other) {
            this.itemCount += other.itemCount;
            this.totalRevenue += other.totalRevenue;
            this.maxSingleItemRevenue = Math.max(this.maxSingleItemRevenue, other.maxSingleItemRevenue);
            return this;
        }

        public RevenueReport toReport() {
            return new RevenueReport(itemCount, totalRevenue, maxSingleItemRevenue);
        }
    }

    @Override
    public Supplier<Accumulator> supplier() {
        return Accumulator::new;
    }

    @Override
    public BiConsumer<Accumulator, LineItem> accumulator() {
        return Accumulator::add;
    }

    @Override
    public BinaryOperator<Accumulator> combiner() {
        return Accumulator::combine;
    }

    @Override
    public Function<Accumulator, RevenueReport> finisher() {
        return Accumulator::toReport;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.CONCURRENT, Characteristics.UNORDERED);
    }
}