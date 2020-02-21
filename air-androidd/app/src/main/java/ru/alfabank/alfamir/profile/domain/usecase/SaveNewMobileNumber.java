package ru.alfabank.alfamir.profile.domain.usecase;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.base_elements.UseCase;
import ru.alfabank.alfamir.profile.data.source.repository.ProfileRepository;

public class SaveNewMobileNumber extends UseCase<SaveNewMobileNumber.RequestValues, SaveNewMobileNumber.ResponseValue> {

    private ProfileRepository mProfileRepository;

    @Inject
    public SaveNewMobileNumber(ProfileRepository profileRepository) {
        mProfileRepository = profileRepository;
    }

    @Override
    public Observable<ResponseValue> execute(RequestValues requestValues) {
        String newNumber = requestValues.getNewNumber();
        return mProfileRepository.saveMobile(newNumber)
                .flatMap(json -> Observable.just(new ResponseValue()));
    }

    public static class RequestValues implements UseCase.RequestValues {
        private String mNewNumber;
        public RequestValues(String newNumber) {
            mNewNumber = newNumber;
        }
        public String getNewNumber() {
            return mNewNumber;
        }
    }

    public static class ResponseValue implements UseCase.ResponseValue { }
}