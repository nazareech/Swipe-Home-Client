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
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
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

        // Підписуємося на нашу базу даних
        val draftEntities by screenModel.drafts.collectAsState(initial = emptyList())

        // Мапимо дані з ДБ у формат для карток
        val listings = draftEntities.map { draft ->
            val photosList = if (draft.photosUris.isNotBlank()) draft.photosUris.split(",") else emptyList()
            val displayTitle = if (draft.title.isNotBlank()) "Чернетка: ${draft.title}" else "Нове оголошення"
            val displayLocation = draft.address.ifBlank{ "Адресу не вказано" }

            ListingItem(
                id = draft.draftId,
                title = displayTitle,
                location = displayLocation,
                imagesUrl = photosList,
                status = ListingStatus.DRAFT,
                views = "--",
                likes = "--",
                onClick = {
                    // Відкриваємо редактор зі збереженими даними чернетки
                    navigator.push(AnalyticsScreen()/*TODO: Передавання ID (draftId = listing.id) для відображення статистики даного оголошення */)
                }
            )

        }
/*
        // Моккові дані
        val listings = listOf(
            ListingItem("1", "Title 1", "Location 1", listOf("https://images.unsplash.com/photo-1512917774080-9991f1c4c750?q=80&w=400"), ListingStatus.ACTIVE, "100", "10", onClick = { navigator.push(AnalyticsScreen())}),
            ListingItem("2", "Title 2", "Location 2", listOf("https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?q=80&w=400"), ListingStatus.MODERATION, "--", "--", onClick = {/*TODO:*/}),
            ListingItem("3", "Title 3", "Location 3", listOf("https://images.unsplash.com/photo-1512917774080-9991f1c4c750?q=80&w=400"), ListingStatus.HIDDEN, "1.2k", "84", onClick = {/*TODO:*/}),
            ListingItem("4", "Title 4", "Location 4", listOf("https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?q=80&w=400"), ListingStatus.ACTIVE, "100", "10", onClick = {/*TODO:*/}),
            ListingItem("5", "Title 5", "Location 5", listOf("https://images.unsplash.com/photo-1512917774080-9991f1c4c750?q=80&w=400"), ListingStatus.ACTIVE, "100", "10", onClick = {/*TODO:*/}),
            ListingItem(
                "draft_123",
                "Title 6",
                "Location 6",
                emptyList(),
                ListingStatus.DRAFT,
                "--", "--",
                onClick = {}
            )
        )
 */

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
                        }
                    )
                }
            }
        }
    }
}