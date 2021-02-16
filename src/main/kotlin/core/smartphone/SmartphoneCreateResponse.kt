package core.smartphone

sealed class SmartphoneCreateResponse {
    data class Success(val smartphone: Smartphone): SmartphoneCreateResponse()
}
