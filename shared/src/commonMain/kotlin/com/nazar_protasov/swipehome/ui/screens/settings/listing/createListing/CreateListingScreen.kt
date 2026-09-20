package com.nazar_protasov.swipehome.ui.screens.settings.listing.createListing

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.nazar_protasov.swipehome.ui.screens.settings.listing.createListing.components.PropertyType
import com.nazar_protasov.swipehome.ui.screens.settings.listing.createListing.components.Step1BasicInfo
import com.nazar_protasov.swipehome.ui.screens.settings.listing.createListing.components.Step2Details
import com.nazar_protasov.swipehome.ui.screens.settings.listing.createListing.components.Step3Media
import com.nazar_protasov.swipehome.ui.screens.settings.listing.createListing.components.Step4Verification
import com.nazar_protasov.swipehome.ui.screens.settings.listing.createListing.components.VerificationMethods
import com.nazar_protasov.swipehome.ui.screens.settings.listing.createListing.components.WizardBottomBar
import com.nazar_protasov.swipehome.ui.screens.settings.listing.createListing.components.WizardTopBar
import com.nazar_protasov.swipehome.ui.theme.SwipeHomeTheme

class CreateListingScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val scrollState = rememberScrollState()

        var currentStep by remember { mutableStateOf(1) }
        val totalSteps = 4

        //TODO: Потом перекинуть в CreateListingScreenModel
        var selectedType by remember { mutableStateOf(PropertyType.APARTMENT) }
        var address by remember { mutableStateOf("") }
        var area by remember { mutableStateOf("") }
        var price by remember { mutableStateOf("") }
        var verificationMethod by remember { mutableStateOf(VerificationMethods.DIIA) }

        var countBedrooms by remember { mutableStateOf(1) }
        var countBathrooms by remember { mutableStateOf(1) }
        var floorNumber by remember { mutableStateOf(1) }

        var allowedPets by remember { mutableStateOf(false) }
        var isParking by remember { mutableStateOf(false) }
        var isFurnished by remember { mutableStateOf(false) }
        var isBalcony by remember { mutableStateOf(false) }
        var isCondition by remember { mutableStateOf(false) }

        var description by remember { mutableStateOf("") }
        var gymIsSelect by remember { mutableStateOf(false) }
        var poolIsSelect by remember { mutableStateOf(false) }
        var elevatorIsSelect by remember { mutableStateOf(false) }
        var securityIsSelect by remember { mutableStateOf(false) }

        val uploadedPhotos = remember { mutableStateListOf<String>() }

        Scaffold(
            topBar = {
                WizardTopBar(
                    currentStep = currentStep,
                    totalSteps = totalSteps,
                    onBackClick = { if(currentStep > 1) currentStep-- else navigator.pop() }
                )
            },
            bottomBar = {
                WizardBottomBar(
                    currentStep = currentStep,
                    totalSteps = totalSteps,
                    onNext = { if(currentStep < totalSteps) currentStep++ else { /*TODO: Submit*/} },
                    onPrev = { if(currentStep > 1) currentStep-- else navigator.pop() }
                )
            },
            containerColor = SwipeHomeTheme.colors.background
        ){ paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ){
                Crossfade(targetState = currentStep, label = "StepTransition") { step ->
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(16.dp)
                    ){
                        when(step){
                            1 -> Step1BasicInfo(
                                selectedType = selectedType,
                                onTypeSelected = { selectedType = it },
                                address = address,
                                onAddressChange = { address = it },
                                area = area,
                                onAreaChange = { area = it },
                                price = price,
                                onPriceChange = { price = it }
                            )
                            2 -> Step2Details(
                                description = description,
                                onDescriptionChange = { description = it },

                                countBedrooms = countBedrooms, onCountBedrooms = { countBedrooms = it },
                                countBathrooms = countBathrooms, onCountBathrooms = { countBathrooms = it },
                                floorNumber = floorNumber, onFloorNumber = { floorNumber = it },

                                allowedPets = allowedPets, onAllowedPets = { allowedPets = it },
                                isParking = isParking, onIsParking = { isParking = it },
                                isFurnished = isFurnished, onIsFurnished = { isFurnished = it },
                                isBalcony = isBalcony, onIsBalcony = { isBalcony = it },
                                isCondition = isCondition, onIsCondition = { isCondition = it },

                                gymIsSelect = gymIsSelect, onGymIsSelect = { gymIsSelect = it},
                                poolIsSelect = poolIsSelect, onPoolIsSelect = { poolIsSelect = it},
                                elevatorIsSelect = elevatorIsSelect, onElevatorIsSelect = { elevatorIsSelect = it},
                                securityIsSelect = securityIsSelect, onSecurityIsSelect = { securityIsSelect = it},
                            )
                            3 -> Step3Media(
                                    photos = uploadedPhotos,
                                    onAddPhoto = {
                                        uploadedPhotos.add("https://picsum.photos/200/300?id=${uploadedPhotos.size + 1}")
                                    },
                                    onRemovePhoto = { index -> uploadedPhotos.removeAt(index) },
                                    onMovePhoto = { fromIndex, toIndex ->
                                        // Логіка перетягування (Drag & Drop)
                                        val item = uploadedPhotos.removeAt(fromIndex)
                                        uploadedPhotos.add(toIndex, item)
                                    }
                                )
                            4 -> Step4Verification(
                                verificationMethod = verificationMethod,
                                onVerificationMethod = { verificationMethod = it }
                            )
                        }
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}