package core.smartphone

class SmartphoneCreateUseCaseImpl(
    private val productRepository: ProductRepository
) : SmartphoneCreateUseCase {
    override fun execute(request: SmartphoneCreateRequest): SmartphoneCreateResponse {

        productRepository.createSmartphone(request.smartphone)

        return SmartphoneCreateResponse.Success(
            smartphone = request.smartphone
        )
    }
}