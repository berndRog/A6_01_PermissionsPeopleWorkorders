package de.rogallab.mobile.ui.composables

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore.Images.Media.getBitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.rogallab.mobile.R
import de.rogallab.mobile.data.io.writeImageToInternalStorage
import de.rogallab.mobile.domain.utilities.logDebug

@Composable
fun SelectPhotoFromGallery(
   onImagePathChanged: (String?) -> Unit,  // Event ↑
) {
   val tag = "ok>SelectPhotoFromGale."
   logDebug(tag,"Start")

   var bitmap:Bitmap?
   val context = LocalContext.current

   // callback for result from photo gallery
   val galleryLauncher = rememberLauncherForActivityResult(
      contract = ActivityResultContracts.GetContent()
   ) { uri: Uri? ->
      // get bitmap from content resolver (photo gallery)
      uri?.let {
         logDebug(tag,"Get Bitmap ${it.path}")
         bitmap = if (Build.VERSION.SDK_INT < 28) {
            getBitmap(context.contentResolver, it)
         } else {
            val source = ImageDecoder.createSource(context.contentResolver, it)
            ImageDecoder.decodeBitmap(source)
         }
         // save bitmap to internal storage of the app
         bitmap?.let { bitmap ->
            writeImageToInternalStorage(context, bitmap)?.let { uriPath:String? ->
               logDebug("ok>SelectPhotoFromGale.", "Storage $uriPath")
               onImagePathChanged(uriPath)  // Event ↑
            }
         }
      }
   }

   Button(
      modifier = Modifier.padding(horizontal = 4.dp).fillMaxWidth(),
      onClick = {
         logDebug("ok>SelectPhotoFromGale.", "Click")
         galleryLauncher.launch("image/*")
      }
   ) {
      Row(
         verticalAlignment = Alignment.CenterVertically
      ) {
         Icon(imageVector = Icons.Outlined.Face,
            contentDescription = stringResource(R.string.back))

         Text(
            modifier = Modifier.padding(start = 8.dp),
            text = "Bitte wählen Sie ein Foto aus",
            style = MaterialTheme.typography.bodyMedium
         )
      }
   }
}