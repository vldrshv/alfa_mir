package ru.alfabank.alfamir.utility

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Parcelable
import android.provider.MediaStore
import ru.alfabank.alfamir.R
import ru.alfabank.alfamir.base_elements.BaseFragment
import java.io.File
import java.util.*


/**
 * Create a chooser intent to select the  source to get image from.<br></br>
 * The source can be camera's  (ACTION_IMAGE_CAPTURE) or gallery's (ACTION_GET_CONTENT).<br></br>
 * All possible sources are added to the intent chooser.<br></br>
 * Use "pick_image_intent_chooser_title" string resource to override chooser title.
 *
 * @param context used to access Android APIs, like content resolve, it is your activity/fragment/widget.
 */
fun BaseFragment.getPickImageChooserIntent(): Intent {
    context?.also { context ->
        return getPickImageChooserIntent(context, context.getString(R.string.pick_image_intent_chooser_title), includeDocuments = false, includeCamera = true)
    }
    return Intent()
}

//fun getPickImageChooserIntent(context: Context): Intent {
//    return getPickImageChooserIntent(context, context.getString(R.string.pick_image_intent_chooser_title), false, true)
//}

/**
 * Create a chooser intent to select the  source to get image from.<br>
 * The source can be camera's  (ACTION_IMAGE_CAPTURE) or gallery's (ACTION_GET_CONTENT).<br>
 * All possible sources are added to the intent chooser.
 *
 * @param context used to access Android APIs, like content resolve, it is your activity/fragment/widget.
 * @param title the title to use for the chooser UI
 * @param includeDocuments if to include KitKat documents activity containing all sources
 * @param includeCamera if to include camera intents
 */
fun getPickImageChooserIntent(context: Context, title: CharSequence, includeDocuments: Boolean, includeCamera: Boolean): Intent {

    val allIntents = ArrayList<Intent>()
    val packageManager = context.packageManager

    // collect all camera intents if Camera permission is available
    if (!isExplicitCameraPermissionRequired(context) && includeCamera) {
        allIntents.addAll(getCameraIntents(context, packageManager))
    }

//    var galleryIntents = getGalleryIntents(packageManager, Intent.ACTION_GET_CONTENT, includeDocuments)
//    if (galleryIntents.isEmpty()) {
//        // if no intents found for get-content try pick intent action (Huawei P9).
//        galleryIntents = getGalleryIntents(packageManager, Intent.ACTION_PICK, includeDocuments)
//    }
//    allIntents.addAll(galleryIntents)

    val target: Intent
    if (allIntents.isEmpty()) {
        target = Intent()
    } else {
        target = allIntents[allIntents.size - 1]
        allIntents.removeAt(allIntents.size - 1)
    }

    // Create a chooser from the main  intent
    val chooserIntent = Intent.createChooser(target, title)

    // Add all other intents
    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toTypedArray<Parcelable>())

    return chooserIntent
}

/**
 * Get all Camera intents for capturing image using device camera apps.
 */
fun getCameraIntents(context: Context, packageManager: PackageManager): List<Intent> {

    val allIntents = ArrayList<Intent>()

    // Determine Uri of camera image to  save.
    val outputFileUri = getCaptureImageOutputUri(context)

    val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    val listCam = packageManager.queryIntentActivities(captureIntent, 0)
    for (res in listCam) {
        val intent = Intent(captureIntent)
        intent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
        intent.setPackage(res.activityInfo.packageName)
        if (outputFileUri != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
        }
        allIntents.add(intent)
    }

    return allIntents
}

/**
 * Get URI to image received from capture  by camera.
 *
 * @param context used to access Android APIs, like content resolve, it is your activity/fragment/widget.
 */
fun getCaptureImageOutputUri(context: Context): Uri? {
    var outputFileUri: Uri? = null
    val getImage = context.externalCacheDir
    if (getImage != null) {
        outputFileUri = Uri.fromFile(File(getImage.path, "pickImageResult.jpeg"))
    }
    return outputFileUri
}

/**
 * Get all Gallery intents for getting image from one of the apps of the device that handle images.
 */
fun getGalleryIntents(packageManager: PackageManager, action: String, includeDocuments: Boolean): List<Intent> {
    val intents = ArrayList<Intent>()
    val galleryIntent = if (action === Intent.ACTION_GET_CONTENT)
        Intent(action)
    else
        Intent(action, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    galleryIntent.type = "image/*"
    galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
    val listGallery = packageManager.queryIntentActivities(galleryIntent, 0)
    for (res in listGallery) {
        val intent = Intent(galleryIntent)
        intent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
        intent.setPackage(res.activityInfo.packageName)
        intents.add(intent)
    }

    // remove documents intent
    if (!includeDocuments) {
        for (intent in intents) {
            if (intent.component!!.className == "com.android.documentsui.DocumentsActivity") {
                intents.remove(intent)
                break
            }
        }
    }
    return intents
}

/**
 * Check if explicetly requesting camera permission is required.<br></br>
 * It is required in Android Marshmellow and above if "CAMERA" permission is requested in the manifest.<br></br>
 * See [StackOverflow
 * question](http://stackoverflow.com/questions/32789027/android-m-camera-intent-permission-bug).
 */
fun isExplicitCameraPermissionRequired(context: Context): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            hasPermissionInManifest(context, "android.permission.CAMERA") &&
            context.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
}

/**
 * Check if the app requests a specific permission in the manifest.
 *
 * @param permissionName the permission to check
 * @return true - the permission in requested in manifest, false - not.
 */
fun hasPermissionInManifest(context: Context, permissionName: String): Boolean {
    val packageName = context.packageName
    try {
        val packageInfo = context.packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
        val declaredPermisisons = packageInfo.requestedPermissions
        if (declaredPermisisons != null && declaredPermisisons.size > 0) {
            for (p in declaredPermisisons) {
                if (p.equals(permissionName, ignoreCase = true)) {
                    return true
                }
            }
        }
    } catch (e: PackageManager.NameNotFoundException) {
    }

    return false
}

/**
 * Get the URI of the selected image from [.getPickImageChooserIntent].<br></br>
 * Will return the correct URI for camera and gallery image.
 *
 * @param context used to access Android APIs, like content resolve, it is your activity/fragment/widget.
 * @param data the returned getData of the  activity result
 */
fun getPickImageResultUri(context: Context, data: Intent?): Uri? {
    var isCamera = true
    if (data != null && data.data != null) {
        val action = data.action
        isCamera = action != null && action == MediaStore.ACTION_IMAGE_CAPTURE
    }
    return if (isCamera || data!!.data == null) getCaptureImageOutputUri(context) else data.data
}