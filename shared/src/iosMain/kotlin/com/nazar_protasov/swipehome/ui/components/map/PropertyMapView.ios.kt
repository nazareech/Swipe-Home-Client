package com.nazar_protasov.swipehome.ui.components.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.MapKit.MKMapCamera
import platform.MapKit.MKMapView
import platform.MapKit.MKPointAnnotation

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun PropertyMapView(
    lat: Double,
    lon: Double,
    isInteractive: Boolean,
    modifier: Modifier,
    onMapLoaded: (MapController) -> Unit
) {
    val mapView = remember { MKMapView() }

    UIKitView(
        factory = {
            mapView.apply {
                showsBuildings = true
                pitchEnabled = true
                scrollEnabled = isInteractive
                zoomEnabled = isInteractive
                rotateEnabled = isInteractive
            }
        },
        modifier = modifier,
        update = { view ->
            val coordinate = CLLocationCoordinate2DMake(lat, lon)

            val annotation = MKPointAnnotation().apply {
                setCoordinate(coordinate)
            }
            view.removeAnnotations(view.annotations)
            view.addAnnotation(annotation)

            val camera = MKMapCamera.cameraLookingAtCenterCoordinate(
                centerCoordinate = coordinate,
                fromDistance = if (isInteractive) 500.0 else 1000.0,
                pitch = if(isInteractive) 60.0 else 0.0,
                heading =  0.0
            )
            view.setCamera(camera, animated = true)

            val controller = object : MapController {
                private var is3DMode = true

                override fun zoomIn() {
                    val cam = view.camera
                    cam.altitude *= 0.5
                    view.setCamera(cam, animated = true)
                }

                override fun zoomOut() {
                    val cam = view.camera
                    cam.altitude *= 2.0
                    view.setCamera(cam, animated = true)
                }

                override fun resetNorth() {
                    val cam = view.camera
                    cam.heading *= 0.0
                    view.setCamera(cam, animated = true)
                }

                override fun toggle3D(): Boolean {
                    is3DMode = !is3DMode
                    val cam = view.camera
                    cam.pitch = if (is3DMode) 60.0 else 0.0
                    view.setCamera(cam, animated = true)
                    return is3DMode
                }

                override fun moveToUserLocation() {
                    view.setCenterCoordinate(coordinate, animated = true)
                }
            }
            onMapLoaded(controller)
        }
    )
}