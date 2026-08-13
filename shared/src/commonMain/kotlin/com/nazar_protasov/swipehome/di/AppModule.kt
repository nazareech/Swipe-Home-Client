package com.nazar_protasov.swipehome.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module

// "Коробка" з інструментами
val appModule: Module = module {
    // single означає, що клієнт створиться один раз і буде жити весь час роботи програми
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true // Щоб програма не падала, якщо сервер надішле зайві поля
                    prettyPrint = true
                })
            }
        }
    }
}
