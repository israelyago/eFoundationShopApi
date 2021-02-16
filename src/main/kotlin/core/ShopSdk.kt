package core

import core.cart.CartUpdateUseCase
import core.order.OrderCreateUseCase
import core.order.OrderRepository
import core.smartphone.ProductRepository
import core.smartphone.SmartphoneCreateUseCase
import core.user.UserCreateUseCase
import core.user.UserLoginUseCase
import core.user.UserRepository

interface ShopSdk {
    val tokenGenerator: TokenGenerator
    val cartUpdateUseCase: CartUpdateUseCase
    val userCreateUseCase: UserCreateUseCase
    val userLoginUseCase: UserLoginUseCase
    val smartphoneCreateUseCase: SmartphoneCreateUseCase
    val productRepository: ProductRepository
    val orderRepository: OrderRepository
    val orderCreateUseCase: OrderCreateUseCase
    val userRepository: UserRepository
}