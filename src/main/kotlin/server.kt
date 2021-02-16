import core.DevShop
import core.ShopSdk
import core.cart.Cart
import core.cart.CartItem
import core.cart.CartUpdateRequest
import core.cart.CartUpdateUseCaseResponse
import core.order.OrderCreateUseCaseRequest
import core.order.OrderCreateUseCaseResponse
import core.smartphone.Smartphone
import core.smartphone.SmartphoneCreateRequest
import core.smartphone.SmartphoneCreateResponse
import core.user.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.util.pipeline.*
import request.OrderCreateRequestBody
import request.UserCreateRequestBody

fun main() {
    val shopSdk: ShopSdk = DevShop()
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
            }
        }
        routing {
            createUserRoute(shopSdk)
            generateTokenRoute(shopSdk)
            userLoginRoute(shopSdk)

            updateCartRoute(shopSdk)

            createSmartphone(shopSdk)
            getAllProducts(shopSdk)

            createOrder(shopSdk)
            getAllOrders(shopSdk)
        }
    }.start(wait = true)
}

suspend inline fun <reified T : Any> requireBody(
    pipelineContext: PipelineContext<Unit, ApplicationCall>,
    function: (body: T) -> Unit
) {
    val body: T
    try {
        body = pipelineContext.call.receive<T>()
    }catch (_: Exception) {
        pipelineContext.call.response.status(HttpStatusCode.BadRequest)
        pipelineContext.call.respond(mapOf(
            "err_code" to "INVALID_BODY"
        ))
        return
    }
    function(body)
}

suspend fun requireToken(
    pipelineContext: PipelineContext<Unit, ApplicationCall>,
    function: suspend (token: String) -> Unit
) {
    val authToken = pipelineContext.call.request.headers["Authorization"]
    if (authToken == null) {
        pipelineContext.call.response.status(HttpStatusCode.Unauthorized)
        pipelineContext.call.respond(mapOf(
            "err_code" to "VALID_TOKEN_REQUIRED"
        ))
    } else {
        function(authToken)
    }
}

private fun Route.getAllOrders(shopSdk: ShopSdk) {
    get("/order") {
        requireToken(this) { token ->
            val user = shopSdk.userRepository.getFromToken(token = token)

            if (user == null) {
                call.response.status(HttpStatusCode.Unauthorized)
                call.respond(mapOf(
                    "err_code" to "VALID_TOKEN_REQUIRED"
                ))
                return@requireToken
            }

            val orders = if (user.isAdmin)
                shopSdk.orderRepository.getAll()
            else
                shopSdk.orderRepository.getAll(token = token)

            call.response.status(HttpStatusCode.BadRequest)
            call.respond(mapOf(
                "orders" to orders
            ))

        }
    }
}

private fun Route.createOrder(shopSdk: ShopSdk) {
    post("/order") {
        requireToken(this) { token ->
            requireBody<OrderCreateRequestBody>(this) { body ->
                val request = OrderCreateUseCaseRequest(
                    token = token,
                    creditCard = body.creditCard,
                    billingDetails = body.billingDetails
                )
                when(val response = shopSdk.orderCreateUseCase.execute(request)) {
                    is OrderCreateUseCaseResponse.Success -> {
                        call.response.status(HttpStatusCode.NoContent)
                    }
                    is OrderCreateUseCaseResponse.Error -> {
                        call.response.status(HttpStatusCode.BadRequest)
                        call.respond(mapOf(
                            "err_code" to response.error
                        ))
                    }
                    else -> {
                        call.response.status(HttpStatusCode.InternalServerError)
                        call.respond(mapOf(
                            "smartphones" to shopSdk.productRepository.getAllSmartphones()
                        ))
                    }
                }

            }
        }
    }
}

private fun Route.getAllProducts(shopSdk: ShopSdk) {
    get("/product") {
        requireToken(this) { _ ->

            // As per the api design, we should validate the token before allowing access to the information

            call.respond(mapOf(
                "smartphones" to shopSdk.productRepository.getAllSmartphones()
            ))

        }
    }
}

