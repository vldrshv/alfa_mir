package ru.alfabank.alfamir.main.home_fragment.domain.usecase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.base_elements.UseCase;
import ru.alfabank.alfamir.main.home_fragment.data.source.repository.TopNewsDataSource;
import ru.alfabank.alfamir.main.home_fragment.data.source.repository.TopNewsRepository;
import ru.alfabank.alfamir.main.home_fragment.domain.mapper.TopNewsMapper;
import ru.alfabank.alfamir.main.home_fragment.presentation.dto.TopNews;

public class GetTopNews extends UseCase<GetTopNews.RequestValues, GetTopNews.ResponseValue> {

    private TopNewsRepository mHomeRepository;
    private TopNewsMapper mTopNewsMapper;

    @Inject
    public GetTopNews(TopNewsRepository homeRepository,
               TopNewsMapper topNewsMapper) {
        mHomeRepository = homeRepository;
        mTopNewsMapper = topNewsMapper;
    }

    @Override
    public Observable<ResponseValue> execute(RequestValues requestValues) {
        int newsItemAmount = requestValues.getNewsItemAmount();
        boolean isCacheDirty = false;
        return mHomeRepository.getTopNews(new TopNewsDataSource.RequestValues(newsItemAmount, isCacheDirty))
                .flatMapIterable(topNewsRawList -> topNewsRawList)
                .map(mTopNewsMapper)
                .toList()
                .flatMapObservable(topNews -> Observable.just(new ResponseValue(topNews)));
    }

    public static class RequestValues implements UseCase.RequestValues {
        private int mNewsItemAmount;
        public RequestValues(int newsItemAmount){
            mNewsItemAmount = newsItemAmount;
        }
        int getNewsItemAmount() {
            return mNewsItemAmount;
        }
    }

    public static class ResponseValue implements UseCase.ResponseValue {
        private List<TopNews> mTopNews;
        public ResponseValue(List<TopNews> topNews){
            mTopNews = topNews;
        }
        public List<TopNews> getTopNews() {
            return mTopNews;
        }
    }
}