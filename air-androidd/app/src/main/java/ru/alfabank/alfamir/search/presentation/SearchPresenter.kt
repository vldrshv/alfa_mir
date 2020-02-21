package ru.alfabank.alfamir.search.presentation

import com.google.common.base.Strings
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.alfabank.alfamir.image.domain.usecase.GetImage
import ru.alfabank.alfamir.search.domain.usecase.Search
import ru.alfabank.alfamir.search.domain.usecase.SearchMore
import ru.alfabank.alfamir.search.domain.usecase.UpdateSearchResult
import ru.alfabank.alfamir.search.presentation.contract.SearchAdapterContract
import ru.alfabank.alfamir.search.presentation.contract.SearchFragmentContract
import ru.alfabank.alfamir.search.presentation.dto.DisplayableSearchItem
import ru.alfabank.alfamir.search.presentation.dto.Page
import ru.alfabank.alfamir.search.presentation.dto.Person
import ru.alfabank.alfamir.search.presentation.dummy_view.SearchViewDummy
import ru.alfabank.alfamir.utility.static_utilities.InitialsMaker

import javax.inject.Inject

class SearchPresenter @Inject
internal constructor(private val mSearch: Search,
                     private val mSearchMore: SearchMore,
                     private val mGetImage: GetImage,
                     private val mUpdateSearchResult: UpdateSearchResult) : SearchFragmentContract.Presenter {

    private var mView: SearchFragmentContract.View? = null
    private var mAdapter: SearchAdapterContract.Adapter? = null
    private val mCompositeDisposable = CompositeDisposable()
    private var mDisplayableSearchItemList: List<DisplayableSearchItem>? = null

    private var mIsAdapterInitialized: Boolean = false

    override fun takeView(view: SearchFragmentContract.View) {
        mView = view
        mView!!.showKeyboard()
    }

    override fun dropView() {
        mCompositeDisposable.dispose()
        mView = null
    }

    override fun getView(): SearchFragmentContract.View {
        return if (mView == null) SearchViewDummy() else mView!!
    }

    override fun onSearchInput(input: String) {
        if (input.length < 3) return
        view.showLoading()
        mCompositeDisposable.add(mSearch.execute(Search.RequestValues(input))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ responseValue ->
                    mDisplayableSearchItemList = responseValue.displayableSearchItemList
                    if (mIsAdapterInitialized) {
                        adapter.onDataSetChanged()
                    } else {
                        view.showResults()
                        mIsAdapterInitialized = true
                    }
                    view.hideLoading()
                }, { view.hideLoading() }))
    }

    override fun onSearchMore(input: String, morePeople: Boolean, morePages: Boolean) {

    }

    override fun bindListRowPage(position: Int, rowView: SearchAdapterContract.SearchPageRowView) {
        val item = mDisplayableSearchItemList!![position]
        val page = item as Page
        val pubDate = page.publishedDate
        val title = page.title
        val url = page.imageUrl
        val viewId = page.viewId
        rowView.setTitle(title)
        rowView.setDate(pubDate)
        rowView.clearImage()

        if (Strings.isNullOrEmpty(url)) {
            rowView.hideImage()
        } else {
            rowView.showImage()

            CoroutineScope(Dispatchers.Main).launch {
                mCompositeDisposable.add(mGetImage.bitmap(url, 48)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ rowView.setPicture(it, true) }, Throwable::printStackTrace))
            }
        }
    }

    override fun bindListRowPerson(position: Int, rowView: SearchAdapterContract.SearchPersonRowView) {
        val item = mDisplayableSearchItemList!![position]
        val person = item as Person
        val name = person.name
        val personPosition = person.position
        val imageUrl = person.imageUrl
        val viewId = person.viewId
        rowView.setName(name)
        rowView.setPosition(personPosition)
        rowView.clearImage()

        if (Strings.isNullOrEmpty(imageUrl)) {
            val initials = InitialsMaker.formInitials(name)
            rowView.setPlaceHolder(initials)
        } else {

            CoroutineScope(Dispatchers.Main).launch {
                mCompositeDisposable.add(mGetImage.bitmap(imageUrl, 48)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ rowView.setPicture(it, true) }, Throwable::printStackTrace))
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return mDisplayableSearchItemList!![position].viewId
    }

    override fun getListSize(): Int {
        return mDisplayableSearchItemList!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return mDisplayableSearchItemList!![position].type
    }

    override fun takeListAdapter(adapter: SearchAdapterContract.Adapter) {
        mAdapter = adapter
    }

    override fun getAdapter(): SearchAdapterContract.Adapter {
        return if (mAdapter == null) SearchViewDummy() else mAdapter!!
    }

    override fun onPageMoreClicked() {
        loadMore(morePeople = false, morePages = true)
    }

    override fun onPersonMoreClicked() {
        loadMore(morePeople = true, morePages = false)
    }

    private fun loadMore(morePeople: Boolean, morePages: Boolean) {
        view.showLoading()
        val keyword = view.userInput
        mCompositeDisposable.add(mSearchMore.execute(SearchMore.RequestValues(morePeople, morePages, keyword))
                .subscribeOn(Schedulers.io())
                .flatMap { responseValue ->
                    val pageList = responseValue.pageList
                    val personList = responseValue.personList
                    mUpdateSearchResult.execute(UpdateSearchResult.RequestValues(mDisplayableSearchItemList, personList, pageList))
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ responseValue ->
                    mDisplayableSearchItemList = responseValue.displayableSearchItemList
                    adapter.onDataSetChanged()
                    view.hideLoading()
                }, { view.hideLoading() }))
    }

    override fun onPersonClicked(position: Int) {
        if (position == -1) return
        val item = mDisplayableSearchItemList!![position]
        val person = item as Person
        val id = person.id
        view.hideKeyboard()
        view.openProfileUi(id)
    }

    override fun onPageClicked(position: Int) {
        if (position == -1) return
        val item = mDisplayableSearchItemList!![position]
        val page = item as Page
        val feedId = page.feedId
        val postId = page.id
        val feedUrl = page.feedId
        val feedType = page.feedType
        view.hideKeyboard()
        adapter.openPostUi(feedId, postId, feedUrl, feedType)
    }
}
