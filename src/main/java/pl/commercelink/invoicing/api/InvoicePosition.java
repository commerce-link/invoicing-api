package pl.commercelink.invoicing.api;

public record InvoicePosition(String id, String name, int qty, Price price) {
    public Price totalPrice() {
        return price.times(qty);
    }
}
