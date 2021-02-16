package core.user

import core.cart.Cart

class UserLoginUseCaseImpl(
    private val userRepository: UserRepository
) : UserLoginUseCase {
    override fun execute(request: UserLoginRequest): UserLoginResponse {
        val user = userRepository.getByCredentials(request.credentials)
            ?: return UserLoginResponse.CredentialError(invalidCredentials = true)

        return UserLoginResponse.Success(
            token = "dasda",
            user = user,
            cart = Cart(
                items = listOf()
            ),
            orders = listOf(),
        )
    }
}