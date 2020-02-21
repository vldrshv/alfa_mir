package ru.alfabank.alfamir.messenger.data.source.repository;

import io.reactivex.subjects.PublishSubject;
import ru.alfabank.alfamir.messenger.data.dto.MessageRaw;
import ru.alfabank.alfamir.messenger.data.dto.StatusRaw;
import ru.alfabank.alfamir.messenger.data.dto.UserRaw;

public interface PollDataDataSource {

    PublishSubject<MessageRaw> subscribeForMessage();

    PublishSubject<UserRaw> subscribeForUser();

    PublishSubject<StatusRaw> subscribeForStatus();

}
