package core.cart

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

internal class CartUpdateUseCaseImplTest {

    private val cartRepository: CartRepository = CacheCartRepository()
    private val cartUpdateUseCaseImpl = CartUpdateUseCaseImpl(cartRepository = cartRepository)

    @Test
    fun `should create cart if there is none and update when there is one`() {

        val token = "totally-valid-token"
        assertNull(cartRepository.getCartByToken(token))

        val request = CartUpdateRequest(
            token = token,
            cart = Cart(
                items = listOf(
                    CartItem(
                        productId = 1,
                        quantity = 2
                    ),
                    CartItem(
                        productId = 3,
                        quantity = 1
                    )
                )
            )
        )
        val response = cartUpdateUseCaseImpl.execute(request)
        assert(response is CartUpdateUseCaseResponse.Success)

        assertNotNull(cartRepository.getCartByToken(token))
        assertEquals(request.cart, cartRepository.getCartByToken(token))

        val newRequest = request.copy(cart = Cart(items = listOf()))
        val responseForClearingCart = cartUpdateUseCaseImpl.execute(newRequest)
        assert(responseForClearingCart is CartUpdateUseCaseResponse.Success)

        assertNotNull(cartRepository.getCartByToken(token))
        assertEquals(newRequest.cart, cartRepository.getCartByToken(token))
    }
}