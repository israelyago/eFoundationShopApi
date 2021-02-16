package core.user

sealed class UserCreateUseCaseResponse{
    data class Error(val error: UserCreateError): UserCreateUseCaseResponse()
    data class ValidationError(val error: UserValidationError): UserCreateUseCaseResponse()
    data class Success(val data: UserCreateSuccess): UserCreateUseCaseResponse()
}