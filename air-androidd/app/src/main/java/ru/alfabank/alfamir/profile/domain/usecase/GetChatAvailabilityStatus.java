package ru.alfabank.alfamir.profile.domain.usecase;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.base_elements.UseCase;
import ru.alfabank.alfamir.profile.data.source.repository.ProfileRepository;

public class GetChatAvailabilityStatus extends UseCase<GetChatAvailabilityStatus.RequestValues, GetChatAvailabilityStatus.ResponseValue> {

    private ProfileRepository mProfileRepository;

    @Inject
    public GetChatAvailabilityStatus(ProfileRepository profileRepository) {
        mProfileRepository = profileRepository;
    }

    @Override
    public Observable<ResponseValue> execute(RequestValues requestValues) {
        String responderLogin = requestValues.getResponderLogin();
        return mProfileRepository.getChatAvailabilityStatus(responderLogin)
                .flatMap(status -> Observable.just(new ResponseValue(status)));
    }

    public static class RequestValues implements UseCase.RequestValues {
        private String mResponderLogin;
        public RequestValues(String responderLogin) {
            mResponderLogin = responderLogin;
        }
        String getResponderLogin() {
            return mResponderLogin;
        }
    }

    public static class ResponseValue implements UseCase.ResponseValue {
        private int mChatAvailabilityStatus;
        ResponseValue(int chatAvailabilityStatus) {
            mChatAvailabilityStatus = chatAvailabilityStatus;
        }
        public int getChatAvailabilityStatus() {
            return mChatAvailabilityStatus;
        }
    }
}