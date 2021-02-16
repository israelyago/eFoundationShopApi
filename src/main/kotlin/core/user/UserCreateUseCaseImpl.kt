package core.user

import core.TokenGenerator
import core.cart.Cart
import core.cart.CartRepository

class UserCreateUseCaseImpl(
    private val userRepository: UserRepository,
    private val tokenGenerator: TokenGenerator,
    private val cartRepository: CartRepository
) : UserCreateUseCase {
    override fun execute(req: UserCreateUseCaseRequest): UserCreateUseCaseResponse {
        val user = req.user.copy(id = userRepository.getNextId())

        if (userRepository.getFromToken(req.token) != null) {
            return UserCreateUseCaseResponse.Error(UserCreateError.TOKEN_WAS_ALREADY_USED)
        }

        val userValidationError = UserValidator.validate(user)
        if (userValidationError != null) {
            return UserCreateUseCaseResponse.ValidationError(userValidationError)
        }

        if (!userRepository.create(req.token, user)) {
            return UserCreateUseCaseResponse.Error(UserCreateError.COULD_NOT_CREATE)
        }

        val cartItems = cartRepository.getCartByToken(req.token)?.items

        return UserCreateUseCaseResponse.Success(
            data = UserCreateSuccess(
                token = tokenGenerator.generate(),
                user = user,
                cart = Cart(
                    items = cartItems?: listOf()
                )
            )
        )
    }
}