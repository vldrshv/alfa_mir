package ru.alfabank.alfamir.notification.domain.mapper;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import ru.alfabank.alfamir.notification.data.dto.AuthorRaw;
import ru.alfabank.alfamir.notification.presentation.dto.Author;

public class AuthorMapper implements Function<AuthorRaw, Author> {

    @Inject
    AuthorMapper(){ }

    @Override
    public Author apply(AuthorRaw authorRaw) throws Exception {
        String imageUrl = authorRaw.getImageUrl();
        String login = authorRaw.getLogin();
        String name = authorRaw.getName();
        String title = authorRaw.getTitle();
        return new Author.Builder()
                .imageUrl(imageUrl)
                .login(login)
                .name(name)
                .title(title)
                .build();
    }
}