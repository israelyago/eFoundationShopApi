package core.order

import com.google.gson.annotations.SerializedName

data class CreditCard(
    @SerializedName("card_number")
    val cardNumber: String,
    @SerializedName("expiry_date")
    val expiryDate: String,
    val cvc: String
)
