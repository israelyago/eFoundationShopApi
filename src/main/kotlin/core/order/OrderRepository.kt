package core.order

interface OrderRepository {
    fun getAll(): List<Order>
    fun getAll(token: String): List<Order>
    fun create(order: Order): Boolean
}
