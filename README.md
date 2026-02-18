# Invoicing API

This library defines a provider-agnostic API for integrating external invoicing systems into the CommerceLink platform. It provides a common set of interfaces and data models that invoicing provider implementations must adhere to, enabling seamless swapping or coexistence of different invoicing backends.

The core `InvoicingProvider` interface supports creating invoices, fetching invoices by ID or number, retrieving invoice PDFs, and looking up billing parties and cost centers. The API covers standard, advance, and final invoice workflows along with supporting concepts such as billing parties, invoice positions, pricing with VAT, and split payment policies.

## Provider Discovery

This library extends the [provider-api](https://github.com/commercelink/commercelink-provider-api) plugin system. The `InvoicingProviderDescriptor` interface extends `ProviderDescriptor<InvoicingProvider>` and serves as the SPI entry point for pluggable invoicing implementations.

Concrete implementations are discovered at runtime via `ServiceLoader`. See the [provider-api README](https://github.com/commercelink/commercelink-provider-api) for registration details.
