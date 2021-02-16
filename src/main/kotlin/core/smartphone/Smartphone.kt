package core.smartphone

import core.Image

data class Smartphone(
    val id: Int,
    val productId: Long,
    val name: String,
    val priceEur: Float,
    val description: String,
    val images: List<Image>
)
