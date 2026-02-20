package pl.commercelink.invoicing.api;

import java.time.LocalDate;
import java.util.List;

public record InvoiceRequest(
        InvoiceKind invoiceKind,
        String orderId,
        LocalDate sellDate,
        BillingParty billingParty,
        List<InvoicePosition> positions,
        double paidAmount,
        String description,
        int paymentTerms,
        boolean splitPaymentsEnabled,
        boolean send,
        String wmsOrderNo,
        double leftToPay,
        List<String> invoiceNumbers
) {

    public static InvoiceBuilder standardInvoice() { return new InvoiceBuilder(); }
    public static AdvanceInvoiceBuilder advanceInvoice() { return new AdvanceInvoiceBuilder(); }
    public static FinalInvoiceBuilder finalInvoice() { return new FinalInvoiceBuilder(); }

    public static class InvoiceBuilder {
        private InvoiceKind invoiceKind;
        private String orderId;
        private LocalDate sellDate;
        private BillingParty billingParty;
        private List<InvoicePosition> positions;
        private double paidAmount;
        private String description;
        private int paymentTerms;
        private boolean splitPaymentsEnabled;
        private boolean send;

        public InvoiceBuilder invoiceKind(InvoiceKind invoiceKind) { this.invoiceKind = invoiceKind; return this; }
        public InvoiceBuilder orderId(String orderId) { this.orderId = orderId; return this; }
        public InvoiceBuilder sellDate(LocalDate sellDate) { this.sellDate = sellDate; return this; }
        public InvoiceBuilder billingParty(BillingParty billingParty) { this.billingParty = billingParty; return this; }
        public InvoiceBuilder positions(List<InvoicePosition> positions) { this.positions = positions; return this; }
        public InvoiceBuilder paidAmount(double paidAmount) { this.paidAmount = paidAmount; return this; }
        public InvoiceBuilder description(String description) { this.description = description; return this; }
        public InvoiceBuilder paymentTerms(int paymentTerms) { this.paymentTerms = paymentTerms; return this; }
        public InvoiceBuilder splitPaymentsEnabled(boolean splitPaymentsEnabled) { this.splitPaymentsEnabled = splitPaymentsEnabled; return this; }
        public InvoiceBuilder send(boolean send) { this.send = send; return this; }

        public InvoiceRequest build() {
            return new InvoiceRequest(invoiceKind, orderId, sellDate, billingParty, positions,
                    paidAmount, description, paymentTerms, splitPaymentsEnabled, send,
                    null, 0, null);
        }
    }

    public static class AdvanceInvoiceBuilder {
        private String orderId;
        private String wmsOrderNo;
        private LocalDate sellDate;
        private double paidAmount;
        private BillingParty billingParty;
        private boolean splitPaymentsEnabled;
        private boolean send;

        public AdvanceInvoiceBuilder orderId(String orderId) { this.orderId = orderId; return this; }
        public AdvanceInvoiceBuilder wmsOrderNo(String wmsOrderNo) { this.wmsOrderNo = wmsOrderNo; return this; }
        public AdvanceInvoiceBuilder sellDate(LocalDate sellDate) { this.sellDate = sellDate; return this; }
        public AdvanceInvoiceBuilder paidAmount(double paidAmount) { this.paidAmount = paidAmount; return this; }
        public AdvanceInvoiceBuilder billingParty(BillingParty billingParty) { this.billingParty = billingParty; return this; }
        public AdvanceInvoiceBuilder splitPaymentsEnabled(boolean splitPaymentsEnabled) { this.splitPaymentsEnabled = splitPaymentsEnabled; return this; }
        public AdvanceInvoiceBuilder send(boolean send) { this.send = send; return this; }

        public InvoiceRequest build() {
            return new InvoiceRequest(InvoiceKind.Advance, orderId, sellDate, billingParty, null,
                    paidAmount, null, 0, splitPaymentsEnabled, send,
                    wmsOrderNo, 0, null);
        }
    }

    public static class FinalInvoiceBuilder {
        private String orderId;
        private String wmsOrderNo;
        private BillingParty billingParty;
        private double leftToPay;
        private List<String> invoiceNumbers;
        private boolean splitPaymentsEnabled;
        private boolean send;

        public FinalInvoiceBuilder orderId(String orderId) { this.orderId = orderId; return this; }
        public FinalInvoiceBuilder wmsOrderNo(String wmsOrderNo) { this.wmsOrderNo = wmsOrderNo; return this; }
        public FinalInvoiceBuilder billingParty(BillingParty billingParty) { this.billingParty = billingParty; return this; }
        public FinalInvoiceBuilder leftToPay(double leftToPay) { this.leftToPay = leftToPay; return this; }
        public FinalInvoiceBuilder invoiceNumbers(List<String> invoiceNumbers) { this.invoiceNumbers = invoiceNumbers; return this; }
        public FinalInvoiceBuilder splitPaymentsEnabled(boolean splitPaymentsEnabled) { this.splitPaymentsEnabled = splitPaymentsEnabled; return this; }
        public FinalInvoiceBuilder send(boolean send) { this.send = send; return this; }

        public InvoiceRequest build() {
            return new InvoiceRequest(InvoiceKind.Final, orderId, null, billingParty, null,
                    0, null, 0, splitPaymentsEnabled, send,
                    wmsOrderNo, leftToPay, invoiceNumbers);
        }
    }
}
