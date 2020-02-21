package ru.alfabank.alfamir.alfa_tv.domain.usecase;

import android.text.format.DateUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.alfa_tv.presentation.dto.Show;
import ru.alfabank.alfamir.alfa_tv.presentation.dto.ShowListElement;
import ru.alfabank.alfamir.base_elements.UseCase;

public class GetPossibleCurrentShow extends UseCase<GetPossibleCurrentShow.RequestValues, GetPossibleCurrentShow.ResponseValue> {

    @Inject
    public GetPossibleCurrentShow(){}

    @Override
    public Observable<ResponseValue> execute(RequestValues requestValues) {
        Map<Integer, Integer> possibleCurrentShowsPosition = new HashMap<>();
        List<ShowListElement> showListElements = requestValues.getShowListElements();
        for (int i = 0; i < showListElements.size(); i++){
            ShowListElement showListElement = showListElements.get(i);
            if (showListElement instanceof Show){
                Show show = (Show) showListElement;
                long lStartTime = show.getLongStartDate();
                if (DateUtils.isToday(lStartTime)){
                    int showId = show.getId();
                    possibleCurrentShowsPosition.put(showId, i);
                }
            }
        }
        return Observable.just(new ResponseValue(possibleCurrentShowsPosition));
    }

    public static class RequestValues implements UseCase.RequestValues {
        List<ShowListElement> mShowListElements;
        public RequestValues(List<ShowListElement> showListElements){
            mShowListElements = showListElements;
        }

        public List<ShowListElement> getShowListElements() {
            return mShowListElements;
        }
    }

    public static class ResponseValue implements UseCase.ResponseValue {
        Map<Integer, Integer> mPossibleCurrentShowsPosition;

        ResponseValue(Map<Integer, Integer> possibleCurrentShowsPosition){
            mPossibleCurrentShowsPosition = possibleCurrentShowsPosition;
        }

        public Map<Integer, Integer> getPossibleCurrentShowsPosition() {
            return mPossibleCurrentShowsPosition;
        }
    }
}