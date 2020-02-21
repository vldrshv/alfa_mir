package ru.alfabank.alfamir.alfa_tv.domain.usecase;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.alfa_tv.data.source.repository.password.PasswordRepository;
import ru.alfabank.alfamir.alfa_tv.data.source.repository.show.ShowRepository;
import ru.alfabank.alfamir.alfa_tv.domain.mapper.ShowMapper;
import ru.alfabank.alfamir.alfa_tv.presentation.dto.Show;
import ru.alfabank.alfamir.base_elements.UseCase;

public class GetShow extends UseCase<GetShow.RequestValues, GetShow.ResponseValue> {

    private ShowRepository mShowRepository;
    private PasswordRepository mPasswordRepository;
    private ShowMapper mShowMapper;

    @Inject
    public GetShow(ShowRepository alfaTvRepository,
                   PasswordRepository passwordRepository,
                   ShowMapper showMapper){
        mShowRepository = alfaTvRepository;
        mPasswordRepository = passwordRepository;
        mShowMapper = showMapper;
    }

    @Override
    public Observable<ResponseValue> execute(RequestValues requestValues) {
        int showId = requestValues.getShowId();
        return mShowRepository.getShow(showId)
                .map(mShowMapper)
                .flatMap(show -> mPasswordRepository.ifPasswordIsSaved(showId)
                .flatMap(isSaved -> {
                    if(isSaved) show.setIsPasswordEntered(true);
                    return Observable.just(new ResponseValue(show));
                }));
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
        private Show mShow;

        public ResponseValue(Show show){
            mShow = show;
        }

        public Show getShow() {
            return mShow;
        }
    }
}
