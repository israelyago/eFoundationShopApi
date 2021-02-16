package core.order

data class OrderCreateUseCaseRequest(
    val token: String,
    val creditCard: CreditCard,
    val billingDetails: BillingDetails,
)
