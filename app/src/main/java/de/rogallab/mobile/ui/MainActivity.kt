package de.rogallab.mobile.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_DENIED
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.content.pm.PermissionGroupInfo
import android.content.pm.PermissionInfo
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import de.rogallab.mobile.domain.utilities.logVerbose
import de.rogallab.mobile.ui.base.BaseActivity
import de.rogallab.mobile.ui.permissions.RequestPermissions
import de.rogallab.mobile.ui.theme.AppTheme

@AndroidEntryPoint
class MainActivity : BaseActivity(tag) {

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)

      // use insets to show to snackbar above ime keyboard
      WindowCompat.setDecorFitsSystemWindows(window, false)

      setContent {

         AppTheme {
            Surface(modifier = Modifier
               .fillMaxSize()
               .safeDrawingPadding())
            {
               //listPermissionGroups()
               RequestPermissions()
               //AppNavHost()

            }
         }
      }
   }


   fun listPermissionGroups() {
      val context: Context = this
      val packageManager: PackageManager = context.packageManager

      val listPermissionGroups: MutableList<PermissionGroupInfo> =
         packageManager.getAllPermissionGroups(0)
      for (permissionGroupInfo: PermissionGroupInfo in listPermissionGroups) {
         val permissionGroupLabel = permissionGroupInfo.loadLabel(packageManager).toString()
         logVerbose("ok>PermissionGroups", "Group: ${permissionGroupInfo.name}: $permissionGroupLabel")
         try {
            val listPermissionInfos: MutableList<PermissionInfo> =
               packageManager.queryPermissionsByGroup(permissionGroupInfo.name, 0)
            for (permissionInfo in listPermissionInfos) {
               val des = permissionInfo.loadDescription(packageManager).toString()
               val permissionLabel = permissionInfo.loadLabel(packageManager).toString()
               if(context.checkSelfPermission(permissionInfo.name) == PERMISSION_GRANTED) {
                  logVerbose("ok>PermissionGroups", "   GRANTED: ${permissionInfo.name}: $permissionLabel $des")
               } else if(context.checkSelfPermission(permissionInfo.name) == PERMISSION_DENIED) {
                  logVerbose("ok>PermissionGroups", "   DENIED : ${permissionInfo.name}: $permissionLabel $des")
               } else {
                  logVerbose("ok>PermissionGroups", "          : ${permissionInfo.name}: $permissionLabel $des")
               }
            }
         }
         catch (ex: Exception) {
            ex.printStackTrace()
         }
      }
   }

   companion object {
      //                       12345678901234567890123
      private const val tag = "ok>MainActivity       ."
   }
}

// static extension function for Activity
fun Activity.openAppSettings() {
   Intent(
      Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
      Uri.fromParts("package", packageName, null)
   ).also(::startActivity)
}




