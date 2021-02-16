package core.order

data class BillingDetails(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val streetAddress: String,
    val postCode: String,
    val city: String,
    val email: String,
    val phone: String
)