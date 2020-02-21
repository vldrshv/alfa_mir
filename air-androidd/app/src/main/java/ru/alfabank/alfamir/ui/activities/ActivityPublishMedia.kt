package ru.alfabank.alfamir.ui.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.MotionEvent
import android.view.View
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.publish_media_activity.*
import kotlinx.coroutines.*
import ru.alfabank.alfamir.App
import ru.alfabank.alfamir.Constants.Companion.ACTION_POST_SENDING
import ru.alfabank.alfamir.Constants.Companion.POST_SENT
import ru.alfabank.alfamir.R
import ru.alfabank.alfamir.base_elements.BaseActivity
import ru.alfabank.alfamir.utility.logging.firebase.FirebaseWrapper
import ru.alfabank.alfamir.utility.logging.remote.LoggerContract
import ru.alfabank.alfamir.utility.logging.remote.LoggerContract.Client.NewMedia
import ru.alfabank.alfamir.utility.static_utilities.RequestFactory
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class ActivityPublishMedia : BaseActivity(), NewMedia {

    @JvmField
    @Inject
    var logger: LoggerContract.Provider? = null

    @JvmField
    @Inject
    var mFirebaseWrapper: FirebaseWrapper? = null

    private val scope = CoroutineScope(Dispatchers.Main)
    private var deferredEncodedImage: Deferred<String>? = null
    private var app: App? = null
    private val destination = "instagram"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.publish_media_activity)

        scroll.setOnTouchListener { _: View?, _: MotionEvent? -> true }

        app = application as App
        logOpen()
        setClickListeners()
        dispatchTakePictureIntent()
    }

    override fun onResume() {
        super.onResume()
        if (!checkIfInitialized()) return
    }

    private fun setClickListeners() {

        send_post.setOnClickListener {

            onBackPressed()

            val intent = Intent()
            intent.action = ACTION_POST_SENDING
            intent.putExtra("result", POST_SENT)
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)

            val deferredUri = scope.async {
                deferredEncodedImage?.await()?.let { encodedImage ->
                    app!!.dProvider.uploadPhoto(destination, encodedImage)
                }
            }

            scope.launch {
                deferredUri.await()?.also { uri ->
                    mFirebaseWrapper!!.logEvent("click_post_media_post_media", Bundle())
                    logSend("mediapost")

                    app!!.dProvider.publishPost(null, RequestFactory.formPostMediaRequest(destination, null, editText.text.toString(), uri))
                    editText.text.clear()
                }
            }
        }

        back.setOnClickListener { onBackPressed() }

        editText.setOnClickListener { mFirebaseWrapper!!.logEvent("write_description_post_media", Bundle()) }
    }

    private fun dispatchTakePictureIntent() {
        CropImage.activity()
                .setAllowRotation(false)
                .setAllowFlipping(false)
                .setBackgroundColor(R.color.black)
                .setActivityMenuIconColor(R.color.colorAccent)
                .setInitialCropWindowPaddingRatio(0f)
                .setAspectRatio(1, 1)
                .setAutoZoomEnabled(true)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == -1) {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    val resultUri = result.uri
                    image_photo.setImageURI(resultUri)

                    deferredEncodedImage = scope.async {
                        val bitmap = withContext(Dispatchers.IO) {
                            MediaStore.Images.Media.getBitmap(this@ActivityPublishMedia.contentResolver, resultUri)
                        }
                        val resized = Bitmap.createScaledBitmap(bitmap, 2048, 2048, true)
                        val baos = ByteArrayOutputStream()
                        resized.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        val b = baos.toByteArray()
                        Base64.encodeToString(b, Base64.NO_WRAP)
                    }
                }
            } else {
                onBackPressed()
            }
        }
    }

    override fun logOpen() {
        logger!!.openCreateMedia()
    }

    override fun logSend(name: String) {
        logger!!.createMedia(name)
    }
}