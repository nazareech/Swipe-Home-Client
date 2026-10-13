package com.nazar_protasov.swipehome.network.API

import com.nazar_protasov.swipehome.network.dto.NominatimResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders

class GeocodingApiService(private val client: HttpClient) {

    suspend fun getCoordinates(address: String): List<NominatimResponse> {
        return try {
            val response = client.get("https://nominatim.openstreetmap.org/search") {
                header(HttpHeaders.UserAgent, "SwipeHomeApp/1.0 (swipehome27@gmail.com)")

                parameter("q", address)
                parameter("format", "json")
                parameter("addressdetails", 1)
                parameter("limit", 5) // Максимальна кількість результатів для автоматичного доповнення
                parameter("countrycodes", "pl,ua") // Покашо тільки Польща і Україна
            }
            response.body()
        } catch (e: Exception) {
            println("Помилка отримання координат: ${e.message}")
            emptyList()
        }
    }
}