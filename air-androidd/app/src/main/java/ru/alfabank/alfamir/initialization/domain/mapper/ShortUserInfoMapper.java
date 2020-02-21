package ru.alfabank.alfamir.initialization.domain.mapper;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import ru.alfabank.alfamir.initialization.data.dto.ShortUserInfoRaw;
import ru.alfabank.alfamir.initialization.presentation.dto.ShortUserInfo;

public class ShortUserInfoMapper implements Function<ShortUserInfoRaw, ShortUserInfo> {

    @Inject
    public ShortUserInfoMapper(){ }

    @Override
    public ShortUserInfo apply(ShortUserInfoRaw shortUserInfoRaw) throws Exception {
        String id = shortUserInfoRaw.getId();
        String name = shortUserInfoRaw.getName();
        String title = shortUserInfoRaw.getTitle();
        String encodedImage = shortUserInfoRaw.getEncodedImage();
        String city = shortUserInfoRaw.getCity();
        ShortUserInfo shortUserInfo = new ShortUserInfo.Builder()
                .id(id)
                .name(name)
                .title(title)
                .encodedImage(encodedImage)
                .city(city)
                .build();
        return shortUserInfo;
    }

}