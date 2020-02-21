package ru.alfabank.alfamir.profile.data.source.repository;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import ru.alfabank.alfamir.data.dto.Profile;
import ru.alfabank.alfamir.data.dto.ShortProfile;
import ru.alfabank.alfamir.di.qualifiers.Remote;
import ru.alfabank.alfamir.profile.data.dto.ProfileRaw;
import ru.alfabank.alfamir.profile.data.dto.UserLikeStatusRaw;

@Singleton
public class ProfileRepository implements ProfileDataSource {

    private final ProfileDataSource mProfileRemoteDataSource;
    private Map<String, ProfileRaw> mCachedProfileList = new LinkedHashMap<>();
    private Map<String, Profile> mCachedFullProfiles;
    private Map<String, List<ShortProfile>> mCachedShortProfiles;
    private boolean mShortCacheIsDirty;

    private volatile boolean mIsCacheDirty = false;
    private Object mLock = new Object();

    @Inject
    ProfileRepository(@Remote ProfileDataSource profileRemoteDataSource) {
        mProfileRemoteDataSource = profileRemoteDataSource;
    }

    @Override
    public Observable<ProfileRaw> getProfile(String profileId) {
        profileId = profileId.toUpperCase();

        return mProfileRemoteDataSource.getProfile(profileId)
                .flatMap(profileRaw -> {
                    saveToCache(profileRaw);
                    return Observable.just(profileRaw);
                });
    }

    @Override
    public Observable<Integer> getChatAvailabilityStatus(String respondentLogin) {
        return mProfileRemoteDataSource.getChatAvailabilityStatus(respondentLogin);
    }

    @Override
    public Observable<UserLikeStatusRaw> setLike(int targetLikeStatus, String userLogin) {
        return mProfileRemoteDataSource.setLike(targetLikeStatus, userLogin).flatMap(userLikeStatusRaw -> {
            int currentCount = userLikeStatusRaw.getCurrentLikes();
            int isLiked = userLikeStatusRaw.getCurrentLikes();
            if (mCachedFullProfiles != null) {
                Profile cachedProfile = mCachedFullProfiles.get(userLogin);
                cachedProfile.setLikes(currentCount);
                cachedProfile.setLiked(isLiked);
            }
            return Observable.just(userLikeStatusRaw);
        });
    }

    @Override
    public Observable<String> saveAboutMe(String aboutMeText) {
        return mProfileRemoteDataSource.saveAboutMe(aboutMeText);
    }

    @Override
    public Observable<String> saveMobile(String newNumber) {
        return mProfileRemoteDataSource.saveMobile(newNumber);
    }

    @Override
    public void getProfiles(String profileId, LoadProfilesCallback callback) {
        if (mCachedShortProfiles != null && mCachedShortProfiles.containsKey(profileId) && !mShortCacheIsDirty) {
            List<ShortProfile> profiles = mCachedShortProfiles.get(profileId);
            callback.onProfilesLoaded(profiles);
            return;
        }
        getProfilesFromRemoteDataSource(profileId, callback);
    }

    @Override
    public void refreshProfile() {
    }

    private void saveToCache(ProfileRaw profileRaw) {
        if (profileRaw.getLogin() == null)
            return;

        mCachedProfileList.put(profileRaw.getLogin().toUpperCase(), profileRaw);
    }

    private void saveToCache(String id, List<ShortProfile> profiles) {
        if (mCachedShortProfiles == null) {
            mCachedShortProfiles = new HashMap<>();
        }
        mCachedShortProfiles.put(id, profiles);
        mShortCacheIsDirty = false;
    }

    public void refreshShortProfiles() {
        mShortCacheIsDirty = true;
    }

    private void getProfilesFromRemoteDataSource(String profileId, LoadProfilesCallback callback) {
        mProfileRemoteDataSource.getProfiles(profileId, new LoadProfilesCallback() {
            @Override
            public void onProfilesLoaded(List<ShortProfile> profiles) {
                saveToCache(profileId, profiles);
                callback.onProfilesLoaded(profiles);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    public int getCashedProfilesListSize(String id) {
        if (mCachedShortProfiles != null && mCachedShortProfiles.containsKey(id)) {
            List<ShortProfile> profiles = mCachedShortProfiles.get(id);
            return profiles.size();
        } else {
            return -1;
        }
    }

    public ShortProfile getCachedShortProfileAtPosition(int position, String id) {
        ShortProfile profile;
        if (mCachedShortProfiles != null && mCachedShortProfiles.containsKey(id)) {
            List<ShortProfile> profiles = mCachedShortProfiles.get(id);
            profile = profiles.get(position);
        } else {
            profile = new ShortProfile();
        }
        return profile;
    }

}