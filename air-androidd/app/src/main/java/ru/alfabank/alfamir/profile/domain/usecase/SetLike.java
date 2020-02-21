package ru.alfabank.alfamir.profile.domain.usecase;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.base_elements.UseCase;
import ru.alfabank.alfamir.profile.data.source.repository.ProfileRepository;

public class SetLike extends UseCase<SetLike.RequestValues, SetLike.ResponseValue> {

    private ProfileRepository mProfileRepository;

    @Inject
    SetLike(ProfileRepository profileRepository) {
        mProfileRepository = profileRepository;
    }

    @Override
    public Observable<ResponseValue> execute(RequestValues requestValues) {
        int targetLikeStatus = requestValues.getTargetLikeStatus();
        String userLogin = requestValues.getUserLogin();

        return mProfileRepository.setLike(targetLikeStatus, userLogin)
                .flatMap(userLikeStatusRaw -> {
                    int currentLikes = userLikeStatusRaw.getCurrentLikes();
                    int isLiked = userLikeStatusRaw.isLiked();
                    return Observable.just(new ResponseValue(currentLikes, isLiked));
                });
    }

    public static class RequestValues implements UseCase.RequestValues {
        private int mTargetLikeStatus;
        private String mUserLogin;
        public RequestValues(String userLogin,
                             int targetLikeStatus) {
            mUserLogin = userLogin;
            mTargetLikeStatus = targetLikeStatus;
        }
        int getTargetLikeStatus() {
            return mTargetLikeStatus;
        }
        String getUserLogin() {
            return mUserLogin;
        }
    }

    public static class ResponseValue implements UseCase.ResponseValue {
        private int mCurrentLikes;
        private int mIsLiked;
        public ResponseValue(int currentLikes, int isLiked) {
            mCurrentLikes = currentLikes;
            mIsLiked = isLiked;
        }

        public int getCurrentLikes() {
            return mCurrentLikes;
        }

        public int getIsLiked() {
            return mIsLiked;
        }
    }
}