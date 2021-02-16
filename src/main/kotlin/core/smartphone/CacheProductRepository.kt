package core.smartphone

import kotlin.random.Random

class CacheProductRepository : ProductRepository {
    private val smartphoneRepository = mutableListOf<Smartphone>()
    override fun getAllSmartphones(): List<Smartphone> {
        return smartphoneRepository
    }

    override fun createSmartphone(smartphone: Smartphone): Boolean {
        val newSmartphone = smartphone.copy(
            id = Random.nextInt(),
            productId = Random.nextLong(),
        )
        smartphoneRepository.add(newSmartphone)
        return true
    }
}