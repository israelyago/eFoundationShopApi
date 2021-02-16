package core.user

interface UserCreateUseCase {
    fun execute(req: UserCreateUseCaseRequest): UserCreateUseCaseResponse
}