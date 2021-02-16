package core

interface TokenGenerator {
    fun generate(): String
}

class UniqueTokenGenerator(): TokenGenerator {
    override fun generate(): String {
        return java.util.UUID.randomUUID().toString()
    }

}