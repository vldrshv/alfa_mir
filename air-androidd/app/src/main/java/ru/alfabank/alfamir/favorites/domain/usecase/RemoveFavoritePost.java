package ru.alfabank.alfamir.favorites.domain.usecase;

public class RemoveFavoritePost  {

//    private FavoriteRepository mFavoriteRepository;

//    @Inject
//    RemoveFavoritePost(FavoriteRepository favoriteRepository) {
//        mFavoriteRepository = favoriteRepository;
//    }

//    public Observable<ResponseValue> execute(RequestValues requestValues) {
//        String postId = requestValues.getPostId();
//        return mFavoriteRepository.removeFavoritePage(postId)
//                .flatMap(s -> {
//                    mFavoriteRepository.refreshPostList();
//                    return Observable.just(new ResponseValue());
//                });
//    }

//    public void execute(String postId) {
//         mFavoriteRepository.removeFavoritePage(postId);
//                .flatMap(s -> {
//                    mFavoriteRepository.refreshPostList();
//                    return Observable.just(new ResponseValue());
//                });
//    }

//    public static class RequestValues implements UseCase.RequestValues {
//        private String mPostId;
//        public RequestValues(String postId){
//            mPostId = postId;
//        }
//        public String getPostId() {
//            return mPostId;
//        }
//    }
//
//    public static class ResponseValue implements UseCase.ResponseValue {
//
//    }
}