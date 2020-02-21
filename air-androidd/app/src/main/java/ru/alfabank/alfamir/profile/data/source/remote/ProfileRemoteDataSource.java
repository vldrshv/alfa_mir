package ru.alfabank.alfamir.profile.data.source.remote;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.data.dto.ShortProfile;
import ru.alfabank.alfamir.data.source.remote.api.WebService;
import ru.alfabank.alfamir.profile.data.dto.ProfileRaw;
import ru.alfabank.alfamir.profile.data.dto.ProfileWrapper;
import ru.alfabank.alfamir.profile.data.dto.UserLikeStatusRaw;
import ru.alfabank.alfamir.profile.data.source.repository.ProfileDataSource;
import ru.alfabank.alfamir.utility.logging.local.LogWrapper;
import ru.alfabank.alfamir.utility.static_utilities.JsonWrapper;
import ru.alfabank.alfamir.utility.static_utilities.RequestFactory;

import static ru.alfabank.alfamir.Constants.Log.LOG_PROFILE;

public class ProfileRemoteDataSource implements ProfileDataSource {

    private final WebService service;
    private LogWrapper mLog;

    @Inject
    ProfileRemoteDataSource(WebService service,
                            LogWrapper log) {
        this.service = service;
        mLog = log;
    }

    @Override
    public Observable<ProfileRaw> getProfile(String profileId) {
        String request = RequestFactory.formProfileRequest(profileId);
        mLog.debug(LOG_PROFILE, "getProfile request: " + request);
        return service.requestX(request).map(response -> {
            mLog.debug(LOG_PROFILE, "getProfile response = " + response);
            ProfileWrapper profileWrapper = JsonWrapper.getProfileWrapperRaw(response);
            return profileWrapper.getProfileRaw();
        });
    }

    @Override
    public Observable<Integer> getChatAvailabilityStatus(String respondentLogin) {
        String request = RequestFactory.formChatAvailabilityRequest(respondentLogin);
        mLog.debug(LOG_PROFILE, "getChatAvailabilityStatus request = " + request);
        return service.requestX(request).map(response -> {
            mLog.debug(LOG_PROFILE, "getChatAvailabilityStatus response = " + response);
            return Integer.parseInt(response);
        });
    }

    @Override
    public Observable<UserLikeStatusRaw> setLike(int targetLikeStatus, String userLogin) {
        String request = RequestFactory.formLikeUserRequest(targetLikeStatus, userLogin);
        return service.requestX(request).map(JsonWrapper::getUserLikeStatusRaw);
    }

    @Override
    public Observable<String> saveAboutMe(String aboutMeText) {
        String request = RequestFactory.formSaveAboutMe(aboutMeText);
        return service.requestX(request);
    }

    @Override
    public Observable<String> saveMobile(String newNumber) {
        String request = RequestFactory.INSTANCE.formSaveMobileRequest(newNumber);
        return service.requestX(request);
    }

    @Override
    public void getProfiles(String profileId, LoadProfilesCallback callback) {
        service.getProfiles(profileId, new LoadProfilesCallback() {
            @Override
            public void onProfilesLoaded(List<ShortProfile> profiles) {
                callback.onProfilesLoaded(profiles);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void refreshProfile() {
        // all job is done in the repository
    }
}
