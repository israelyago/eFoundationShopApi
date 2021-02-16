package core.order

interface OrderCreateUseCase {
    fun execute(request: OrderCreateUseCaseRequest): OrderCreateUseCaseResponse
}
