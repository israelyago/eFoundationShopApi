package core.user

import core.TokenGenerator
import core.UniqueTokenGenerator
import core.cart.CacheCartRepository
import core.cart.CartRepository
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

internal class UserCreateUseCaseImplTest() {

    // TODO: User Validation is not complete, only the email was partially validated (to demonstrate a validation process)

    private val tokenGenerator: TokenGenerator = UniqueTokenGenerator()
    private val cartRepository: CartRepository = CacheCartRepository()
    private val userRepository: UserRepository = CacheUserRepository()
    private val createUserUseCase = UserCreateUseCaseImpl(
        tokenGenerator = tokenGenerator,
        cartRepository = cartRepository,
        userRepository = userRepository,
    )

    @Test
    fun `should create user successfully`() {

        val user = User(
            id = 84716,
            userName = "johndoe",
            firstName = "John",
            lastName = "Doe",
            email = "johndoe@fake.email.com",
            password = "fakepass",
            phone = "1122344445555"
        )
        val token = "abc"

        val req = UserCreateUseCaseRequest(token, user)
        val resp = createUserUseCase.execute(req)

        assert(resp is UserCreateUseCaseResponse.Success)
        resp as UserCreateUseCaseResponse.Success

        // The user id should be ignored and a new one should be created
        assertNotEquals(user.id, resp.data.user.id)

        // Checking for equality of other fields
        val userWithNewId = user.copy(resp.data.user.id)
        assertEquals(userWithNewId, resp.data.user)

        // A new token need's to be generated
        assertNotEquals(token, resp.data.token)

    }

    @Test
    fun `should create user with different id and token each time`() {

        val user = User(
            id = 84716,
            userName = "johndoe",
            firstName = "John",
            lastName = "Doe",
            email = "johndoe@fake.email.com",
            password = "fakepass",
            phone = "1122344445555"
        )
        val token = "abc"


        val tokens = mutableSetOf<String>()

        val ids = mutableSetOf<Long>()

        (1..5).forEach { i ->
            val req = UserCreateUseCaseRequest(token + i.toString(), user)
            val resp = createUserUseCase.execute(req)
            assert(resp is UserCreateUseCaseResponse.Success)
            resp as UserCreateUseCaseResponse.Success

            // A set cannot have duplicated elements
            // So we check if the id/token was added to the set
            val tokenWasAdded = tokens.add(resp.data.token)
            assert(tokenWasAdded)

            val idWasAdded = ids.add(resp.data.user.id)
            assert(idWasAdded)
        }
    }

    @Test
    fun `should not create user when email is invalid`() {
        val user = User(
            id = 84716,
            userName = "johndoe",
            firstName = "John",
            lastName = "Doe",
            email = "invalidemailaddress",
            password = "fakepass",
            phone = "1122344445555"
        )
        val token = "abc"

        val req = UserCreateUseCaseRequest(token, user)
        val resp = createUserUseCase.execute(req)

        assert(resp !is UserCreateUseCaseResponse.Success)
        resp as UserCreateUseCaseResponse.ValidationError

        assertEquals(UserValidationError.INVALID_EMAIL, resp.error)
    }

    @Test
    fun `should not create two user with the same token`() {
        val user = User(
            id = 84716,
            userName = "johndoe",
            firstName = "John",
            lastName = "Doe",
            email = "johndoe@fake.email.com",
            password = "fakepass",
            phone = "1122344445555"
        )
        val token = "abc"

        val req = UserCreateUseCaseRequest(token, user)
        val firstUserResponse = createUserUseCase.execute(req)

        assert(firstUserResponse is UserCreateUseCaseResponse.Success)
        val secondUserResponse = createUserUseCase.execute(req)
        assert(secondUserResponse is UserCreateUseCaseResponse.Error)
        secondUserResponse as UserCreateUseCaseResponse.Error
        assertEquals(UserCreateError.TOKEN_WAS_ALREADY_USED, secondUserResponse.error)
    }

}