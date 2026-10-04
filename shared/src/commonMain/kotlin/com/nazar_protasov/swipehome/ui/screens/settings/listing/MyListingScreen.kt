package com.nazar_protasov.swipehome.ui.screens.settings.listing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
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
import mymultiplatformproject.shared.generated.resources.Res
import mymultiplatformproject.shared.generated.resources.ic_refresh
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject


class MyListingsScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel: MyListingsScreenModel = koinInject()

        val snackbarHostState = remember { SnackbarHostState() }

        // Підписуємося на MyListingsScreenModel
        LaunchedEffect(Unit) {
            screenModel.snackbarEvent.collect { message ->
                snackbarHostState.showSnackbar(message)
            }
        }

        // Підписуємося на нашу базу даних
        val listingsData by screenModel.myListingProperties.collectAsState(initial = emptyList())
        // Спостерігаємо за станами (дані для сервера)
        val isLoading by screenModel.isLoading.collectAsState()

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
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = { ListingsTopBar(navigator) },
            containerColor = SwipeHomeTheme.colors.background
        ) { paddingValues ->
            // ПОКАЗУЄМО ІНДИКАТОР ЗАВАНТАЖЕННЯ
           if(listings.isEmpty()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "Наразі у вас ще немає оголошень",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Створіть нове оголошення, або оновіть сторінку",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                screenModel.fetchOwnProperties()
                            }
                        ){
                            Icon(painterResource(Res.drawable.ic_refresh), contentDescription = "Оновити")
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        ListingsHeader(count = listings.size, isLoading)
                    }

                    items(listings) { listing ->
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
                                screenModel.deleteListing(
                                    targetListingId = listing.id
                                )
                            }
                        )
                    }
                }
            }
        }

        if (screenModel.isDeleting){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SwipeHomeTheme.colors.background.copy(alpha = 0.4f))
                    .pointerInput(Unit) {},
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator(
                    color = SwipeHomeTheme.colors.primary
                )
            }
        }
    }
}