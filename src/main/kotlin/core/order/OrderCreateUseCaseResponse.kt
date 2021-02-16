package core.order

sealed class OrderCreateUseCaseResponse {
    data class Success(val ok: Boolean = true): OrderCreateUseCaseResponse()
    data class Error(val error: OrderCreateError): OrderCreateUseCaseResponse()
}
