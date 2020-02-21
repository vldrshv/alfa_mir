package ru.alfabank.alfamir.feed_new.domain.mapper;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import ru.alfabank.alfamir.feed_new.data.dto.UriElementRaw;
import ru.alfabank.alfamir.feed_new.presentation.dto.UriElement;

public class UriElementMapper implements Function<UriElementRaw, UriElement> {

    @Inject
    public UriElementMapper(){}

    @Override
    public UriElement apply(UriElementRaw messageRaw) {
        String id = messageRaw.getId();
        String referenceType = messageRaw.getRefferenceType();
        String uri = messageRaw.getValue();
        UriElement uriElement = new UriElement.Builder()
                .id(id)
                .uri(uri)
                .referenceType(referenceType)
                .build();
        return uriElement;
    }

}