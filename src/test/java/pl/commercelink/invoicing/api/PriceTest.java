package pl.commercelink.invoicing.api;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.commercelink.invoicing.api.Price.DEFAULT_CURRENCY;
import static pl.commercelink.invoicing.api.Price.DEFAULT_VAT_RATE;

class PriceTest {

    @Nested
    class FromPriceNet {

        @Test
        void shouldCalculateGrossFromNet() {
            Price price = Price.fromNet(100);
            assertThat(price.netValue()).isEqualTo(100.0);
            assertThat(price.grossValue()).isEqualTo(123.0);
        }

        @Test
        void shouldCalculateGrossFromNetWithDecimals() {
            Price price = Price.fromNet(81.30);
            assertThat(price.grossValue()).isEqualTo(100.00);
        }

        @Test
        void shouldUseDefaultVatRate() {
            Price price = Price.fromNet(100);
            assertThat(price.vatRate()).isEqualTo(DEFAULT_VAT_RATE);
        }

        @Test
        void shouldUseDefaultCurrency() {
            Price price = Price.fromNet(100);
            assertThat(price.currency()).isEqualTo(DEFAULT_CURRENCY);
        }

        @Test
        void shouldHandleZero() {
            Price price = Price.fromNet(0);
            assertThat(price.netValue()).isEqualTo(0.0);
            assertThat(price.grossValue()).isEqualTo(0.0);
        }

        @Test
        void shouldUseCustomVatRate() {
            Price price = Price.fromNet(100, 1.08);
            assertThat(price.grossValue()).isEqualTo(108.0);
            assertThat(price.vatRate()).isEqualTo(1.08);
        }

        @Test
        void shouldRoundGrossToTwoDecimalPlaces() {
            Price price = Price.fromNet(33.33);
            assertThat(price.grossValue()).isEqualTo(41.0);
        }
    }

    @Nested
    class FromPriceGross {

        @Test
        void shouldCalculateNetFromGross() {
            Price price = Price.fromGross(123);
            assertThat(price.grossValue()).isEqualTo(123.0);
            assertThat(price.netValue()).isEqualTo(100.0);
        }

        @Test
        void shouldCalculateNetFromGrossWithDecimals() {
            Price price = Price.fromGross(1823.44);
            assertThat(price.netValue()).isEqualTo(1482.47);
        }

        @Test
        void shouldHandleZero() {
            Price price = Price.fromGross(0);
            assertThat(price.netValue()).isEqualTo(0.0);
            assertThat(price.grossValue()).isEqualTo(0.0);
        }

        @Test
        void shouldUseCustomVatRate() {
            Price price = Price.fromGross(108, 1.08);
            assertThat(price.netValue()).isEqualTo(100.0);
            assertThat(price.vatRate()).isEqualTo(1.08);
        }

        @Test
        void shouldRoundNetToTwoDecimalPlaces() {
            Price price = Price.fromGross(10);
            assertThat(price.netValue()).isEqualTo(8.13);
        }
    }

    @Nested
    class Constructors {

        @Test
        void shouldCreateWithNetAndGross() {
            Price price = new Price(100, 123);
            assertThat(price.netValue()).isEqualTo(100.0);
            assertThat(price.grossValue()).isEqualTo(123.0);
            assertThat(price.vatRate()).isEqualTo(DEFAULT_VAT_RATE);
            assertThat(price.currency()).isEqualTo(DEFAULT_CURRENCY);
        }

        @Test
        void shouldCreateWithNetGrossAndCurrency() {
            Price price = new Price(100, 119, "EUR");
            assertThat(price.currency()).isEqualTo("EUR");
            assertThat(price.netValue()).isEqualTo(100.0);
            assertThat(price.grossValue()).isEqualTo(119.0);
        }

        @Test
        void shouldCreateWithNetGrossAndVatRate() {
            Price price = new Price(100, 108, 1.08);
            assertThat(price.vatRate()).isEqualTo(1.08);
            assertThat(price.currency()).isEqualTo(DEFAULT_CURRENCY);
        }

        @Test
        void shouldScaleInputsToTwoDecimalPlaces() {
            Price price = new Price(100.999, 123.001);
            assertThat(price.netValue()).isEqualTo(101.0);
            assertThat(price.grossValue()).isEqualTo(123.0);
        }
    }

    @Nested
    class Times {

        @Test
        void shouldMultiplyPriceByQuantity() {
            Price unit = Price.fromNet(100);
            Price total = unit.times(3);
            assertThat(total.netValue()).isEqualTo(300.0);
            assertThat(total.grossValue()).isEqualTo(369.0);
        }

        @Test
        void shouldMultiplyByOne() {
            Price unit = Price.fromNet(50.5);
            Price total = unit.times(1);
            assertThat(total.netValue()).isEqualTo(50.5);
            assertThat(total.grossValue()).isEqualTo(unit.grossValue());
        }

