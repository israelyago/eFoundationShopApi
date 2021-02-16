package core.smartphone

import core.Image
import org.junit.jupiter.api.Test

internal class SmartphoneCreateUseCaseImplTest {
    private val token = "totally-valid-token"
    private val productRepository: ProductRepository = CacheProductRepository()
    private val smartphoneCreateUseCase: SmartphoneCreateUseCase = SmartphoneCreateUseCaseImpl(productRepository)
    @Test
    fun `should create when smartphone is valid`() {
        // given
        val validSmartphone = Smartphone(
            id = 284,
            productId = 65471,
            name = "/e/OS Fairphone 3",
            priceEur = 429.9f,
            description = "The deGoogled Fairphone 3 is most likely the first privacy conscious and sustainable phone. It combines a phone that cares for people and planet and an OS and apps that care for your privacy.",
            images = listOf(
                Image(
                    url = "https://esolutions.shop/wp-content/uploads/2020/05/fairphone-3.png"
                )
            )
        )
        val request = SmartphoneCreateRequest(
            token = token,
            smartphone = validSmartphone
        )
        // when
        val response = smartphoneCreateUseCase.execute(request)

        // then
        assert(response is SmartphoneCreateResponse.Success)
        response as SmartphoneCreateResponse.Success

    }

    /*
    Other things like that would make sure everything works without surprises:

    @Test
    fun `should return error if the user is not admin`() {

    }

    @Test
    fun `should not create if the user does not exist`() {}
    */
}