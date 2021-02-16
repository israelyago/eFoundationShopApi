package core.user

data class UserCreateUseCaseRequest(
    val token: String,
    val user: User
)