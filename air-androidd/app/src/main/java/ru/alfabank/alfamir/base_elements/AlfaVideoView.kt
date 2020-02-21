package ru.alfabank.alfamir.base_elements

import android.content.Context
import android.media.AudioManager
import android.os.Handler
import android.view.View
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.ContentLoadingProgressBar
import butterknife.BindView
import butterknife.ButterKnife
import ru.alfabank.alfamir.App
import ru.alfabank.alfamir.R
import ru.alfabank.alfamir.utility.video_player.TimeUtils

/**
 * Use this class for video view in app
 * Layout: post_video_viewholder.xml
 */
class AlfaVideoView(val itemView: View, val callback: () -> Boolean ) : VideoView(itemView.context) {
    @BindView(R.id.post_video_videoview)
    lateinit var videoView: VideoView
    @BindView(R.id.post_video_media_player_ll)
    lateinit var mediaPlayer: ConstraintLayout
    @BindView(R.id.post_video_media_player_play_btn)
    lateinit var playBtn: ImageButton
    @BindView(R.id.post_video_media_player_mute_btn)
    lateinit var muteBtn: ImageButton
    @BindView(R.id.post_video_media_payer_progressbar)
    lateinit var progressBar: AppCompatSeekBar
    @BindView(R.id.post_video_media_payer_current_time_tv)
    lateinit var currentTimeTv: TextView
    @BindView(R.id.post_video_media_payer_whole_time_tv)
    lateinit var wholeTimeTv: TextView
    @BindView(R.id.video_post_media_player_video_buffer_progress)
    lateinit var videoBufferingProgress: ContentLoadingProgressBar
    @BindView(R.id.post_video_media_player_fullscreen_btn)
    lateinit var videoFullscreenBtn: ImageButton
    @BindView(R.id.fullscreen_video_back_button)
    lateinit var backButton: ImageButton

    @BindView(R.id.full_screen_video_back_button_layout)
    lateinit var backButtonLayout: LinearLayoutCompat

    private var _handler: Handler
    private var stopHandler = false

    private val timeUtils = TimeUtils()

    private var currentVolume = 0
    private var prevVolume = 1

    private var resolution = Resolution()

    private var seekTo = 0
    private var _duration = 0

    private var isFullscreen = false

    init {
        ButterKnife.bind(this, itemView)
        _handler = getVideoHandler()

        initVideoView()
        initMediaPlayer()
    }

    private fun getVideoHandler() = Handler {
        updateTimeOnVideoScreen(it.what)
        updateProgressBar(it.what)
        updateBufferingInfo()
        setMuteButtonUI()
        return@Handler true
    }

