package core.cart

interface CartRepository {
    fun getCartByToken(token: String): Cart?
    fun setCart(token: String, cart: Cart): Boolean
}
