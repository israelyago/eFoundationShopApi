package core.cart

data class CartUpdateRequest(
    val token: String,
    val cart: Cart,
)
