package core

import core.cart.CacheCartRepository
import core.cart.CartRepository
import core.cart.CartUpdateUseCase
import core.cart.CartUpdateUseCaseImpl
import core.order.CacheOrderRepository
import core.order.OrderCreateUseCase
import core.order.OrderCreateUseCaseImpl
import core.order.OrderRepository
import core.smartphone.CacheProductRepository
import core.smartphone.ProductRepository
import core.smartphone.SmartphoneCreateUseCase
import core.smartphone.SmartphoneCreateUseCaseImpl
import core.user.*

class DevShop(): ShopSdk {
    override val userRepository: UserRepository = CacheUserRepository()
    private val cartRepository: CartRepository = CacheCartRepository()
    override val orderRepository: OrderRepository = CacheOrderRepository()
    override val tokenGenerator: TokenGenerator = UniqueTokenGenerator()
    override val cartUpdateUseCase: CartUpdateUseCase = CartUpdateUseCaseImpl(cartRepository)
    override val userCreateUseCase: UserCreateUseCase = UserCreateUseCaseImpl(
        tokenGenerator = tokenGenerator,
        cartRepository = cartRepository,
        userRepository = userRepository
    )
    override val productRepository: ProductRepository = CacheProductRepository()
    override val orderCreateUseCase: OrderCreateUseCase = OrderCreateUseCaseImpl(
        orderRepository = orderRepository,
        cartRepository = cartRepository,
    )
    override val userLoginUseCase: UserLoginUseCase = UserLoginUseCaseImpl(userRepository = userRepository)
    override val smartphoneCreateUseCase: SmartphoneCreateUseCase = SmartphoneCreateUseCaseImpl(
        productRepository = productRepository
    )

}