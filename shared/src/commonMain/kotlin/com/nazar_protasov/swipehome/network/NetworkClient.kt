package com.nazar_protasov.swipehome.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

val httpClient = HttpClient{
    install(ContentNegotiation){
        json(Json {
            prettyPrint = true
            isLenient = true
            // Якщо бекенд додасть нове поле, а в нашому DTO його ще немає, прога не впаде
            ignoreUnknownKeys = true
        })
    }
}