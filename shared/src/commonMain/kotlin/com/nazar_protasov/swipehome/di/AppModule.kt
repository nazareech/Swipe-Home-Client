package com.nazar_protasov.swipehome.di

import com.nazar_protasov.swipehome.network.PropertyApiService
import com.nazar_protasov.swipehome.ui.screens.home.HomeScreenModel
import com.nazar_protasov.swipehome.ui.screens.hub.SearchHubScreenModel
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

    single { PropertyApiService(get()) }

    // Моделі екранів (Factory, бо вони створюються заново для нових екранів)
    single { HomeScreenModel(get()) }
    factory { SearchHubScreenModel() }
}
