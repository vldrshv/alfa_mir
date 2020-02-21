package ru.alfabank.alfamir.alfa_tv.domain.mapper;

import android.text.format.DateUtils;
import io.reactivex.functions.Function;
import ru.alfabank.alfamir.alfa_tv.presentation.dto.Show;
import ru.alfabank.alfamir.alfa_tv.presentation.dto.ShowListElement;
import ru.alfabank.alfamir.alfa_tv.presentation.dto.ShowSeparator;
import ru.alfabank.alfamir.utility.date_formatter.DateFormatter;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static ru.alfabank.alfamir.Constants.DateFormatter.DATE_PATTERN_2;
import static ru.alfabank.alfamir.Constants.Show.SHOW;
import static ru.alfabank.alfamir.Constants.Show.SHOW_CURRENT;

public class ShowListMapper implements Function<List<Show>, List<ShowListElement>> {

    private DateFormatter mDateFormatter;

    @Inject
    ShowListMapper(DateFormatter dateFormatter) {
        mDateFormatter = dateFormatter;
    }

    @Override
    public List<ShowListElement> apply(List<Show> showList) throws Exception {
        return sort(showList);
    }

    private List<ShowListElement> sort(List<Show> showsNotSorted) {
        Map<String, List<Show>> dateMap = new LinkedHashMap<>();
        long currentTimeAsMilliSec = new Date().getTime();
        for (Show show : showsNotSorted) {
            int isOnAir = show.getIsOnAir();
            switch (isOnAir) {
                case 0: {
                    long lStartTime = show.getLongStartDate();
                    long lEndTime = show.getLongEndDate();
                    String dateKey;
                    if (currentTimeAsMilliSec > lStartTime && currentTimeAsMilliSec < lEndTime) {
                        dateKey = "СЕЙЧАС";
                    } else if (DateUtils.isToday(lStartTime)) {
                        dateKey = "СЕГОДНЯ";
                    } else if (DateUtils.isToday(lStartTime - TimeUnit.DAYS.toMillis(1))) {
                        dateKey = "ЗАВТРА";
                    } else if (DateUtils.isToday(lStartTime - TimeUnit.DAYS.toMillis(2))) {
                        dateKey = "ПОСЛЕЗАВТРА";
                    } else {
                        dateKey = mDateFormatter.formatDate(lStartTime, DATE_PATTERN_2);
                    }
                    List<Show> showList = dateMap.get(dateKey);
                    if (showList == null) {
                        showList = new ArrayList<>();
                        dateMap.put(dateKey, showList);
                    }
                    showList.add(show);
                    break;
                }
                case 1: {
                    List<Show> showList = dateMap.get("СЕЙЧАС");
                    if (showList == null) {
                        showList = new ArrayList<>();
                        dateMap.put("СЕЙЧАС", showList);
                    }
                    showList.add(show);
                    break;
                }
            }
        }
        List<ShowListElement> showListElements = new ArrayList<>();
        List<Show> currentList = dateMap.get("СЕЙЧАС");
        if (currentList != null) {
            showListElements.add(new ShowSeparator("СЕЙЧАС"));
            for (Show show : currentList) {
                show.setViewType(SHOW_CURRENT);
                showListElements.add(show);
            }
            dateMap.remove("СЕЙЧАС");
        }
        Set<String> keys = dateMap.keySet();
        for (String key : keys) {
            List<Show> showList = dateMap.get(key);
            showListElements.add(new ShowSeparator(key));
            assert showList != null;
            for (Show show : showList) {
                show.setViewType(SHOW);
                showListElements.add(show);
            }
        }
        return showListElements;
    }
}
