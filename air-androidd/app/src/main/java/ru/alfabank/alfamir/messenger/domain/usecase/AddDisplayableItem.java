package ru.alfabank.alfamir.messenger.domain.usecase;

import ru.alfabank.alfamir.messenger.presentation.dto.DisplayableMessageItem;

import javax.inject.Inject;
import java.util.List;

import static ru.alfabank.alfamir.Constants.Messenger.DIRECTION_DOWN;
import static ru.alfabank.alfamir.Constants.Messenger.DIRECTION_UP;

public class AddDisplayableItem {

    @Inject
    AddDisplayableItem() {
    }

    public int execute(List<DisplayableMessageItem> newDisplayableMessageItemList, List<DisplayableMessageItem> displayableMessageItemList, int direction) {
        for (int i = 0; i < newDisplayableMessageItemList.size(); i++) {
            DisplayableMessageItem item = newDisplayableMessageItemList.get(i);
            if (direction == DIRECTION_UP) {
                displayableMessageItemList.add(item);
            } else if (direction == DIRECTION_DOWN) {
                displayableMessageItemList.add(0, item);
            }
        }
        return newDisplayableMessageItemList.size();
    }
}