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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.nazar_protasov.swipehome.ui.screens.settings.listing.createListing.components.Step1BasicInfo
import com.nazar_protasov.swipehome.ui.screens.settings.listing.createListing.components.Step2Details
import com.nazar_protasov.swipehome.ui.screens.settings.listing.createListing.components.Step3Media
import com.nazar_protasov.swipehome.ui.screens.settings.listing.createListing.components.Step4Verification
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
        var selectedType by remember { mutableStateOf("") }
        var address by remember { mutableStateOf("") }
        var area by remember { mutableStateOf("") }
        var price by remember { mutableStateOf("") }
        var verificationMethod by remember { mutableStateOf("Дія") }

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
                            2 -> Step2Details()
                            3 -> Step3Media()
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