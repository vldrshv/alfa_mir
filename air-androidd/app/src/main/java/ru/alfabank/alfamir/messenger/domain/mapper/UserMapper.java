package ru.alfabank.alfamir.messenger.domain.mapper;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import ru.alfabank.alfamir.messenger.data.dto.UserRaw;
import ru.alfabank.alfamir.messenger.presentation.dto.User;

public class UserMapper implements Function<UserRaw, User> {

    @Inject
    public UserMapper(){}

    @Override
    public User apply(UserRaw messageRaw) throws Exception {
        String id = messageRaw.getId().toLowerCase();
        int onlineStatus = messageRaw.getOnlineStatus();
        String lastOnline = messageRaw.getLastOnline();
        String name = messageRaw.getName();
        String position = messageRaw.getPosition();
        String picLink = messageRaw.getPicLink();

        User user = new User.Builder()
                .id(id)
                .onlineStatus(onlineStatus)
                .lastOnline(lastOnline)
                .name(name)
                .position(position)
                .picLink(picLink)
                .build();
        return user;
    }

}