private fun Route.createSmartphone(shopSdk: ShopSdk) {
    post("/product/smartphone") {
        requireToken(this) { token ->
            requireBody<Smartphone>(this) { body ->
                val req = SmartphoneCreateRequest(
                    token = token,
                    smartphone = body,
                )

                when (val result = shopSdk.smartphoneCreateUseCase.execute(req)) {
                    is SmartphoneCreateResponse.Success -> {
                        call.response.status(HttpStatusCode.Created)
                        call.respond(mapOf(
                            "smartphone" to result.smartphone
                        ))
                    }
                    /*is SmartphoneCreateResponse.Error -> {
                        call.response.status(HttpStatusCode.BadRequest)
                        call.respond(mapOf(
                            "err_code" to result.error
                        ))
                    }*/
                    else -> {
                        call.respond(mapOf(
                            "err_code" to "INTERNAL_SERVER_ERROR"
                        ))
                    }
                }
            }
        }
    }
}

private fun Route.userLoginRoute(shopSdk: ShopSdk) {
    post("/user/login") {
        requireToken(this) { token ->
            requireBody<UserCredentials>(this) { body ->
                val req = UserLoginRequest(
                    token = token,
                    credentials = body
                )
                when (val result = shopSdk.userLoginUseCase.execute(req)) {
                    is UserLoginResponse.Success -> {
                        call.respond(mapOf(
                            "token" to result.token,
                            "user" to result.user,
                            "cart" to result.cart,
                            "orders" to result.orders
                        ))
                    }
                    is UserLoginResponse.CredentialError -> {
                        call.response.status(HttpStatusCode.BadRequest)
                        call.respond(mapOf(
                            "err_code" to "INVALID_CREDENTIALS"
                        ))
                    }
                    else -> {
                        call.respond(mapOf(
                            "err_code" to "INTERNAL_SERVER_ERROR"
                        ))
                    }
                }
            }
        }
    }

}

private fun Route.updateCartRoute(shopSdk: ShopSdk) {
    put("/user/cart") {
        requireToken(this) { token ->
            requireBody<List<CartItem>>(this) { body ->
                val req = CartUpdateRequest(
                    token = token,
                    cart = Cart(
                        items = body
                    )
                )

                when (val result = shopSdk.cartUpdateUseCase.execute(req)) {
                    is CartUpdateUseCaseResponse.Success -> {
                        call.response.status(HttpStatusCode.NoContent)
                    }
                    is CartUpdateUseCaseResponse.Error -> {
                        call.response.status(HttpStatusCode.BadRequest)
                        call.respond(mapOf(
                            "err_code" to result.error
                        ))
                    }
                    else -> {
                        call.respond(mapOf(
                            "err_code" to "INTERNAL_SERVER_ERROR"
                        ))
                    }
                }
            }
        }
    }
}

private fun Route.generateTokenRoute(shopSdk: ShopSdk) {
    get("/user/token") {
        val response = mapOf(
            "token" to shopSdk.tokenGenerator.generate()
        )
        call.respond(response)
    }
}

private fun Route.createUserRoute(shopSdk: ShopSdk) {
    post("/user") {
        requireToken(this) { token ->
            requireBody<UserCreateRequestBody>(this) { body ->
                val req = UserCreateUseCaseRequest(
                    token = token,
                    user = body.user
                )

                when (val result = shopSdk.userCreateUseCase.execute(req)) {
                    is UserCreateUseCaseResponse.Success -> {
                        call.respond(mapOf(
                            "token" to result.data.token,
                            "user" to result.data.user,
                            "cart" to result.data.cart,
                        ))
                    }
                    is UserCreateUseCaseResponse.Error -> {
                        call.response.status(HttpStatusCode.Unauthorized)
                        call.respond(mapOf(
                            "err_code" to result.error,
                        ))
                    }
                    is UserCreateUseCaseResponse.ValidationError -> {
                        call.response.status(HttpStatusCode.BadRequest)
                        call.respond(mapOf(
                            "err_code" to result.error
                        ))
                    }
                    else -> {
                        call.respond(mapOf(
                            "err_code" to "INTERNAL_SERVER_ERROR"
                        ))
                    }
                }
            }
        }

    }
}
