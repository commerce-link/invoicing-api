package pl.commercelink.invoicing.api;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public record Invoice(
        String id,
        String number,
        String orderId,
        Price amount,
        String viewUrl,
        String currency,
        double exchangeRate,
        boolean paid,
        LocalDate paymentToDate,
        List<InvoicePosition> positions,
        BillingParty seller,
        BillingParty buyer,
        boolean partiallyPaid,
        boolean approved
) {
    public Invoice {
        positions = positions != null ? List.copyOf(positions) : List.of();
    }

    /**
     * Kept for the adapters built before {@code partiallyPaid} and {@code approved}: they report an invoice as paid in
     * full or not at all, and have no approval step, so none of their invoices counts as approved.
     */
    public Invoice(String id, String number, String orderId, Price amount, String viewUrl, String currency,
                   double exchangeRate, boolean paid, LocalDate paymentToDate, List<InvoicePosition> positions,
                   BillingParty seller, BillingParty buyer) {
        this(id, number, orderId, amount, viewUrl, currency, exchangeRate, paid, paymentToDate, positions, seller, buyer, false, false);
    }

    public boolean unpaid() {
        return !paid && !partiallyPaid;
    }

    public boolean hasOrderId(String other) {
        if (orderId == null || orderId.isBlank() || other == null || other.isBlank()) {
            return false;
        }
        return Arrays.stream(orderId.split(","))
                .map(String::trim)
                .anyMatch(id -> id.equalsIgnoreCase(other));
    }
}