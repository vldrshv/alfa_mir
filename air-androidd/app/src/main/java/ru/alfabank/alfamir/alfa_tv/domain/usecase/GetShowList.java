package ru.alfabank.alfamir.alfa_tv.domain.usecase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.alfa_tv.data.source.repository.password.PasswordRepository;
import ru.alfabank.alfamir.alfa_tv.data.source.repository.show.ShowDataSource;
import ru.alfabank.alfamir.alfa_tv.data.source.repository.show.ShowRepository;
import ru.alfabank.alfamir.alfa_tv.domain.mapper.ShowListMapper;
import ru.alfabank.alfamir.alfa_tv.domain.mapper.ShowMapper;
import ru.alfabank.alfamir.alfa_tv.presentation.dto.Show;
import ru.alfabank.alfamir.alfa_tv.presentation.dto.ShowListElement;
import ru.alfabank.alfamir.base_elements.UseCase;

public class GetShowList extends UseCase <GetShowList.RequestValues, GetShowList.ResponseValue> {

    private ShowRepository mShowRepository;
    private PasswordRepository mPasswordRepository;
    private ShowMapper mShowMapper;
    private ShowListMapper mShowListMapper;

    @Inject
    public GetShowList(ShowRepository alfaTvRepository,
                       PasswordRepository passwordRepository,
                       ShowMapper showMapper,
                       ShowListMapper showListMapper){
        mShowRepository = alfaTvRepository;
        mPasswordRepository = passwordRepository;
        mShowMapper = showMapper;
        mShowListMapper = showListMapper;
    }

    @Override
    public Observable<ResponseValue> execute(RequestValues requestValues) {
        boolean isCacheDirty = requestValues.getIsCacheDirty();
        return mShowRepository.getShowList(new ShowDataSource.RequestValues(isCacheDirty))
                .flatMapIterable(list -> list)
                .map(mShowMapper)
                .toList()
                .flatMapObservable(showList -> mPasswordRepository.getShowIdWithSavedPasswords().flatMap(ids ->{
                    for (Integer id : ids){
                        for (Show show : showList){
                            if (show.getId() == id){
                                show.setIsPasswordEntered(true);
                                break;
                            }
                        }
                    }
                    return Observable.just(showList);
                }))
                .map(mShowListMapper)
                .flatMap(showListElements -> Observable.just(new ResponseValue(showListElements)));
    }

    public static class RequestValues implements UseCase.RequestValues {
        private boolean mIsCacheDirty;
        public RequestValues(boolean isCacheDirty){
            mIsCacheDirty = isCacheDirty;
        }
        public boolean getIsCacheDirty() {
            return mIsCacheDirty;
        }
    }

    public static class ResponseValue implements UseCase.ResponseValue {
        private List<ShowListElement> mShowListElements;

        public ResponseValue(List<ShowListElement> showListElements){
            mShowListElements = showListElements;
        }

        public List<ShowListElement> getShowListElements() {
            return mShowListElements;
        }
    }
}
