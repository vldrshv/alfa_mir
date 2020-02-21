package ru.alfabank.alfamir.favorites.domain.mapper;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import ru.alfabank.alfamir.favorites.data.dto.FavoriteProfileRaw;
import ru.alfabank.alfamir.favorites.presentation.dto.FavoriteProfile;

public class FavoriteProfileMapper implements Function<FavoriteProfileRaw, FavoriteProfile> {

    @Inject
    public FavoriteProfileMapper(){ }

    @Override
    public FavoriteProfile apply(FavoriteProfileRaw favoriteProfileRaw) throws Exception {
        String email = favoriteProfileRaw.getEmail();
        String imageUrl = favoriteProfileRaw.getImageUrl();
        String login = favoriteProfileRaw.getLogin();
        String name = favoriteProfileRaw.getName();
        String title = favoriteProfileRaw.getTitle();
        String workPhone = favoriteProfileRaw.getWorkPhone();
        FavoriteProfile favoriteProfile = new FavoriteProfile.Builder()
                .email(email)
                .imageUrl(imageUrl)
                .login(login)
                .name(name)
                .title(title)
                .workPhone(workPhone)
                .build();
        return favoriteProfile;
    }
}