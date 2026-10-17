package com.nazar_protasov.swipehome.ui.components.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// Контролер для керування картою з кнопок Compose
interface MapController {
    fun zoomIn()
    fun zoomOut()
    fun resetNorth()
    fun toggle3D(): Boolean // Якщо увімкнена 3D повертає true
    fun moveToUserLocation()
}

@Composable
expect fun PropertyMapView(
    lat: Double,
    lon: Double,
    isInteractive: Boolean,
    modifier: Modifier = Modifier,
    onMapLoaded: (MapController) -> Unit = {},
    )
