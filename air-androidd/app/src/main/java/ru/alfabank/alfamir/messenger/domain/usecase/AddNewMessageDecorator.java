package ru.alfabank.alfamir.messenger.domain.usecase;

import io.reactivex.Observable;
import ru.alfabank.alfamir.base_elements.UseCase;
import ru.alfabank.alfamir.messenger.presentation.dto.DisplayableMessageItem;
import ru.alfabank.alfamir.messenger.presentation.dto.Message;
import ru.alfabank.alfamir.messenger.presentation.dto.MessageNewDecorator;

import javax.inject.Inject;
import java.util.List;

public class AddNewMessageDecorator extends UseCase<AddNewMessageDecorator.RequestValues, AddNewMessageDecorator.ResponseValue> {

    @Inject
    AddNewMessageDecorator() {
    }

    @Override
    public Observable<ResponseValue> execute(RequestValues requestValues) {
        List<DisplayableMessageItem> itemList = requestValues.getDisplayableMessageItems();
        long firstUnreadMessageId = requestValues.getFirstUnreadMessageId();
        int lastNewItemPosition = -1;
        long lastMessageId = 0;
        boolean hasMessageNewDecorator = false;
        for (int i = 0; i < itemList.size(); i++) {
            DisplayableMessageItem item = itemList.get(i);
            if (item instanceof Message) {
                Message message = (Message) item;
                if (message.getId() == firstUnreadMessageId) {
                    lastNewItemPosition = i;
                    hasMessageNewDecorator = true;
                }
                lastMessageId = message.getId();
            }
        }
        if (lastNewItemPosition != -1) {
            MessageNewDecorator messageNewDecorator = new MessageNewDecorator.Builder().build();
            itemList.add(lastNewItemPosition + 1, messageNewDecorator);
        }
        return Observable.just(new ResponseValue(itemList, hasMessageNewDecorator, lastNewItemPosition, lastMessageId));
    }

    public static class RequestValues implements UseCase.RequestValues {
        private List<DisplayableMessageItem> mDisplayableMessageItems;
        private long mFirstUnreadMessageId;

        public RequestValues(List<DisplayableMessageItem> displayableMessageItems, long firstUnreadMessageId) {
            mDisplayableMessageItems = displayableMessageItems;
            mFirstUnreadMessageId = firstUnreadMessageId;
        }

        List<DisplayableMessageItem> getDisplayableMessageItems() {
            return mDisplayableMessageItems;
        }

        long getFirstUnreadMessageId() {
            return mFirstUnreadMessageId;
        }
    }

    public static class ResponseValue implements UseCase.ResponseValue {
        private List<DisplayableMessageItem> mDisplayableMessageItems;
        private boolean mHasMessageNewDecorator;
        private int mLastNewItemPosition;
        private long mLastMessageId;

        ResponseValue(List<DisplayableMessageItem> displayableMessageItems,
                      boolean hasMessageNewDecorator,
                      int lastNewItemPosition,
                      long lastMessageId) {
            mDisplayableMessageItems = displayableMessageItems;
            mHasMessageNewDecorator = hasMessageNewDecorator;
            mLastNewItemPosition = lastNewItemPosition;
            mLastMessageId = lastMessageId;
        }

        public List<DisplayableMessageItem> getDisplayableMessageItems() {
            return mDisplayableMessageItems;
        }

        public boolean isHasMessageNewDecorator() {
            return mHasMessageNewDecorator;
        }

        public int getLastNewItemPosition() {
            return mLastNewItemPosition;
        }

        public long getLastMessageId() {
            return mLastMessageId;
        }
    }
}