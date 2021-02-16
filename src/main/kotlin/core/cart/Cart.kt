package core.cart

data class Cart(
    val items: List<CartItem>
)

data class CartItem(
    val productId: Long,
    val quantity: Int
)