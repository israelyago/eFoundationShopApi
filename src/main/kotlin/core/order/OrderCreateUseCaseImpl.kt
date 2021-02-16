package core.order

import core.cart.CartRepository
import kotlin.random.Random

class OrderCreateUseCaseImpl(
    private val orderRepository: OrderRepository,
    private val cartRepository: CartRepository
) : OrderCreateUseCase {
    override fun execute(request: OrderCreateUseCaseRequest): OrderCreateUseCaseResponse {

        // Here would be all the validation of the token, credit card, billing details, etc

        val cart = cartRepository.getCartByToken(request.token)?:
            return OrderCreateUseCaseResponse.Error(OrderCreateError.CART_DOES_NOT_EXIST)

        orderRepository.create(
            order = Order(
                id = Random.nextLong(),
                userToken = request.token,
                cart = cart,
                status = OrderStatus.PLACED,
                complete = false,
            )
        )
        return OrderCreateUseCaseResponse.Success()
    }
}