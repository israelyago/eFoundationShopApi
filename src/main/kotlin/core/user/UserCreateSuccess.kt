package core.user

import core.cart.Cart

data class UserCreateSuccess(
    val token: String,
    val user: User,
    val cart: Cart,
)
