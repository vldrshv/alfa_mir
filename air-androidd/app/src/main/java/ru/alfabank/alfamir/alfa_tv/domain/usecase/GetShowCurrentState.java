package ru.alfabank.alfamir.alfa_tv.domain.usecase;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.alfa_tv.data.source.repository.show.ShowRepository;
import ru.alfabank.alfamir.base_elements.UseCase;

public class GetShowCurrentState extends UseCase<GetShowCurrentState.RequestValues, GetShowCurrentState.ResponseValue> {

    private ShowRepository mShowRepository;

    @Inject
    public GetShowCurrentState(ShowRepository showTvRepository){
        mShowRepository = showTvRepository;
    }

    @Override
    public Observable<ResponseValue> execute(RequestValues requestValues) {
        int showId = requestValues.getShowId();
        return mShowRepository.getCurrentVideoState(showId)
                .flatMap(aBoolean -> {
                    if(aBoolean){
                        return Observable.just(new ResponseValue(1));
                    } else {
                        return Observable.just(new ResponseValue(0));
                    }
                });
    }

    public static class RequestValues implements UseCase.RequestValues {
        private int mShowId;

        public RequestValues(int showId){
            mShowId = showId;
        }

        public int getShowId() {
            return mShowId;
        }
    }

    public static class ResponseValue implements UseCase.ResponseValue {
        private int mIsOnAir;
        public ResponseValue(int isOnAir){
            mIsOnAir = isOnAir;
        }

        public int isIsOnAir() {
            return mIsOnAir;
        }
    }
}