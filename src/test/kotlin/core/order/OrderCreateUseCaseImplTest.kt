package core.order

import core.cart.CacheCartRepository
import core.cart.Cart
import core.cart.CartItem
import core.cart.CartRepository
import org.junit.jupiter.api.Test

internal class OrderCreateUseCaseImplTest() {
    private val token = "totally-valid-token"
    private val orderRepository: OrderRepository = CacheOrderRepository()
    private val cartRepository: CartRepository = CacheCartRepository()

    private val orderCreateUseCase: OrderCreateUseCase = OrderCreateUseCaseImpl(
        orderRepository = orderRepository,
        cartRepository = cartRepository,
    )
    private val creditCard = CreditCard(
        cardNumber = "1111222233334444",
        expiryDate = "11/28",
        cvc = "123"
    )
    private val billingDetails = BillingDetails(
        id = 0,
        firstName = "John",
        lastName = "Doe",
        streetAddress = "Baker Street",
        postCode = "NW1 6XE",
        city = "London",
        email = "johndoe@fake.email.com",
        phone = "1122344445555"
    )
    private val cart = Cart(
        items = listOf(
            CartItem(
                productId = 284L,
                quantity = 2
            ),
            CartItem(
                productId = 285L,
                quantity = 3
            )
        )
    )
    @Test
    fun `should create order when everything is valid`() {
        // given
        val request = OrderCreateUseCaseRequest(
            token = token,
            creditCard = creditCard,
            billingDetails = billingDetails,
        )

        cartRepository.setCart(
            token = token,
            cart = cart,
        )

        // when
        val response = orderCreateUseCase.execute(request)
        // then
        assert(response is OrderCreateUseCaseResponse.Success)
    }
}