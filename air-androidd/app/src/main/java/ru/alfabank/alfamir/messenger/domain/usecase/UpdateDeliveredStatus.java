package ru.alfabank.alfamir.messenger.domain.usecase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.messenger.presentation.dto.DisplayableMessageItem;
import ru.alfabank.alfamir.messenger.presentation.dto.Message;
import ru.alfabank.alfamir.messenger.presentation.dto.Status;

import static ru.alfabank.alfamir.Constants.Messenger.MESSAGE_TEXT;

public class UpdateDeliveredStatus {

    @Inject
    UpdateDeliveredStatus() {
    }

    public Observable<List<Integer>> execute(List<DisplayableMessageItem> displayableMessageItemList, List<Status> statuses) {

        List<Integer> updatedPositions = new ArrayList<>();
        for (Status status : statuses) {
            long statusMsgId = status.getMsgId();
            for (int i = 0; i < displayableMessageItemList.size(); i++) {
                DisplayableMessageItem displayableMessageItem = displayableMessageItemList.get(i);
                if (displayableMessageItem.getType() == MESSAGE_TEXT) {
                    Message msg = (Message) displayableMessageItem;
                    long msgId = msg.getId();
                    if (msgId == statusMsgId) {
                        List<Status> statusList = msg.getStatusList();
                        statusList.add(status);
                        if (status.getState() > msg.getCurrentStatus()) {
                            msg.setCurrentStatus(status.getState());
                        }
                        updatedPositions.add(i);
                    }
                }
            }
        }
        return Observable.just(updatedPositions);
    }
}