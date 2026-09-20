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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.nazar_protasov.swipehome.ui.screens.settings.listing.CreateListingScreenModel
import com.nazar_protasov.swipehome.ui.screens.settings.listing.createListing.components.Step1BasicInfo
import com.nazar_protasov.swipehome.ui.screens.settings.listing.createListing.components.Step2Details
import com.nazar_protasov.swipehome.ui.screens.settings.listing.createListing.components.Step3Media
import com.nazar_protasov.swipehome.ui.screens.settings.listing.createListing.components.Step4Verification
import com.nazar_protasov.swipehome.ui.screens.settings.listing.createListing.components.WizardBottomBar
import com.nazar_protasov.swipehome.ui.screens.settings.listing.createListing.components.WizardTopBar
import com.nazar_protasov.swipehome.ui.theme.SwipeHomeTheme
import org.koin.compose.koinInject
import kotlin.random.Random

class CreateListingScreen(val draftId: String? = null) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val scrollState = rememberScrollState()

        val screenModel: CreateListingScreenModel = koinInject()

        // Якщо передано draftId, завантажуємо дані один раз при старті
        LaunchedEffect(draftId){
            draftId?.let { screenModel.loadDraft(it) }
        }
/*
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
*/

        Scaffold(
            topBar = {
                WizardTopBar(
                    currentStep = screenModel.currentStep,
                    totalSteps = screenModel.totalSteps,
                    onSaveClick = { screenModel.saveDraft (
                        onSuccess = { navigator.pop()}
                        )
                    },
                    onBackClick = { if(screenModel.currentStep > 1) screenModel.currentStep-- else navigator.pop() }
                )
            },
            bottomBar = {
                WizardBottomBar(
                    currentStep = screenModel.currentStep,
                    totalSteps = screenModel.totalSteps,
                    onSaveClick = { screenModel.saveDraft (
                        onSuccess = { navigator.pop()}
                        )
                    },
                    onNext = { if(screenModel.currentStep < screenModel.totalSteps) screenModel.currentStep++ else screenModel.submitListing { navigator.pop() } },
                    onPrev = { if(screenModel.currentStep > 1) screenModel.currentStep-- else navigator.pop() }
                )
            },
            containerColor = SwipeHomeTheme.colors.background
        ){ paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ){
                Crossfade(targetState = screenModel.currentStep, label = "StepTransition") { step ->
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(16.dp)
                    ){
                        when(step){
                            1 -> Step1BasicInfo(
                                selectedType = screenModel.selectedType,
                                onTypeSelected = { screenModel.selectedType = it },
                                address = screenModel.address,
                                onAddressChange = { screenModel.address = it },
                                area = screenModel.area,
                                onAreaChange = { screenModel.area = it },
                                price = screenModel.price,
                                onPriceChange = { screenModel.price = it }
                            )
                            2 -> Step2Details(
                                title = screenModel.title,
                                onTitleChange = { screenModel.title = it },
                                description = screenModel.description,
                                onDescriptionChange = { screenModel.description = it },

                                countBedrooms = screenModel.countBedrooms, onCountBedrooms = { screenModel.countBedrooms = it },
                                countBathrooms = screenModel.countBathrooms, onCountBathrooms = { screenModel.countBathrooms = it },
                                floorNumber = screenModel.floorNumber, onFloorNumber = { screenModel.floorNumber = it },

                                allowedPets = screenModel.allowedPets, onAllowedPets = { screenModel.allowedPets = it },
                                isParking = screenModel.isParking, onIsParking = { screenModel.isParking = it },
                                isFurnished = screenModel.isFurnished, onIsFurnished = { screenModel.isFurnished = it },
                                isBalcony = screenModel.isBalcony, onIsBalcony = { screenModel.isBalcony = it },
                                isCondition = screenModel.isCondition, onIsCondition = { screenModel.isCondition = it },

                                gymIsSelect = screenModel.gymIsSelect, onGymIsSelect = { screenModel.gymIsSelect = it},
                                poolIsSelect = screenModel.poolIsSelect, onPoolIsSelect = { screenModel.poolIsSelect = it},
                                elevatorIsSelect = screenModel.elevatorIsSelect, onElevatorIsSelect = { screenModel.elevatorIsSelect = it},
                                securityIsSelect = screenModel.securityIsSelect, onSecurityIsSelect = { screenModel.securityIsSelect = it},
                            )
                            3 -> Step3Media(
                                    photos = screenModel.uploadedPhotos,
                                    onAddPhoto = {
                                        screenModel.uploadedPhotos.add("https://picsum.photos/200/300?id=${Random.nextInt(100000)}")
                                    },
                                    onRemovePhoto = { index -> screenModel.uploadedPhotos.removeAt(index) },
                                    onMovePhoto = { fromIndex, toIndex ->
                                        // Логіка перетягування (Drag & Drop)
                                        val item = screenModel.uploadedPhotos.removeAt(fromIndex)
                                        screenModel.uploadedPhotos.add(toIndex, item)
                                    }
                                )
                            4 -> Step4Verification(
                                verificationMethod = screenModel.verificationMethod,
                                onVerificationMethod = { screenModel.verificationMethod = it }
                            )
                        }
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}