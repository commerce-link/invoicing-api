package pl.commercelink.invoicing.api;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Price {

    public static final double DEFAULT_VAT_RATE = 1.23;
    public static final String DEFAULT_CURRENCY = "PLN";

    private final BigDecimal netValue;
    private final BigDecimal grossValue;
    private final BigDecimal vatRate;
    private final String currency;

    public static Price fromNet(double priceNet) {
        BigDecimal net = scale(BigDecimal.valueOf(priceNet));
        BigDecimal vat = BigDecimal.valueOf(DEFAULT_VAT_RATE);
        return new Price(net, asGrossValue(net, vat), vat, DEFAULT_CURRENCY);
    }

    public static Price fromNet(double priceNet, double vatRate) {
        BigDecimal net = scale(BigDecimal.valueOf(priceNet));
        BigDecimal vat = BigDecimal.valueOf(vatRate);
        return new Price(net, asGrossValue(net, vat), vat, DEFAULT_CURRENCY);
    }

    public static Price fromGross(double priceGross) {
        BigDecimal gross = scale(BigDecimal.valueOf(priceGross));
        BigDecimal vat = BigDecimal.valueOf(DEFAULT_VAT_RATE);
        return new Price(asNetValue(gross, vat), gross, vat, DEFAULT_CURRENCY);
    }

    public static Price fromGross(double priceGross, double vatRate) {
        BigDecimal gross = scale(BigDecimal.valueOf(priceGross));
        BigDecimal vat = BigDecimal.valueOf(vatRate);
        return new Price(asNetValue(gross, vat), gross, vat, DEFAULT_CURRENCY);
    }

    public static Price max(Price price1, Price price2) {
        if (price1 == null) return price2;
        if (price2 == null) return price1;
        return price1.netValue.compareTo(price2.netValue) >= 0 ? price1 : price2;
    }

    public Price(double netValue, double grossValue) {
        this.netValue = scale(BigDecimal.valueOf(netValue));
        this.grossValue = scale(BigDecimal.valueOf(grossValue));
        this.vatRate = BigDecimal.valueOf(DEFAULT_VAT_RATE);
        this.currency = DEFAULT_CURRENCY;
    }

    public Price(double netValue, double grossValue, String currency) {
        this.netValue = scale(BigDecimal.valueOf(netValue));
        this.grossValue = scale(BigDecimal.valueOf(grossValue));
        this.vatRate = BigDecimal.valueOf(DEFAULT_VAT_RATE);
        this.currency = currency;
    }

    public Price(double netValue, double grossValue, double vatRate) {
        this.netValue = scale(BigDecimal.valueOf(netValue));
        this.grossValue = scale(BigDecimal.valueOf(grossValue));
        this.vatRate = BigDecimal.valueOf(vatRate);
        this.currency = DEFAULT_CURRENCY;
    }

    private Price(BigDecimal netValue, BigDecimal grossValue, BigDecimal vatRate, String currency) {
        this.netValue = netValue;
        this.grossValue = grossValue;
        this.vatRate = vatRate;
        this.currency = currency;
    }

    private static BigDecimal asNetValue(BigDecimal priceGross, BigDecimal vatRate) {
        return priceGross.divide(vatRate, 2, RoundingMode.HALF_UP);
    }

    private static BigDecimal asGrossValue(BigDecimal priceNet, BigDecimal vatRate) {
        return priceNet.multiply(vatRate).setScale(2, RoundingMode.HALF_UP);
    }

    private static BigDecimal scale(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }

    public Price times(int quantity) {
        BigDecimal qty = BigDecimal.valueOf(quantity);
        return new Price(netValue.multiply(qty), grossValue.multiply(qty), vatRate, currency);
    }

    public double netValue() {
        return netValue.doubleValue();
    }

    public double grossValue() {
        return grossValue.doubleValue();
    }

    public double vatRate() {
        return vatRate.doubleValue();
    }

    public int vatRatePercent() {
        return vatRate.subtract(BigDecimal.ONE).multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP).intValue();
    }

    public String currency() {
        return currency;
    }

    @Override
    public String toString() {
        return "Price{" +
                "net=" + netValue +
                ", gross=" + grossValue +
                ", vatRate=" + vatRate +
                ", currency='" + currency + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return Objects.equals(netValue, price.netValue) && Objects.equals(grossValue, price.grossValue) && Objects.equals(vatRate, price.vatRate) && Objects.equals(currency, price.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(netValue, grossValue, vatRate, currency);
    }
}