        @Test
        void shouldMultiplyByZero() {
            Price unit = Price.fromNet(100);
            Price total = unit.times(0);
            assertThat(total.netValue()).isEqualTo(0.0);
            assertThat(total.grossValue()).isEqualTo(0.0);
        }

        @Test
        void shouldPreserveVatRateAndCurrency() {
            Price unit = new Price(100, 108, "EUR");
            Price total = unit.times(5);
            assertThat(total.vatRate()).isEqualTo(DEFAULT_VAT_RATE);
            assertThat(total.currency()).isEqualTo("EUR");
        }

        @Test
        void shouldMaintainTwoDecimalPlacePrecision() {
            Price unit = Price.fromNet(33.33);
            Price total = unit.times(3);
            assertThat(total.netValue()).isEqualTo(99.99);
            assertThat(total.grossValue()).isEqualTo(123.0);
        }
    }

    @Nested
    class Max {

        @Test
        void shouldReturnHigherPrice() {
            Price low = Price.fromNet(50);
            Price high = Price.fromNet(100);
            assertThat(Price.max(low, high)).isEqualTo(high);
            assertThat(Price.max(high, low)).isEqualTo(high);
        }

        @Test
        void shouldReturnFirstWhenEqual() {
            Price a = Price.fromNet(100);
            Price b = Price.fromNet(100);
            assertThat(Price.max(a, b)).isSameAs(a);
        }

        @Test
        void shouldHandleNullFirst() {
            Price price = Price.fromNet(100);
            assertThat(Price.max(null, price)).isEqualTo(price);
        }

        @Test
        void shouldHandleNullSecond() {
            Price price = Price.fromNet(100);
            assertThat(Price.max(price, null)).isEqualTo(price);
        }

        @Test
        void shouldHandleBothNull() {
            assertThat(Price.max(null, null)).isNull();
        }
    }

    @Nested
    class VatRatePercent {

        @Test
        void shouldReturn23ForDefaultVatRate() {
            Price price = Price.fromNet(100);
            assertThat(price.vatRatePercent()).isEqualTo(23);
        }

        @Test
        void shouldReturn8For1_08VatRate() {
            Price price = Price.fromNet(100, 1.08);
            assertThat(price.vatRatePercent()).isEqualTo(8);
        }

        @Test
        void shouldReturn5For1_05VatRate() {
            Price price = Price.fromNet(100, 1.05);
            assertThat(price.vatRatePercent()).isEqualTo(5);
        }

        @Test
        void shouldReturn0For1_0VatRate() {
            Price price = Price.fromNet(100, 1.0);
            assertThat(price.vatRatePercent()).isEqualTo(0);
        }
    }

    @Nested
    class Equality {

        @Test
        void shouldBeEqualForSameValues() {
            Price a = Price.fromNet(100);
            Price b = Price.fromNet(100);
            assertThat(a).isEqualTo(b);
            assertThat(a.hashCode()).isEqualTo(b.hashCode());
        }

        @Test
        void shouldNotBeEqualForDifferentNet() {
            Price a = Price.fromNet(100);
            Price b = Price.fromNet(200);
            assertThat(a).isNotEqualTo(b);
        }

        @Test
        void shouldNotBeEqualForDifferentVatRate() {
            Price a = Price.fromNet(100, 1.23);
            Price b = Price.fromNet(100, 1.08);
            assertThat(a).isNotEqualTo(b);
        }

        @Test
        void shouldNotBeEqualForDifferentCurrency() {
            Price a = new Price(100, 123, "PLN");
            Price b = new Price(100, 123, "EUR");
            assertThat(a).isNotEqualTo(b);
        }

        @Test
        void shouldNotBeEqualToNull() {
            Price price = Price.fromNet(100);
            assertThat(price).isNotEqualTo(null);
        }
    }

    @Nested
    class Rounding {

        @Test
        void shouldRoundNetToTwoDecimalPlacesFromGross() {
            Price price = Price.fromGross(4847.33);
            assertThat(price.netValue()).isEqualTo(3940.92);
        }

        @Test
        void shouldRoundGrossToTwoDecimalPlacesFromNet() {
            Price price = Price.fromNet(1652.85);
            assertThat(price.grossValue()).isEqualTo(2033.01);
        }

        @Test
        void shouldRoundInputsInConstructor() {
            Price price = new Price(10.555, 12.985);
            assertThat(price.netValue()).isEqualTo(10.56);
            assertThat(price.grossValue()).isEqualTo(12.99);
        }

        @Test
        void shouldRoundInputsInFactoryMethods() {
            Price price = Price.fromNet(10.555);
            assertThat(price.netValue()).isEqualTo(10.56);
        }

        @Test
        void shouldMaintainPrecisionAfterTimes() {
            Price unit = new Price(10.11, 12.44);
            Price total = unit.times(7);
            assertThat(total.netValue()).isEqualTo(70.77);
            assertThat(total.grossValue()).isEqualTo(87.08);
        }
    }
}
