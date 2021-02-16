package core.user

import kotlin.random.Random

class CacheUserRepository(): UserRepository {
    private val repository = mutableMapOf<String, User>()
    override fun getFromToken(token: String): User? {
        return repository[token]
    }

    override fun getNextId(): Long {
        // TODO: Not quite sure if it can repeat, making our unit tests potentially randomly failing which would be strange to debug
        return Random.nextLong()
    }

    override fun getByCredentials(credentials: UserCredentials): User? {
        repository.forEach { (_, user) ->
            if (user.password == credentials.password && user.userName == credentials.username) {
                return user
            }
        }
        return null
    }

    override fun create(token: String, user: User): Boolean {
        repository[token] = user
        return true
    }

}