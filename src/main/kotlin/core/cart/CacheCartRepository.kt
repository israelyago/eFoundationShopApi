package core.cart

class CacheCartRepository : CartRepository {
    private val repository = mutableMapOf<String, Cart>()
    override fun getCartByToken(token: String): Cart? {
        return repository[token]
    }

    override fun setCart(token: String, cart: Cart): Boolean {
        repository[token] = cart
        return true
    }

}
