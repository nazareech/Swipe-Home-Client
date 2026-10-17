package com.nazar_protasov.swipehome.ui.components.map

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView
import org.maplibre.android.style.expressions.Expression
import org.maplibre.android.style.layers.FillExtrusionLayer
import org.maplibre.android.style.layers.Property
import org.maplibre.android.style.layers.PropertyFactory
import org.maplibre.android.style.layers.SymbolLayer
import org.maplibre.android.style.sources.GeoJsonSource
import org.maplibre.geojson.Feature
import org.maplibre.geojson.Point
import androidx.core.graphics.toColorInt
import com.nazar_protasov.swipehome.BuildKonfig

private val MAP_STYLE_URL = "https://api.maptiler.com/maps/streets-v2/style.json?key=${BuildKonfig.MAPTILER_API_KEY}"
private const val MARKER_SOURCE_ID = "property-marker-source"
private const val MARKER_LAYER_ID = "property-marker-layer"
private const val MARKER_ICON_ID = "property-marker-icon"
@SuppressLint("LocalContextResourcesRead")
@Composable
actual fun PropertyMapView(
    lat: Double,
    lon: Double,
    isInteractive: Boolean,
    modifier: Modifier,
    onMapLoaded: (MapController) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Ініціалізуємо MapLibre один раз
    remember { MapLibre.getInstance(context) }

    val mapView = remember { MapView(context) }

    // Керування життєвим циклом MapView, щоб не було витоків пам'яті
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(null)
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapView.onDestroy()
        }
    }

    AndroidView(
        factory = { mapView },
        modifier = modifier,
        update = { view ->
            view.getMapAsync { map ->
                // Вимикаємо стандартний UI
                map.uiSettings.isCompassEnabled = false
                map.uiSettings.isLogoEnabled = false
                map.uiSettings.isAttributionEnabled = false

                // Якщо це маленьке прев'ю - блокуємо жести, щоб не заважати скролу екрана
                map.uiSettings.setAllGesturesEnabled(isInteractive)

                map.setStyle(MAP_STYLE_URL) { style ->
                    // Додаємо 3D-екструзію будівель, якщо її ще немає в стилі
                    if(style.getLayer("3d-buildings") == null && style.getSource("openmaptiles") != null) {
                        val fillExtrusionLayer = FillExtrusionLayer("3d-buildings", "openmaptiles").apply {
                            setProperties(
                                PropertyFactory.fillExtrusionColor("#E8D8C8".toColorInt()), // Теплий бежевий колір
                                PropertyFactory.fillExtrusionHeight(Expression.get("render_height")),
                                PropertyFactory.fillExtrusionBase(Expression.get("render_min_height")),
                                PropertyFactory.fillExtrusionOpacity(0.9f)
                            )
                        }
                        style.addLayer(fillExtrusionLayer)
                    }
                    // Додаємо маркер без Deprecated API
                    val point = Point.fromLngLat(lon, lat)
                    val existingSource = style.getSourceAs<GeoJsonSource>(MARKER_SOURCE_ID)

                    if (existingSource != null) {

                        existingSource.setGeoJson(point)
                    } else {
                        val markerBitmap = BitmapFactory.decodeResource(
                            context.resources,
                            org.maplibre.android.R.drawable.maplibre_marker_icon_default
                        )
                        style.addImage(
                            MARKER_ICON_ID,
                            markerBitmap
                        )

                        // Створюємо GeoJSON джерело
                        val markerSource = GeoJsonSource(MARKER_SOURCE_ID, Feature.fromGeometry(point))
                        style.addSource(markerSource)

                        // Додаємо шар для показу іконки
                        val symbolLayer = SymbolLayer(MARKER_LAYER_ID, MARKER_SOURCE_ID).apply {
                            setProperties(
                                PropertyFactory.iconImage(MARKER_ICON_ID),
                                PropertyFactory.iconAllowOverlap(true),
                                PropertyFactory.iconIgnorePlacement(true),
                                PropertyFactory.iconAnchor(Property.ICON_ANCHOR_BOTTOM)
                            )
                        }
                        style.addLayer(symbolLayer)
                    }
                }
                val targetPosition = LatLng(lat, lon)

                // Налаштовуємо камеру під кутом 60 градусів для 3D-виду
                val cameraPosition = CameraPosition.Builder()
                    .target(targetPosition)
                    .zoom(if (isInteractive) 17.0 else 15.5)
                    .tilt(if(isInteractive) 60.0 else 0.0) // Нахил камери вмикає 3D перспективу
                    .build()
                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

                // Передаємо реалізацію контролера наверх у Compose
                val controller = object : MapController {
                    private var is3DMode = true
                    override fun zoomIn() {
                        map.animateCamera(CameraUpdateFactory.zoomIn())
                    }

                    override fun zoomOut() {
                        map.animateCamera(CameraUpdateFactory.zoomOut())
                    }

                    override fun resetNorth() {
                        map.animateCamera(CameraUpdateFactory.bearingTo(0.0))
                    }

                    override fun toggle3D(): Boolean {
                        is3DMode = !is3DMode
                        val newTint = if (is3DMode) 60.0 else 0.0
                        map.animateCamera(CameraUpdateFactory.tiltTo(newTint))
                        return is3DMode
                    }

                    override fun moveToUserLocation() {
                        // Центруємо назад на об'єкт (або можна передавати GPS координати користувача)
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(targetPosition, 17.5))
                    }
                }
                onMapLoaded(controller)
            }
        }
    )
}