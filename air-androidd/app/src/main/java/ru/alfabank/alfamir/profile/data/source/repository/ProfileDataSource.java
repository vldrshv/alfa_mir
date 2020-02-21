package ru.alfabank.alfamir.profile.data.source.repository;

import io.reactivex.Observable;
import ru.alfabank.alfamir.data.dto.ShortProfile;
import ru.alfabank.alfamir.profile.data.dto.ProfileRaw;
import ru.alfabank.alfamir.profile.data.dto.UserLikeStatusRaw;

import java.util.List;

public interface ProfileDataSource {

    Observable <ProfileRaw> getProfile(String profileId);

    Observable <Integer> getChatAvailabilityStatus(String respondentLogin);

    Observable <UserLikeStatusRaw> setLike(int targetLikeStatus, String userLogin);

    Observable <String> saveAboutMe(String aboutMeText);

    Observable <String> saveMobile(String newNumber);

    void refreshProfile();

    void getProfiles(String profileId, LoadProfilesCallback callback);

    interface LoadProfilesCallback {
        void onProfilesLoaded(List<ShortProfile> profiles);
        void onDataNotAvailable();
    }
}
