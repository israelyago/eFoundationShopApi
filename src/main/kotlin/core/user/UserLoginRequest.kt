package core.user

data class UserLoginRequest(
    val token: String,
    val credentials: UserCredentials,
)