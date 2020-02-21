package ru.alfabank.alfamir.profile.domain.mapper;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import ru.alfabank.alfamir.profile.data.dto.SubProfile;
import ru.alfabank.alfamir.profile.presentation.dto.FunctionalManager;

public class FunctionalManagerMapper implements Function<SubProfile, FunctionalManager> {

    @Inject
    public FunctionalManagerMapper(){}

    @Override
    public FunctionalManager apply(SubProfile chatRaw) {
        String login = chatRaw.getLogin();
        String name = chatRaw.getName();
        String picUrl = chatRaw.getPicUrl();
        String title = chatRaw.getTitle();
        FunctionalManager administrativeManager = new FunctionalManager.Builder()
                .login(login)
                .name(name)
                .picUrl(picUrl)
                .title(title)
                .build();
        return administrativeManager;
    }

}