package pl.commercelink.invoicing.api;

import java.time.LocalDate;
import java.util.List;

public interface InvoicingProvider {

    Invoice createInvoice(InvoiceRequest request);

    Invoice fetchInvoiceById(String invoiceId, InvoiceDirection direction);

    List<Invoice> fetchInvoices(LocalDate dateFrom, LocalDate dateTo, InvoiceDirection direction);

    byte[] fetchInvoicePdf(String invoiceId);

    BillingParty fetchCostCenterById(String costCenterId);

    BillingParty fetchBillingPartyById(String billingPartyId);

    BillingParty fetchBillingPartyByShortcut(String billingPartyShortcut);
}
