package ru.alfabank.alfamir.alfa_tv.domain.mapper;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import ru.alfabank.alfamir.alfa_tv.data.dto.ShowUriRaw;
import ru.alfabank.alfamir.alfa_tv.presentation.dto.ShowUri;

public class ShowUriMapper implements Function<ShowUriRaw, ShowUri> {

    @Inject
    ShowUriMapper(){ }

    @Override
    public ShowUri apply(ShowUriRaw showRaw) throws Exception {
        String url = showRaw.getUrl();
        String token = showRaw.getToken();
        String fullUrl = url+"?token="+token;
        ShowUri showUri = new ShowUri.Builder()
                .uri(fullUrl)
                .build();
        return showUri;
    }
}