    private fun initVideoView() {
        videoView.setOnClickListener { showPlayer() }
    }
    private fun initMediaPlayer() {
        mediaPlayer.setOnClickListener { v -> run {
            showPlayer()
            currentVolume = getVolume()
            setMuteButtonUI()
        }}
        playBtn.setOnClickListener { onPlayBtnClicked() }
        progressBar.setOnSeekBarChangeListener(
                object  : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, p2: Boolean) { updateTimeOnVideoScreen(progress) }
                    override fun onStartTrackingTouch(p0: SeekBar?) {}
                    override fun onStopTrackingTouch(view: SeekBar?) { videoView.seekTo(view!!.progress) }
                })
        progressBar.max = 0
        initMuteButton()

        initVideoFullscreenBtn()
        initBackButton()
    }
    private fun changeViewListener() {
        if (videoView.currentPosition != 0)
            playBtn.callOnClick()
        _duration = videoView.duration
        callback.invoke()
    }
    private fun initVideoFullscreenBtn() {
        videoFullscreenBtn.setOnClickListener { changeViewListener() }
        videoFullscreenBtn.setImageDrawable(
                if (isFullscreen)
                    videoFullscreenBtn.context.getDrawable(R.drawable.baseline_fullscreen_exit_24)
                else
                    videoFullscreenBtn.context.getDrawable(R.drawable.outline_fullscreen_24)
        )
    }
    private fun initBackButton() {
        backButton.setOnClickListener { changeViewListener() }
        backButtonLayout.visibility = if(isFullscreen) View.VISIBLE else View.GONE
    }

    private fun initMuteButton() {
        muteBtn.setOnClickListener { onMuteClicked()  }
        setMuteButtonUI()
    }
    private fun setMuteButtonUI() {
        currentVolume = getVolume()
        if (currentVolume == 0)
            muteBtn.setImageDrawable(muteBtn.context.getDrawable(R.drawable.ic_vol_mute))
        else
            muteBtn.setImageDrawable(muteBtn.context.getDrawable(R.drawable.ic_vol_unmute))
    }

    /**
     * video view updaters: current time and buffering percentage
     */
    private fun updateCurrentPositionListener() : Runnable {
        return Runnable () {
            if (stopHandler && !videoView.isPlaying)
                return@Runnable

            _handler.sendEmptyMessage(videoView.currentPosition)
            _handler.postDelayed(updateCurrentPositionListener(), 500)
        }
    }
    private fun updateTimeOnVideoScreen(timeInMills: Int) {
        setCurrentVideoTime(timeInMills)
        setEndVideoTime(getVideoDuration())
    }
    private fun updateProgressBar(timeInMills: Int) {
        progressBar.progress = timeInMills
        if (isProgressMaxConfigured())
            progressBar.max = videoView.duration
    }
    private fun isProgressMaxConfigured() =
            progressBar.max != videoView.duration && videoView.duration > 0
    private fun updateBufferingInfo() {
        val bufferingProgress = videoView.bufferPercentage * videoView.duration / 100
        if (videoView.currentPosition >= bufferingProgress) {
            videoBufferingProgress.show()
        }
        else {
            if (videoBufferingProgress.isShown)
                videoBufferingProgress.hide()
        }
        progressBar.secondaryProgress = bufferingProgress
    }

    /**
     * configure video player: set url, play and pause methods
     */
    fun setVideoUri(url: String) {
        videoView.setVideoURI(android.net.Uri.parse(url))
    }
    private fun playVideo() {
        videoView.requestFocus()
        videoView.start()
    }
    private fun pauseVideo() {
        videoView.pause()
    }

    /**
     * configure video player interface: progress bar, buffering progress, current time and duration,
     * mute btn behaviour
     */
    private fun showPlayer() {
        mediaPlayer.visibility = if (mediaPlayer.visibility == View.GONE)
        View.VISIBLE else View.GONE
    }
    private fun onPlayBtnClicked() {
        if (videoView.isPlaying) {
            pauseVideo()
            playBtn.setImageDrawable(playBtn.context.getDrawable(R.drawable.exo_icon_play))
        } else {
            playVideo()
            playBtn.setImageDrawable(playBtn.context.getDrawable(R.drawable.ic_playback_pause))
        }
    }
    private fun onMuteClicked() {
        currentVolume = getVolume()
        saveVolumeState()
        setVolume(currentVolume)
        setMuteButtonUI()
    }

    private fun saveVolumeState() {
        if (currentVolume == 0)
            currentVolume = prevVolume
        else {
            prevVolume = currentVolume
            currentVolume = 0
        }
    }
    private fun setVolume(volume: Int) {
        val am = videoView.context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        am.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0)
    }
    private fun getVolume() : Int {
        val am = videoView.context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return am.getStreamVolume(AudioManager.STREAM_MUSIC)
    }

    fun getVideoPosition() = videoView.currentPosition
    fun setSeekTo(position: Int) {
        videoView.seekTo(position)
        seekTo = position
    }
    fun setVideoDuration(duration: Int) {
        progressBar.max = duration
        setEndVideoTime(duration)
    }
    fun getVideoDuration() = videoView.duration

    private fun setCurrentVideoTime(timeInMills: Int) {
        currentTimeTv.text = timeUtils.getMinSec(timeInMills)
    }
    private fun setEndVideoTime(duration: Int) {
        val videoEndTime = wholeTimeTv.text.toString()
        if (!videoEndTime.contains(":") || videoEndTime == "00:00") {
            wholeTimeTv.text = timeUtils.getMinSec(duration)
        }
    }
    fun setIsFullscreen(isFullscreen: Boolean) {
        this.isFullscreen = isFullscreen
    }

    private fun checkPosition() {
        val seekToAppSettings = App.getAppInstance().getAppValue("seek_to") ?: 0
        if ((seekToAppSettings as Int) > seekTo)
            setSeekTo(seekToAppSettings)
    }

    fun notifyDataChanged() {
        checkPosition()
        updateTimeOnVideoScreen(seekTo)
        updateProgressBar(seekTo)
        initVideoFullscreenBtn()
        initBackButton()

        stopHandler = false
        _handler.postDelayed(updateCurrentPositionListener(), 100)
    }

    fun release() {
        stopHandler = true
        videoView.stopPlayback()
    }

    /**
     * @param _resolution - video resolution from server as 1920x1080
     * @param screenWidth - width of phone screen
     * @param screenHeight - height of phone screen
     *
     * Screen width and height are used to initialize max values.
     * _Resolution is used to get the video aspect ratio
     */
    fun setResolution(_resolution: String, screenWidth: Int, screenHeight: Int) {
        videoView.layoutParams.apply {
            resolution = Resolution(_resolution)
            val ratio = resolution.ratio()
            if (screenHeight / ratio > screenWidth ) {
                width = screenWidth
                height = (screenWidth * ratio).toInt()
            } else {
                width = (screenHeight / ratio).toInt()
                height = screenHeight
            }
            println("w * h = $height*$width")
        }
    }
    fun getResolution() = resolution.toString()

    private class Resolution(val _resolution: String = "800x600") {
        private var width = 0
        private var height = 0

        init {
            parse()
        }

        private fun parse() {
            if (!_resolution.contains("x"))
                setResolution(800, 600)
            else {
                val resInt = _resolution.split("x")
                if (resInt.size == 2)
                    setResolution(resInt[0].parseInt(), resInt[1].parseInt())
                else
                    setResolution(800, 600)
            }
        }

        private fun setResolution(w: Int, h: Int) {
            this.width = w
            this.height = h
        }

        fun ratio(): Double = height.toDouble() / width

        private fun String.parseInt() : Int {
            var a = 0
            try {
                a = toInt()
            } catch (e: NumberFormatException) {
                forEach { a += it.toInt() }
            } finally {
                return a
            }
        }

        override fun toString(): String {
            return "${width}x${height}"
        }
    }
}