package pl.commercelink.invoicing.api;

import java.util.List;

public interface InvoicingProvider {

    Invoice createInvoice(InvoiceRequest request);

    /** Returns invoice with positions, or null if not found. */
    Invoice fetchInvoiceById(String invoiceId, InvoiceDirection direction);

    /** Returns invoices matching the order ID, or empty list if none found. */
    List<Invoice> fetchInvoicesByOrderId(String orderId, InvoiceDirection direction);

    /**
     * Returns the purchase invoices of the order that are still waiting for approval. A provider without an approval
     * step returns every purchase invoice of the order.
     */
    default List<Invoice> fetchPurchaseInvoicesAwaitingApprovalByOrderId(String orderId) {
        return fetchInvoicesByOrderId(orderId, InvoiceDirection.Purchase);
    }

    /** Marks the purchase invoice as approved. A provider without an approval step does nothing. */
    default void approvePurchaseInvoice(String invoiceId) {
    }

    /** Returns raw PDF bytes for the invoice. */
    byte[] fetchInvoicePdf(String invoiceId);

    /** Returns the company (cost center) for this provider, or null if not found. */
    BillingParty fetchCostCenterById(String costCenterId);

    /** Returns billing party by provider-specific ID, or null if not found. */
    BillingParty fetchBillingPartyById(String billingPartyId);

    /** Returns billing party by shortcut/alias, or null if not found. */
    BillingParty fetchBillingPartyByShortcut(String billingPartyShortcut);
}
