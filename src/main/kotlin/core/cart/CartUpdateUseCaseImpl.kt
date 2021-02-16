package core.cart

class CartUpdateUseCaseImpl(private val cartRepository: CartRepository) : CartUpdateUseCase {
    override fun execute(request: CartUpdateRequest): CartUpdateUseCaseResponse {
        val wasSet = cartRepository.setCart(request.token, request.cart)
        return if (wasSet) {
            CartUpdateUseCaseResponse.Success(cart = request.cart)
        } else {
            CartUpdateUseCaseResponse.Error(CartUpdateError.REPOSITORY_WRITE_ERROR)
        }
    }
}