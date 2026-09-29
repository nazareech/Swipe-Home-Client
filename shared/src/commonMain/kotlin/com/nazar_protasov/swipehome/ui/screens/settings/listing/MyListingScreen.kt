package com.nazar_protasov.swipehome.ui.screens.settings.listing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.nazar_protasov.swipehome.network.PropertyApiService
import com.nazar_protasov.swipehome.ui.screens.settings.analystics.AnalyticsScreen
import com.nazar_protasov.swipehome.ui.screens.settings.listing.components.ListingCard
import com.nazar_protasov.swipehome.ui.screens.settings.listing.components.ListingItem
import com.nazar_protasov.swipehome.ui.screens.settings.listing.components.ListingStatus
import com.nazar_protasov.swipehome.ui.screens.settings.listing.components.ListingsHeader
import com.nazar_protasov.swipehome.ui.screens.settings.listing.components.ListingsTopBar
import com.nazar_protasov.swipehome.ui.screens.settings.listing.createListing.CreateListingScreen
import com.nazar_protasov.swipehome.ui.theme.SwipeHomeTheme
import org.koin.compose.koinInject


class MyListingsScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel: MyListingsScreenModel = koinInject()
        val screenModelListing = koinScreenModel<CreateListingScreenModel>()

        // Підписуємося на нашу базу даних
        val listingsData by screenModel.myListingProperties.collectAsState(initial = emptyList())

        // Мапимо дані з ДБ у формат для карток
        val listings = listingsData.map { data ->

            val photosList = if (data.photosUris.isNotBlank()) {
                data.photosUris.split(",").map { path ->
                    if (path.startsWith("http") || path.startsWith("file://")) path
                    else "${PropertyApiService.BASE_URL}/uploads/$path"
                }
            } else emptyList()
            
            val displayTitle = when {
                data.title.isNotBlank() && data.listingStatus == ListingStatus.DRAFT -> "Чернетка: ${data.title}"
                data.title.isNotBlank() -> data.title
                else -> "Нове оголошення"
            }

            val displayLocation = data.address.ifBlank{ "Адресу не вказано" }

            ListingItem(
                id = data.localId,
                title = displayTitle,
                location = displayLocation,
                imagesUrl = photosList,

                status = data.listingStatus,
                views = if (data.viewsNumber == 0) "--" else data.viewsNumber.toString(),
                likes = if (data.likesNumber == 0) "--" else data.likesNumber.toString(),
                onClick = {
                    // Відкриваємо редактор зі збереженими даними чернетки
                    navigator.push(
                        if(data.listingStatus == ListingStatus.DRAFT) {
                            CreateListingScreen(draftId = data.localId)
                        } else {
                            AnalyticsScreen(cardId = data.localId)
                        }
                    )
                }
            )
        }

        Scaffold(
            topBar = { ListingsTopBar(navigator) },
            containerColor = SwipeHomeTheme.colors.background
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item{
                    ListingsHeader(count = listings.size)
                }

                items(listings){ listing ->
                    ListingCard(
                        listing = listing,
                        onEditClick = {
                            navigator.push(
                                CreateListingScreen(draftId = listing.id)
                            )
                        },
                        onAnalyticClick = {
                            navigator.push(
                                AnalyticsScreen(cardId = listing.id)
                            )
                        },
                        onDeleteClick = {
                            screenModelListing.deleteListing(
                                targetListingId = listing.id,
                                onSuccess = {/*TODO: Зробити спиливаюче сповіщення TOST з інформацією про видалення*/}
                            )
                        }
                    )
                }
            }
        }
    }
}