package ru.alfabank.alfamir.post.presentation.post

import android.content.pm.ActivityInfo
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_SENSOR
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import ru.alfabank.alfamir.App
import ru.alfabank.alfamir.R
import ru.alfabank.alfamir.base_elements.AlfaVideoView
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class FullscreenActivity : AppCompatActivity() {

    lateinit var imageView: SubsamplingScaleImageView
    lateinit var videoViewLayout: LinearLayoutCompat
    lateinit var imageLayout: ConstraintLayout
    private lateinit var alfaVideo: AlfaVideoView
    private lateinit var root: View
    private lateinit var imageBackButton: ImageButton

    private var contentType = ""
    private var url = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen)

        initUI()
        getGlobalExtras()


        when (contentType) {
            "video" -> initVideoView()
            "image" -> initImageView()
        }
    }

    private fun initUI() {
        root = this.findViewById<View>(android.R.id.content)

        initVideo()
        initImage()
    }
    private fun getGlobalExtras() {
        contentType = intent.getStringExtra("type")
        url = intent.getStringExtra("url")
    }

    /**
     * Open image full screen in any scape - portrait or land
     */
    private fun initImage() {
        imageLayout = findViewById(R.id.full_screen_image_layout)
        imageView = findViewById(R.id.full_screen_image_view)
        imageBackButton = findViewById(R.id.full_screen_image_back_button)
        imageBackButton.setOnClickListener { backPressed() }
    }
    private fun initImageView() {
        hideAndShow(videoViewLayout, imageLayout)
        imageView.maxScale = 5.0f
        val name = intent.getStringExtra("name")
        loadImageFromStorage(url, name)
    }
    private fun loadImageFromStorage(path: String, name: String) {
        try {
            val f = File(path, name)
            val bitmap = BitmapFactory.decodeStream(FileInputStream(f));
            imageView.setImage(ImageSource.bitmap(bitmap))
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    /**
     * Open video full screen
     */
    private fun initVideo() {
        videoViewLayout = findViewById(R.id.full_screen_video_view)
        alfaVideo = AlfaVideoView(root) { backPressed() }
    }
    private fun initVideoView() {
        hideAndShow(imageLayout, videoViewLayout)

        val seekTo = intent.getIntExtra("seek_to", 0)
        val resolution = intent.getStringExtra("resolution")
        val duration = intent.getIntExtra("duration", 0)

        initVideoFullScreen()
        resizeVideo(resolution)
        initAlfaVideo(seekTo, duration)
    }

    private fun initVideoFullScreen() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        this.window.decorView.postDelayed(
                { requestedOrientation = SCREEN_ORIENTATION_SENSOR }, 1000L)
        this.window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }
    }
    private fun resizeVideo(resolution: String = "800x600") {
        val displaySize = getDisplaySize()
        alfaVideo.setResolution(resolution, displaySize.y, displaySize.x)
    }
    private fun getDisplaySize(): Point{
        val displaySize = Point()
        windowManager.defaultDisplay.getSize(displaySize)
        return displaySize
    }

    private fun initAlfaVideo(seekTo: Int, duration: Int) {
        alfaVideo.setIsFullscreen(true)
        alfaVideo.setVideoUri(url)
        alfaVideo.setSeekTo(seekTo)
        alfaVideo.setVideoDuration(duration)
        alfaVideo.notifyDataChanged()
    }
    private fun hideAndShow(hideView: View, showView: View){
        hideView.visibility = View.GONE
        showView.visibility = View.VISIBLE
    }

    private fun backPressed(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        App.getAppInstance().setAppValue("seek_to", alfaVideo.getVideoPosition())
        alfaVideo.release()
        super.onBackPressed()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val displaySize = getDisplaySize()
        println("RESOLUTION = ${alfaVideo.getResolution()}")

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            alfaVideo.setResolution(alfaVideo.getResolution(), displaySize.x, displaySize.y)
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            alfaVideo.setResolution(alfaVideo.getResolution(), displaySize.x, displaySize.y)
        }
    }


}
