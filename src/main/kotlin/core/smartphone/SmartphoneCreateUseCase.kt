package core.smartphone

interface SmartphoneCreateUseCase {
    fun execute(request: SmartphoneCreateRequest): SmartphoneCreateResponse
}
