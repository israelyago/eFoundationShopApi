package core.cart

sealed class CartUpdateUseCaseResponse {
    data class Success(val cart: Cart): CartUpdateUseCaseResponse()
    data class Error(val error: CartUpdateError): CartUpdateUseCaseResponse()
}
