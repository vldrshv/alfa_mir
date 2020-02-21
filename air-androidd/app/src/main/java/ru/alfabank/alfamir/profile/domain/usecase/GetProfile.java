package ru.alfabank.alfamir.profile.domain.usecase;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.profile.data.source.repository.ProfileRepository;
import ru.alfabank.alfamir.profile.domain.mapper.ProfileMapper;
import ru.alfabank.alfamir.profile.presentation.dto.Profile;

public class GetProfile {

    private ProfileRepository mProfileRepository;
    private ProfileMapper mProfileMapper;

    @Inject
    public GetProfile(ProfileRepository profileRepository, ProfileMapper profileMapper) {
        mProfileRepository = profileRepository;
        mProfileMapper = profileMapper;
    }

    public Observable<Profile> execute(String profileId) {
        return mProfileRepository.getProfile(profileId)
                .map(mProfileMapper)
                .flatMap(Observable::just);
    }
}