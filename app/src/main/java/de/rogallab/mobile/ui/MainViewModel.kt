package de.rogallab.mobile.ui

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.lifecycle.HiltViewModel
import de.rogallab.mobile.data.seed.Seed
import de.rogallab.mobile.domain.utilities.logDebug
import de.rogallab.mobile.domain.utilities.logInfo
import javax.inject.Inject

data class LngLat(
   val longitude: Double = 0.0,
   val latitude: Double = 0.0
)

@HiltViewModel
class MainViewModel @Inject constructor(
   application: Application,
   private val _seed: Seed
) : ViewModel() {

   val permissionQueue: SnapshotStateList<String> = mutableStateListOf()

   private val applicationContext = application.applicationContext
   private val resources = application.resources


   private val fusedLocationClient: FusedLocationProviderClient =
      LocationServices.getFusedLocationProviderClient(application)

   private val _currentLocation = mutableStateOf<LngLat>(value = LngLat())
   val currentLocation: State<LngLat>
      get() = _currentLocation
   fun requestLocation() {
      if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
         ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
         fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
               val latLng = LngLat(location.longitude, location.latitude)
               _currentLocation.value = latLng
            }
         }
      }
   }


   init{
      _seed.initDatabase()
   }


   override fun onCleared() {
      super.onCleared()
      logInfo(tag, "onCleared()")
      _seed.disposeImages()
   }

   fun addPermission(
      permission: String,
      isGranted: Boolean
   ) {
      logDebug(tag, "addPermission $permission $isGranted")
      if (isGranted || permissionQueue.contains(permission)) return
      permissionQueue.add(permission)
   }

   fun removePermission() {
      logDebug(tag, "removePermission ${permissionQueue.size}")
      permissionQueue.removeFirst()
   }

   companion object {
      private const val tag: String = "ok>MainViewModel      ."
   }
}