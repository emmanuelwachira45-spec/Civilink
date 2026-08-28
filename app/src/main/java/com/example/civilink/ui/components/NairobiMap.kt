package com.example.civilink.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun NairobiMap(
    modifier: Modifier = Modifier,
    selectedLocation: LatLng? = null,
    onLocationSelected: (LatLng) -> Unit = {}
) {
    val nairobi = LatLng(-1.2921, 36.8219)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(nairobi, 11f)
    }

    val markerState = remember(selectedLocation) {
        MarkerState(position = selectedLocation ?: nairobi)
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapClick = { latLng ->
            onLocationSelected(latLng)
        }
    ) {
        Marker(
            state = markerState,
            title = if (selectedLocation != null) "Selected Location" else "Nairobi County",
            snippet = if (selectedLocation != null) "Lat: ${selectedLocation.latitude}, Lng: ${selectedLocation.longitude}" else "Heart of Kenya"
        )
    }
}
