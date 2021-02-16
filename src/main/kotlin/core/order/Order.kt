package core.order

import com.google.gson.annotations.SerializedName
import core.cart.Cart

data class Order(
    val id: Long,
    @SerializedName("user_token")
    val userToken: String,
    val cart: Cart,
    val status: OrderStatus,
    val complete: Boolean,
)

