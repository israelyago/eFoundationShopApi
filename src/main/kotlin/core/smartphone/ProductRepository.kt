package core.smartphone

interface ProductRepository {
    fun getAllSmartphones(): List<Smartphone>
    fun createSmartphone(smartphone: Smartphone): Boolean
}
