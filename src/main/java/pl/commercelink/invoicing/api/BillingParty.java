package pl.commercelink.invoicing.api;

public record BillingParty(
        String id,
        String name,
        String surname,
        String company,
        String streetAndNumber,
        String postalCode,
        String city,
        String country,
        String taxNo,
        String shortcut
) {

    public boolean hasTaxId() {
        return taxNo != null && !taxNo.isBlank();
    }

    public boolean hasCompanyDetails() {
        return isNotBlank(company) &&
                isNotBlank(streetAndNumber) &&
                isNotBlank(postalCode) &&
                isNotBlank(city) &&
                isNotBlank(country) &&
                isNotBlank(taxNo);
    }

    public static BillingParty company(String id, String companyName, String streetAndNumber, String postalCode, String city, String country, String taxNo, String shortcut) {
        return new BillingParty(id, null, null, companyName, streetAndNumber, postalCode, city, country, taxNo, shortcut);
    }

    public static BillingParty individual(String id, String name, String surname, String streetAndNumber, String postalCode, String city, String country, String shortcut) {
        return new BillingParty(id, name, surname, null, streetAndNumber, postalCode, city, country, null, shortcut);
    }

    public boolean hasShortcut() {
        return isNotBlank(shortcut);
    }

    private boolean isNotBlank(String str) {
        return str != null && !str.isBlank();
    }
}
