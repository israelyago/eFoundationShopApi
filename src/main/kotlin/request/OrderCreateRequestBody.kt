package request

import com.google.gson.annotations.SerializedName
import core.order.BillingDetails
import core.order.CreditCard

data class OrderCreateRequestBody(
    @SerializedName("credit_card")
    val creditCard: CreditCard,
    @SerializedName("billing_details")
    val billingDetails: BillingDetails,
)
