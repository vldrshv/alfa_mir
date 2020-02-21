package ru.alfabank.alfamir.profile.domain.usecase;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.base_elements.UseCase;
import ru.alfabank.alfamir.profile.data.source.repository.ProfileRepository;

public class SaveAboutMe extends UseCase<SaveAboutMe.RequestValues, SaveAboutMe.ResponseValue> {

    private ProfileRepository mProfileRepository;

    @Inject
    public SaveAboutMe(ProfileRepository profileRepository) {
        mProfileRepository = profileRepository;
    }

    @Override
    public Observable<ResponseValue> execute(RequestValues requestValues) {
        String aboutMeText = requestValues.getAboutMeText();
        return mProfileRepository.saveAboutMe(aboutMeText)
                .flatMap(json -> Observable.just(new ResponseValue()));
    }

    public static class RequestValues implements UseCase.RequestValues {
        private String mAboutMeText;
        public RequestValues(String aboutMeText) {
            mAboutMeText = aboutMeText;
        }
        String getAboutMeText() {
            return mAboutMeText;
        }
    }

    public static class ResponseValue implements UseCase.ResponseValue { }
}