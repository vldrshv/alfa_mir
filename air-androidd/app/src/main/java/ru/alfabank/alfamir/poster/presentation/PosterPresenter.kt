package ru.alfabank.alfamir.poster.presentation

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.alfabank.alfamir.Constants
import ru.alfabank.alfamir.Constants.Initialization.SCREEN_WIDTH_DP
import ru.alfabank.alfamir.Constants.Poster.TEMP_QUESTION_KEY
import ru.alfabank.alfamir.Constants.Poster.TEMP_STRING_KEY
import ru.alfabank.alfamir.Constants.Profile.USER_IMAGE
import ru.alfabank.alfamir.Constants.Profile.USER_NAME
import ru.alfabank.alfamir.Constants.QuestionSortType
import ru.alfabank.alfamir.Constants.QuestionSortType.POPULAR
import ru.alfabank.alfamir.Constants.QuestionSortType.RECENT
import ru.alfabank.alfamir.R
import ru.alfabank.alfamir.base_elements.BaseFragment
import ru.alfabank.alfamir.image.domain.usecase.GetImage
import ru.alfabank.alfamir.poster.data.PosterDataSource
import ru.alfabank.alfamir.poster.data.PosterFragmentContract
import ru.alfabank.alfamir.poster.data.TempQuestion
import ru.alfabank.alfamir.poster.data.dto.Poster
import ru.alfabank.alfamir.poster.data.dto.Question
import ru.alfabank.alfamir.utility.static_utilities.ActivityUtils
import ru.alfabank.alfamir.utility.static_utilities.PictureClipper
import ru.alfabank.alfamir.utility.toBoolean
import ru.alfabank.alfamir.utility.toInt
import javax.inject.Inject

class PosterPresenter @Inject constructor(posterDataSource: PosterDataSource, var getImage: GetImage, val sPref: SharedPreferences) : PosterFragmentContract.Presenter {

    private var sort = POPULAR
    var dataSource: PosterDataSource = posterDataSource
    var key: String? = null
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    companion object {
        lateinit var poster: Poster
        lateinit var mView: PosterFragment
        fun poster() = poster
    }

    override fun takeView(view: PosterFragment) {
        mView = view
    }

    override fun dropView() {
    }

    override fun getView(): PosterFragment {
        return mView
    }

    @SuppressLint("CheckResult")
    fun getPoster(id: Int) {
        dataSource.getPost(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isSliDo.toBoolean()) showSliDo(it) else showPoster(it)
                }, { e -> e.printStackTrace() })
    }

    fun getImage(url: String, view: ImageView, rounded: Boolean) {
        uiScope.launch {
            getImage.bitmap(url, 48)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (rounded) {
                            view.setEncodedImage(it, true)
                        } else {
                            view.setImageBitmap(it)
                        }
                    }, Throwable::printStackTrace)
        }
    }

    private fun showSliDo(poster: Poster) {
        Companion.poster = poster
        applySort(sort)
        mView.setData(Companion.poster)
    }

    private fun showPoster(poster: Poster) {

        if (poster.imageList.isNotEmpty()) {
            CoroutineScope(Dispatchers.Main).launch {
                val result = HashMap<String, Drawable>()

                for (image in poster.imageList) {
                    getImage.bitmap(image.url, SCREEN_WIDTH_DP)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                result[image.url] = BitmapDrawable(mView.resources, it)
                                if (result.size == poster.imageList.size) {
                                    mView.setHtmlData(poster, result)
                                }
                            }, Throwable::printStackTrace)
                }
            }
        }
        mView.setHtmlData(poster, HashMap())
    }

    private fun ImageView.setEncodedImage(encodedImage: String, isAnimated: Boolean) {
        this.setEncodedImage(PictureClipper.toBitmap(encodedImage), isAnimated)
    }

    private fun ImageView.setEncodedImage(encodedImage: Bitmap, isAnimated: Boolean) {
        val roundedImage = PictureClipper.makeItRound(encodedImage)
        this.setImageDrawable(roundedImage)
        this.visibility = View.VISIBLE

        if (isAnimated) {
            this.alpha = 0f
            val mShortAnimationDuration = 200
            this.animate()
                    .alpha(1f)
                    .setDuration(mShortAnimationDuration.toLong())
                    .setListener(null)
        }
    }

    fun writeQuestion() {

        val fragment = WriteQuestionFragment()
        getTempData(TEMP_QUESTION_KEY + poster.id)?.apply {
            val bundle = Bundle()
            bundle.putString(TEMP_STRING_KEY, body)
            fragment.arguments = bundle
        }

        ActivityUtils.addFragmentToActivity((mView as BaseFragment).childFragmentManager, fragment, R.id.contentFrame, Constants.FRAGMENT_TRANSITION_ADD, true)

    }

    fun setUserPhoto(imageView: ImageView) {
        imageView.setEncodedImage(sPref.getString(USER_IMAGE, "")!!, true)
    }

    fun getUserName(): String {
        return sPref.getString(USER_NAME, "")!!
    }

    fun sendQuestion(body: String, anonymous: Int) {
        val key = saveTempData(body, anonymous)
        sendQuestion(body, anonymous, key)
    }

    fun sendQuestion(key: String) {
        getTempData(key)?.apply {
            sendQuestion(body, isAnonymous, key)
        }
    }

    @SuppressLint("CheckResult")
    private fun sendQuestion(body: String, anonymous: Int, key: String) {

        dataSource.sendQuestion(body, anonymous, poster.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    clearTempData(key)
                }, { mView.onError(key) })
    }

    private fun saveTempData(body: String, anonymous: Int): String {
        val tempQuestion = TempQuestion(body, poster.id, anonymous)
        val tempSting = Gson().toJson(tempQuestion)
        val key = TEMP_QUESTION_KEY + poster.id
        sPref.edit()
                .putString(key, tempSting)
                .apply()
        return key
    }

    private fun clearTempData(key: String) {
        sPref.edit()
                .remove(key)
                .apply()
    }

    private fun getTempData(key: String): TempQuestion? {
        sPref.getString(key, null)?.apply {
            return Gson().fromJson(this, TempQuestion::class.java)
        }
        return null
    }

    fun toggleLike(id: Int, liked: Boolean) {
        var index = 0
        poster.questions
                ?.find { it.id == id }
                ?.apply {
                    index = poster.questions?.indexOf(this)!!
                    isSetLike = liked.toInt()
                    if (liked) likeCount++ else likeCount--
                }
        mView.setItem(index, poster)

        if (liked) dataSource.like(id) else dataSource.unlike(id)

    }

    fun onSortClicked() {
        SortFragment()
                .setPresenter(this)
                .show((mView as BaseFragment).childFragmentManager, "sort_fragment")
    }

    fun applySort(type: QuestionSortType) {
        this.sort = type
        poster.questions?.apply {
            when (type) {
                POPULAR -> {
                    poster.sort = R.string.sort_type_popular
                    poster.questions = sortedWith(compareByDescending<Question> { it.likeCount }.thenByDescending { it.date })
                }
                RECENT -> {
                    poster.sort = R.string.sort_type_recent
                    poster.questions = sortedWith(compareByDescending { it.date })
                }
            }
            mView.setData(poster)
        }
    }
}