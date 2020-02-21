package ru.alfabank.alfamir.calendar_event.presentation

import android.content.Intent
import androidx.fragment.app.Fragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.alfabank.alfamir.Constants.Initialization.SCREEN_WIDTH_DP
import ru.alfabank.alfamir.Constants.Poster.IS_SLIDO
import ru.alfabank.alfamir.Constants.Poster.POSTER_ID
import ru.alfabank.alfamir.Constants.Poster.POSTER_TITLE
import ru.alfabank.alfamir.calendar_event.domain.usecase.GetCalendarEventList
import ru.alfabank.alfamir.calendar_event.presentation.contract.CalendarEventAdapterContract
import ru.alfabank.alfamir.calendar_event.presentation.dto.CalendarEvent
import ru.alfabank.alfamir.image.domain.usecase.GetImage
import ru.alfabank.alfamir.poster.presentation.PosterActivity
import javax.inject.Inject

class CalendarEventPresenter
@Inject
internal constructor(private val mGetCalendarEventList: GetCalendarEventList, private val mGetImage: GetImage) : CalendarEventAdapterContract.Presenter {

    private var mAdapter: CalendarEventAdapterContract.Adapter? = null
    private var mListener: CalendarEventAdapterContract.CalendarEventLoadListener? = null
    private var mCalendarEventList: List<CalendarEvent>? = null
    private val mCompositeDisposable = CompositeDisposable()
    private var mIsDataLoaded: Boolean = false
    private var view: androidx.fragment.app.Fragment? = null
    private val uiScope = CoroutineScope(Dispatchers.Main)

    override fun setFragmentView(fragment: androidx.fragment.app.Fragment?) {
        this.view = fragment
    }

    override fun bindListRowCalendarEvent(position: Int, rowView: CalendarEventAdapterContract.CalendarEventCardRowView) {
        val calendarEvent = mCalendarEventList!![position]
        val picUrl = calendarEvent.picUrl
        rowView.setDate(calendarEvent.date)
        rowView.setTitle(calendarEvent.title)
        rowView.setTime(calendarEvent.time)

        if (!picUrl.isNullOrEmpty()) {
            uiScope.launch {
                mGetImage.bitmap(picUrl, SCREEN_WIDTH_DP)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ rowView.setPicture(it, true) }, Throwable::printStackTrace)
            }
        }
    }

    override fun onHostViewResume(listener: CalendarEventAdapterContract.CalendarEventLoadListener) {
        mListener = listener
        if (!mIsDataLoaded) {
            loadCalendarEventList()
        } else {
            mListener?.onDataLoaded()
        }
    }

    override fun onHostViewDestroy() {
        mAdapter = null
        mListener = null
        mCompositeDisposable.dispose()
    }

    private fun loadCalendarEventList() {
        mCompositeDisposable.add(mGetCalendarEventList.execute(GetCalendarEventList.RequestValues(4))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ responseValue ->
                    mIsDataLoaded = true
                    mCalendarEventList = responseValue.calendarEventList
                    mListener!!.onDataLoaded()
                }, Throwable::printStackTrace))
    }


    override fun getListSize(): Int {
        return mCalendarEventList!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun takeListAdapter(adapter: CalendarEventAdapterContract.Adapter) {
        mAdapter = adapter
    }

    override fun getAdapter(): CalendarEventAdapterContract.Adapter? {
        return null
    }

    override fun onItemClicked(index: Int) {
        view?.apply {
            val event = mCalendarEventList!![index]
            Intent(context, PosterActivity::class.java)
                    .apply {
                        putExtra(POSTER_ID, event.id)
                        putExtra(POSTER_TITLE, event.title)
                        putExtra(IS_SLIDO, event.isSliDo)
                        startActivity(this)
                    }
        }
    }
}



























