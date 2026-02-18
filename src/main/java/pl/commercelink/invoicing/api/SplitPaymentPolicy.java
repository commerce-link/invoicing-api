package pl.commercelink.invoicing.api;

public class SplitPaymentPolicy {

    public static boolean isRequired(BillingParty billingParty, double grossInvoiceValue, boolean splitPaymentsEnabled) {
        if (billingParty == null || !billingParty.hasTaxId()) {
            return false;
        }

        if (!splitPaymentsEnabled) {
            return false;
        }

        return grossInvoiceValue >= 15000;
    }

}
