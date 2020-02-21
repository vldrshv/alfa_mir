package ru.alfabank.alfamir.people.presentation

import com.google.common.base.Strings
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.alfabank.alfamir.image.data.source.repository.ImageRepository
import ru.alfabank.alfamir.image.domain.usecase.GetImage
import ru.alfabank.alfamir.people.presentation.dummy_view.PeopleAdapterDummy
import ru.alfabank.alfamir.people.presentation.post_contract.PeopleListContract
import ru.alfabank.alfamir.profile.data.source.repository.ProfileRepository
import ru.alfabank.alfamir.utility.static_utilities.InitialsMaker
import ru.alfabank.alfamir.utility.static_utilities.LinkHandler
import javax.inject.Inject

/**
 * Created by U_M0WY5 on 12.04.2018.
 */

class PeopleListPresenter
@Inject
internal constructor(private val mProfileId: String,
                     private val mGetImage: GetImage,
                     private val mProfileRepository: ProfileRepository,
                     private val mImageRepository: ImageRepository) : PeopleListContract.ListPresenter {
    private var mAdapter: PeopleListContract.ListAdapter? = null
    private val mCompositeDisposable = CompositeDisposable()

    override fun getListSize(): Int {
        return mProfileRepository.getCashedProfilesListSize(mProfileId)
    }

    override fun bindListRowView(position: Int, rowView: PeopleListContract.ListRowView) {
        val profile = mProfileRepository.getCachedShortProfileAtPosition(position, mProfileId)
        val name = profile.name
        val title = profile.title
        val url = LinkHandler.getPhotoLink(profile.imageUrl)

        rowView.setName(name)
        rowView.setTitle(title ?: "")
        rowView.setProfileImage(null, false) // / mb put it after image onCallClicked?

        if (Strings.isNullOrEmpty(url)) {
            val initials = InitialsMaker.formInitials(name)
            rowView.setInitials(initials)
            return
        } else {
            rowView.clearInitials()
        }
        loadPicture(url, position)
    }

    override fun takeListAdapter(adapter: PeopleListContract.ListAdapter) {
        mAdapter = adapter
    }

    override fun onItemClicked(position: Int) {
        val profile = mProfileRepository.getCachedShortProfileAtPosition(position, mProfileId)
        val id = profile.id
        mAdapter!!.openActivityProfileUi(id)
    }

    override fun getListView(): PeopleListContract.ListAdapter {
        if (mAdapter == null) {
            mAdapter = PeopleAdapterDummy()
        }
        return mAdapter!!
    }

    private fun loadPicture(url: String, position: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            mCompositeDisposable.add(mGetImage.bitmap(url, 48)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ mAdapter!!.onImageDownloaded(position, it, true) }, Throwable::printStackTrace))
        }
    }
}
