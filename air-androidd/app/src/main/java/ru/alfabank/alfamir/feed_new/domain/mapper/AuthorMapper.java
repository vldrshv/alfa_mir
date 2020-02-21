package ru.alfabank.alfamir.feed_new.domain.mapper;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import ru.alfabank.alfamir.feed_new.data.dto.AuthorRaw;
import ru.alfabank.alfamir.feed_new.presentation.dto.Author;

public class AuthorMapper implements Function<AuthorRaw, Author> {

    @Inject
    public AuthorMapper(){}

    @Override
    public Author apply(AuthorRaw authorRaw) {
        String imageUrl = authorRaw.getImageUrl();
        String login = authorRaw.getLogin();
        String name = authorRaw.getName();
        String title = authorRaw.getTitle();
        Author author = new Author.Builder()
                .imageUrl(imageUrl)
                .login(login)
                .name(name)
                .title(title)
                .build();
        return author;
    }
}