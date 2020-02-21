package ru.alfabank.alfamir.favorites.presentation.favorite_profile;//package ru.alfabank.alfamir.favorites.presentation.favorite_profile;
//
//import com.google.common.base.Strings;
//
//import java.util.List;
//
//import javax.inject.Inject;
//
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.disposables.CompositeDisposable;
//import io.reactivex.schedulers.Schedulers;
//import ru.alfabank.alfamir.favorites.domain.usecase.GetFavoriteProfileList;
//import ru.alfabank.alfamir.favorites.presentation.dto.FavoriteProfile;
//import ru.alfabank.alfamir.favorites.presentation.favorite_profile.contract.FavoriteProfileAdapterContract;
//import ru.alfabank.alfamir.favorites.presentation.favorite_profile.contract.FavoriteProfileContract;
//import ru.alfabank.alfamir.favorites.presentation.favorite_profile.dummy_view.FavoriteProfileViewDummy;
//import ru.alfabank.alfamir.GetImage;
//import ru.alfabank.alfamir.image.presentation.dto.Image;
//import ru.alfabank.alfamir.utility.static_utilities.InitialsMaker;
//
//public class FavoriteProfilePresenter implements FavoriteProfileContract.Presenter {
//
//    private FavoriteProfileContract.View mView;
//    private FavoriteProfileAdapterContract.Adapter mAdapter;
//    private GetFavoriteProfileList mGetFavoriteProfileList;
//    private GetImage mGetImage;
//    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
//    private List<FavoriteProfile> mFavoriteProfiles;
//    private boolean mIsDataLoaded;
//
//
//    @Inject
//    FavoriteProfilePresenter(GetFavoriteProfileList getFavoriteProfileList,
//                             GetImage fillImage){
//        mGetFavoriteProfileList = getFavoriteProfileList;
//        mGetImage = fillImage;
//    }
//
//    private void loadFavoriteProfileList(){
//        mCompositeDisposable.add(mGetFavoriteProfileList.execute(new GetFavoriteProfileList.RequestValues())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(responseValue -> {
//                    mFavoriteProfiles = responseValue.getmFavoriteProfiles();
//                    getView().showFavoriteList();
//                    getView().hideLoadingProgress();
//                }, throwable -> {
//                    getView().hideLoadingProgress();
//                }));
//    }
//
//    @Override
//    public int getListSize() {
//        return mFavoriteProfiles.size();
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return 0;
//    }
//
//    @Override
//    public void takeListAdapter(FavoriteProfileAdapterContract.Adapter adapter) {
//        mAdapter = adapter;
//    }
//
//    @Override
//    public FavoriteProfileAdapterContract.Adapter getAdapter() {
//        if (mAdapter == null) return new FavoriteProfileViewDummy();
//        return mAdapter;
//    }
//
//    @Override
//    public void takeView(FavoriteProfileContract.View view) {
//        mView = view;
////        if(mIsDataLoaded) return;
//        getView().showLoadingProgress();
//        loadFavoriteProfileList();
//    }
//
//    @Override
//    public void dropView() {
//        mView = null;
//        mAdapter = null;
//        mCompositeDisposable.dispose();
//    }
//
//    @Override
//    public FavoriteProfileContract.View getView() {
//        if (mView == null) return new FavoriteProfileViewDummy();
//        return mView;
//    }
//
//    @Override
//    public void bindListRowProfile(FavoriteProfileAdapterContract.ProfileRowView rowView, int position) {
//        FavoriteProfile favoriteProfile =  mFavoriteProfiles.get(position);
//        String email = favoriteProfile.getEmail();
//        String imageUrl = favoriteProfile.getImageUrl();
//        String login = favoriteProfile.getLogin();
//        String name = favoriteProfile.getName();
//        String title = favoriteProfile.getTitle();
//        String workPhone = favoriteProfile.getWorkPhone();
//        rowView.setName(name);
//        rowView.setTitle(title);
//        if(Strings.isNullOrEmpty(imageUrl)){
//            String initials = InitialsMaker.formInitials(name);
//            rowView.setPlaceHolder(initials);
//        } else {
//            mCompositeDisposable.add(mGetImage.execute(new GetImage.RequestValues(imageUrl, 80))
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(responseValue -> {
//                        Image image = responseValue.fillImage();
//                        String encodedImage = image.getEncodedImage();
//                        boolean isCached = image.getIsCached();
//                        if(Strings.isNullOrEmpty(encodedImage)) return; // TODO do i really need this?
//                        if(isCached){
//                            rowView.setImage(encodedImage, true);
//                        } else {
//                            getAdapter().onImageLoaded(position, 0, encodedImage, true);
//                        }
//                    }, throwable -> {
//
//                    }));
//        }
//    }
//
//    @Override
//    public void onItemClicked(int position) {
//        FavoriteProfile favoriteProfile =  mFavoriteProfiles.get(position);
//        String email = favoriteProfile.getEmail();
//        String imageUrl = favoriteProfile.getImageUrl();
//        String login = favoriteProfile.getLogin();
//        String name = favoriteProfile.getName();
//        String title = favoriteProfile.getTitle();
//        String workPhone = favoriteProfile.getWorkPhone();
//        mView.openProfileActivityUi(login);
//    }
//
//    @Override
//    public void onSearchClicked() {
//        mView.openSearchUi();
//    }
//}