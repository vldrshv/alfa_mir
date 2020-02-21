package ru.alfabank.alfamir.post.presentation.post.view_holder

import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.alfabank.alfamir.App
import ru.alfabank.alfamir.base_elements.AlfaVideoView
import ru.alfabank.alfamir.post.presentation.post.FullscreenActivity
import ru.alfabank.alfamir.post.presentation.post.post_contract.PostAdapterContract
import java.util.regex.Pattern


class VideoVH (itemView: View) : RecyclerView.ViewHolder(itemView), PostAdapterContract.VideoRowView {
    private lateinit var client: Api
    val BASE_ADDRESS = "https://atv.alfabank.ru/replay_alfamir/AlfaMirNews/"
    val VIDEO_STREAM_URL_PREFIX = "tracks-v1a1"
    val RESOLUTION_PREFIX = "RESOLUTION="

    private var videoStreamUrl = ""
    private var resolution = "800x600"

    private val alfaVideoView = AlfaVideoView(itemView) { openVideoFullScreen() }
    init {
        api()
    }
    /**
     * main api method to parse incoming url and get video stream
     */
    override fun configureVideo(url: String, token: String) {
        val videoName = getVideoName(url)

        client.getInfo(videoName, token).enqueue( object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                val responseString = response.body().toString()
                getUrlOfVideoStream(responseString)
                getResolution(responseString)

                if (videoStreamUrl.isNotEmpty()) {
                    videoStreamUrl = "$BASE_ADDRESS$videoName/$videoStreamUrl"
                    alfaVideoView.setVideoUri(videoStreamUrl)
                    configureView()
                }
            }
        })
    }

    private fun getVideoName(url: String): String {
        println("getVideoName=$url")
        var res = url.replace(BASE_ADDRESS, "").replace("index.m3u8", "")
        if (res[res.length - 1] == '/')
            res = res.replace("/", "")
        println("result = $res")
        return res
    }
    private fun getUrlOfVideoStream(responseString: String) {
        if (responseString.contains(VIDEO_STREAM_URL_PREFIX)) {
            val pattern = Pattern.compile("(tracks-v1a1\\S+)")
            val matcher = pattern.matcher(responseString)

            if (matcher.find())
                videoStreamUrl = responseString.substring(matcher.start(), matcher.end())
        }
    }
    private fun getResolution(responseString: String) {
        if (responseString.contains(RESOLUTION_PREFIX)) {
            val pattern = Pattern.compile("$RESOLUTION_PREFIX\\d{1,4}x\\d{1,4},")
            val matcher = pattern.matcher(responseString)

            if (matcher.find())
                resolution = responseString.substring(matcher.start(), matcher.end())
                        .replace(RESOLUTION_PREFIX, "").replace(",", "")
        }
    }

    private fun resizeVideo() {
        val displaySize = Point()
        val windowManager = itemView.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getSize(displaySize)
        alfaVideoView.setResolution(resolution, displaySize.x, displaySize.y)
    }
    private fun configureView() {
        resizeVideo()
        alfaVideoView.notifyDataChanged()
    }


    private fun openVideoFullScreen(): Boolean {
        val intent = Intent(itemView.context, FullscreenActivity::class.java)
        intent.putExtra("url", videoStreamUrl)
        intent.putExtra("type", "video")
        intent.putExtra("resolution", resolution)
        intent.putExtra("duration", alfaVideoView.getVideoDuration())

        intent.putExtra("seek_to", alfaVideoView.getVideoPosition())
        App.getAppInstance().setAppValue("seek_to", alfaVideoView.getVideoPosition())

        itemView.context.startActivity(intent)
        return true
    }

    override fun releaseVideo() {
        alfaVideoView.release()
    }

    /**
     * retrofit api configurator
     */
    private fun api() {
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_ADDRESS)
                .client(OkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
        client = retrofit.create(Api::class.java)
    }

    /**
     * interface for video downloading
     */
    interface Api {
        @GET("{videoName}/index.m3u8")
        fun getInfo(@Path("videoName") videoName: String, @Query("token") token: String ) : Call<String>
    }

}

