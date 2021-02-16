package core.order

class CacheOrderRepository() : OrderRepository {
    private val repository = mutableListOf<Order>()
    override fun getAll(): List<Order> {
        return repository
    }

    override fun getAll(token: String): List<Order> {
        return repository.filter {
            it.userToken == token
        }
    }

    override fun create(order: Order): Boolean {
        repository.add(order)
        return true
    }

}
