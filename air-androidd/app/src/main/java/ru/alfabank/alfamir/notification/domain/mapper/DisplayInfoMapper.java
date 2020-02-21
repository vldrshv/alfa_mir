package ru.alfabank.alfamir.notification.domain.mapper;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import ru.alfabank.alfamir.notification.data.dto.DisplayInfoRaw;
import ru.alfabank.alfamir.notification.presentation.dto.DisplayInfo;

public class DisplayInfoMapper implements Function<DisplayInfoRaw, DisplayInfo> {

    @Inject
    DisplayInfoMapper(){ }

    @Override
    public DisplayInfo apply(DisplayInfoRaw displayInfoRaw) throws Exception {
        String content = displayInfoRaw.getContent();
        String type = displayInfoRaw.getType();
        return new DisplayInfo.Builder()
                .content(content)
                .type(type)
                .build();
    }
}