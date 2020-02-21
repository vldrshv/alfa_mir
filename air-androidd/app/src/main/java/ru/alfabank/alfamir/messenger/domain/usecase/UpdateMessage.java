package ru.alfabank.alfamir.messenger.domain.usecase;

import io.reactivex.Observable;
import ru.alfabank.alfamir.messenger.presentation.dto.DisplayableMessageItem;
import ru.alfabank.alfamir.messenger.presentation.dto.Message;

import javax.inject.Inject;
import java.util.List;

import static ru.alfabank.alfamir.Constants.Messenger.MESSAGE_TEXT;

public class UpdateMessage {

    @Inject
    UpdateMessage() {
    }

    public Observable<Boolean> execute(List<DisplayableMessageItem> displayableMessageItemList, Message newMessage) {
        long newMessageViewId = newMessage.getViewId();
        for (int i = 0; i < displayableMessageItemList.size(); i++) {
            DisplayableMessageItem displayableMessageItem = displayableMessageItemList.get(i);
            if (displayableMessageItem.getType() == MESSAGE_TEXT) {
                Message message = (Message) displayableMessageItem;
                long viewId = message.getViewId();
                if (viewId == newMessageViewId) {
                    displayableMessageItemList.set(i, newMessage);
                    break;
                }
            }
        }
        return Observable.just(true);
    }
}
