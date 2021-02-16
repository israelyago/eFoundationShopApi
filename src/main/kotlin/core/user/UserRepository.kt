package core.user

interface UserRepository {
    fun getFromToken(token: String): User?
    fun getNextId(): Long
    fun getByCredentials(credentials: UserCredentials): User?
    fun create(token: String, user: User): Boolean
}