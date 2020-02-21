package ru.alfabank.alfamir.messenger.domain.mapper;

import io.reactivex.functions.Function;
import ru.alfabank.alfamir.messenger.data.dto.ChatRaw;
import ru.alfabank.alfamir.messenger.data.dto.MessageRaw;
import ru.alfabank.alfamir.messenger.data.dto.UserRaw;
import ru.alfabank.alfamir.messenger.presentation.dto.Chat;
import ru.alfabank.alfamir.messenger.presentation.dto.DisplayableMessageItem;
import ru.alfabank.alfamir.messenger.presentation.dto.Message;
import ru.alfabank.alfamir.messenger.presentation.dto.User;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class ChatMapper implements Function<ChatRaw, Chat> {

    private MessageMapper mMessageMapper;
    private UserMapper mUserMapper;

    @Inject
    public ChatMapper(MessageMapper messageMapper,
                      UserMapper userMapper) {
        mMessageMapper = messageMapper;
        mUserMapper = userMapper;
    }

    @Override
    public Chat apply(ChatRaw chatRaw) throws Exception {

        String chatId = chatRaw.getId();
        String chatType = chatRaw.getType();
        long firstUnreadMessageId = chatRaw.getFirstUnreadMessageId();
        long firstMessageId = chatRaw.getFirstMessageId();
        long lastMessageId = chatRaw.getLastMessageId();
        List<MessageRaw> messagesRaw = chatRaw.getMessages();
        List<UserRaw> userRaws = chatRaw.getUsers();
        List<DisplayableMessageItem> displayableMessageItemList = new ArrayList<>();
        List<User> users = new ArrayList<>();
        if (messagesRaw != null) {
            for (MessageRaw messageRaw : messagesRaw) {
                Message message = mMessageMapper.apply(messageRaw);
                displayableMessageItemList.add(message);
            }
        }
        if (userRaws != null) {
            for (UserRaw userRaw : userRaws) {
                User user = mUserMapper.apply(userRaw);
                users.add(user);
            }
        }
        return new Chat.Builder()
                .id(chatId)
                .type(chatType)
                .firstUnreadMessageId(firstUnreadMessageId)
                .firstMessageId(firstMessageId)
                .lastMessageId(lastMessageId)
                .displayableMessageItemList(displayableMessageItemList)
                .users(users)
                .build();
    }
}
