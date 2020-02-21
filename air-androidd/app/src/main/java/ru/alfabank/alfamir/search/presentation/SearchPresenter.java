package ru.alfabank.alfamir.search.presentation;//package ru.alfabank.alfamir.search.presentation;
//
//import com.google.common.base.Strings;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.disposables.CompositeDisposable;
//import io.reactivex.schedulers.Schedulers;
//import ru.alfabank.alfamir.GetImage;
//import ru.alfabank.alfamir.search.domain.usecase.Search;
//import ru.alfabank.alfamir.search.domain.usecase.SearchMore;
//import ru.alfabank.alfamir.search.domain.usecase.UpdateSearchResult;
//import ru.alfabank.alfamir.search.presentation.contract.SearchAdapterContract;
//import ru.alfabank.alfamir.search.presentation.contract.SearchFragmentContract;
//import ru.alfabank.alfamir.search.presentation.dto.DisplayableSearchItem;
//import ru.alfabank.alfamir.search.presentation.dto.Page;
//import ru.alfabank.alfamir.search.presentation.dto.Person;
//import ru.alfabank.alfamir.search.presentation.dummy_view.SearchViewDummy;
//import ru.alfabank.alfamir.utility.static_utilities.InitialsMaker;
//
//import javax.inject.Inject;
//import java.util.List;
//
//public class SearchPresenter implements SearchFragmentContract.Presenter {
//
//    private SearchFragmentContract.View mView;
//    private SearchAdapterContract.Adapter mAdapter;
//    private Search mSearch;
//    private SearchMore mSearchMore;
//    private GetImage mGetImage;
//    private UpdateSearchResult mUpdateSearchResult;
//    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
//    private List<DisplayableSearchItem> mDisplayableSearchItemList;
//
//    private boolean mIsAdapterInitialized;
//
//    @Inject
//    SearchPresenter(Search search,
//                    SearchMore searchMore,
//                    GetImage fillImage,
//                    UpdateSearchResult updateSearchResult) {
//        mSearch = search;
//        mSearchMore = searchMore;
//        mGetImage = fillImage;
//        mUpdateSearchResult = updateSearchResult;
//    }
//
//    @Override
//    public void takeView(SearchFragmentContract.View view) {
//        mView = view;
//        mView.showKeyboard();
//    }
//
//    @Override
//    public void dropView() {
//        mCompositeDisposable.dispose();
//        mView = null;
//    }
//
//    @Override
//    public SearchFragmentContract.View getView() {
//        if (mView == null) return new SearchViewDummy();
//        return mView;
//    }
//
//    @Override
//    public void onSearchInput(String input) {
//        if (input.length() < 3) return;
//        getView().showLoading();
//        mCompositeDisposable.add(mSearch.execute(new Search.RequestValues(input))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(responseValue -> {
//                    mDisplayableSearchItemList = responseValue.getDisplayableSearchItemList();
//                    if (mIsAdapterInitialized) {
//                        getAdapter().onDataSetChanged();
//                    } else {
//                        getView().showResults();
//                        mIsAdapterInitialized = true;
//                    }
//                    getView().hideLoading();
//                }, throwable -> getView().hideLoading()));
//    }
//
//    @Override
//    public void onSearchMore(String input, boolean morePeople, boolean morePages) {
//
//    }
//
//    @Override
//    public void bindListRowPage(int position, SearchAdapterContract.SearchPageRowView rowView) {
//        DisplayableSearchItem item = mDisplayableSearchItemList.get(position);
//        Page page = (Page) item;
//        String pubDate = page.getPublishedDate();
//        String title = page.getTitle();
//        String url = page.getImageUrl();
//        long viewId = page.getViewId();
//        rowView.setTitle(title);
//        rowView.setDate(pubDate);
//        rowView.clearImage();
//
//        if (Strings.isNullOrEmpty(url)) {
//            rowView.hideImage();
//        } else {
//            rowView.showImage();
//            mCompositeDisposable.add(mGetImage.execute(url, 48)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(image -> {
//                        String encodedImage = image.getEncodedImage();
//                        boolean isCached = image.getIsCached();
//                        if (Strings.isNullOrEmpty(encodedImage)) return; // TODO do i really need this?
//                        if (isCached) {
//                            rowView.setPicture(encodedImage, !isCached);
//                        } else {
//                            getAdapter().onPostImageLoaded(position, viewId, encodedImage, !isCached);
//                        }
//                    }, Throwable::printStackTrace));
//        }
//    }
//
//    @Override
//    public void bindListRowPerson(int position, SearchAdapterContract.SearchPersonRowView rowView) {
//        DisplayableSearchItem item = mDisplayableSearchItemList.get(position);
//        Person person = (Person) item;
//        String name = person.getName();
//        String personPosition = person.getPosition();
//        String imageUrl = person.getImageUrl();
//        long viewId = person.getViewId();
//        rowView.setName(name);
//        rowView.setPosition(personPosition);
//        rowView.clearImage();
//
//        if (Strings.isNullOrEmpty(imageUrl)) {
//            String initials = InitialsMaker.formInitials(name);
//            rowView.setPlaceHolder(initials);
//        } else {
//            mCompositeDisposable.add(mGetImage.execute(imageUrl, 48)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(image -> {
//                        String encodedImage = image.getEncodedImage();
//                        boolean isCached = image.getIsCached();
//                        if (Strings.isNullOrEmpty(encodedImage)) return; // TODO do i really need this?
//                        if (isCached) {
//                            rowView.setPicture(encodedImage, !isCached);
//                        } else {
//                            getAdapter().onProfileImageLoaded(position, viewId, encodedImage, !isCached);
//                        }
//                    }, Throwable::printStackTrace));
//        }
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return mDisplayableSearchItemList.get(position).getViewId();
//    }
//
//    @Override
//    public int getListSize() {
//        return mDisplayableSearchItemList.size();
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return mDisplayableSearchItemList.get(position).getType();
//    }
//
//    @Override
//    public void takeListAdapter(SearchAdapterContract.Adapter adapter) {
//        mAdapter = adapter;
//    }
//
//    @Override
//    public SearchAdapterContract.Adapter getAdapter() {
//        if (mAdapter == null) return new SearchViewDummy();
//        return mAdapter;
//    }
//
//    @Override
//    public void onPageMoreClicked() {
//        loadMore(false, true);
//    }
//
//    @Override
//    public void onPersonMoreClicked() {
//        loadMore(true, false);
//    }
//
//    private void loadMore(boolean morePeople, boolean morePages) {
//        getView().showLoading();
//        String keyword = getView().getUserInput();
//        mCompositeDisposable.add(mSearchMore.execute(new SearchMore.RequestValues(morePeople, morePages, keyword))
//                .subscribeOn(Schedulers.io())
//                .flatMap(responseValue -> {
//                    List<Page> pageList = responseValue.getPageList();
//                    List<Person> personList = responseValue.getPersonList();
//                    return mUpdateSearchResult.execute(new UpdateSearchResult.RequestValues(mDisplayableSearchItemList, personList, pageList));
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(responseValue -> {
//                    mDisplayableSearchItemList = responseValue.getDisplayableSearchItemList();
//                    getAdapter().onDataSetChanged();
//                    getView().hideLoading();
//                }, throwable -> getView().hideLoading()));
//    }
//
//    @Override
//    public void onPersonClicked(int position) {
//        if (position == -1) return;
//        DisplayableSearchItem item = mDisplayableSearchItemList.get(position);
//        Person person = (Person) item;
//        String id = person.getId();
//        getView().hideKeyboard();
//        getView().openProfileUi(id);
//    }
//
//    @Override
//    public void onPageClicked(int position) {
//        if (position == -1) return;
//        DisplayableSearchItem item = mDisplayableSearchItemList.get(position);
//        Page page = (Page) item;
//        String feedId = page.getFeedId();
//        String postId = page.getId();
//        String feedUrl = page.getFeedId();
//        String feedType = page.getFeedType();
//        getView().hideKeyboard();
//        getAdapter().openPostUi(feedId, postId, feedUrl, feedType);
//    }
//}
