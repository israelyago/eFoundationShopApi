package core.user

import core.cart.Cart
import core.order.Order

sealed class UserLoginResponse {
    data class Success(
        val token: String,
        val user: User,
        val cart: Cart,
        val orders: List<Order>,
    ): UserLoginResponse()

    data class CredentialError(val invalidCredentials: Boolean): UserLoginResponse()
}
