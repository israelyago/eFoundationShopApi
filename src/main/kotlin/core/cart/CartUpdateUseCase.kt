package core.cart

interface CartUpdateUseCase {
    fun execute(request: CartUpdateRequest): CartUpdateUseCaseResponse
}