package core.user

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class UserLoginUseCaseImplTest {
    private val userRepository: UserRepository = CacheUserRepository()
    private val userLoginUseCase: UserLoginUseCase = UserLoginUseCaseImpl(userRepository = userRepository)

    private val token = "totally-valid-token"
    private val fakeUser = User(
        id = 84716,
        userName = "johndoe",
        firstName = "John",
        lastName = "Doe",
        email = "johndoe@fake.email.com",
        password = "fakepass",
        phone = "1122344445555"
    )

    @BeforeEach
    fun createFakeUser() {
        userRepository.create(token = token, user = fakeUser)
    }

    @Test
    fun `should login when user does exist`() {
        // given
        val userCredentials = UserCredentials(
            username = "johndoe",
            password = "fakepass"
        )
        val request = UserLoginRequest(
            token = token,
            credentials = userCredentials
        )
        // when
        val result = userLoginUseCase.execute(request)

        // then
        assert(result is UserLoginResponse.Success)

        result as UserLoginResponse.Success

        assertNotEquals(token, result.token)
        assertEquals(fakeUser, result.user)
    }

    @Test
    fun `should give error when invalid credentials`() {
        // given
        val userCredentials = UserCredentials(
            username = "johndoe",
            password = "wrongpassword"
        )
        val request = UserLoginRequest(
            token = token,
            credentials = userCredentials
        )
        // when
        val result = userLoginUseCase.execute(request)

        // then
        assert(result is UserLoginResponse.CredentialError)
    }
}