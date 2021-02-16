package core.user

interface UserLoginUseCase {
    fun execute(request: UserLoginRequest): UserLoginResponse
}
