package ru.alfabank.alfamir.alfa_tv.domain.mapper;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import ru.alfabank.alfamir.alfa_tv.data.dto.HostRaw;
import ru.alfabank.alfamir.alfa_tv.presentation.dto.Host;
import ru.alfabank.alfamir.utility.initials.InitialsProvider;

public class HostMapper implements Function<HostRaw, Host> {

    private InitialsProvider mInitialsProvider;

    @Inject
    HostMapper(InitialsProvider initialsProvider){
        mInitialsProvider = initialsProvider;
    }

    @Override
    public Host apply(HostRaw hostRaw) throws Exception {
        String login = hostRaw.getLogin();
        String name = hostRaw.getName();
        String initials = mInitialsProvider.formInitials(name);
        String picLink = hostRaw.getPicLink();
        String title = hostRaw.getTitle();

        Host host = new Host.Builder()
                .login(login)
                .name(name)
                .initials(initials)
                .picLink(picLink)
                .title(title)
                .build();
        return host;
    }
}
