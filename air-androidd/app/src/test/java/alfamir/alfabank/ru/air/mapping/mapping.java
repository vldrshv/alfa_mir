package alfamir.alfabank.ru.air.mapping;

import com.google.gson.Gson;

import org.junit.Test;

import ru.alfabank.alfamir.messenger.data.dto.ChatRaw;
import ru.alfabank.alfamir.messenger.domain.mapper.ChatMapper;
import ru.alfabank.alfamir.messenger.domain.mapper.MessageMapper;
import ru.alfabank.alfamir.messenger.domain.mapper.StatusMapper;
import ru.alfabank.alfamir.messenger.domain.mapper.UserMapper;
import ru.alfabank.alfamir.messenger.presentation.dto.Chat;
import ru.alfabank.alfamir.utility.date_formatter.DateFormatter;
import ru.alfabank.alfamir.utility.date_formatter.DateFormatterImpl;

public class mapping {

    @Test
    public void mapping() throws Exception {
        DateFormatter dateFormatter = new DateFormatterImpl(); // TODO should remove public constructor
        StatusMapper statusMapper = new StatusMapper(dateFormatter);
        UserMapper userMapper = new UserMapper(); // TODO should remove public constructor
        MessageMapper messageMapper = new MessageMapper(dateFormatter, statusMapper, userMapper); // TODO should remove public constructor
        ChatMapper chatMapper = new ChatMapper(messageMapper, userMapper); // TODO should remove public constructor
        String json = "{\"ChatID\":\"724d8423-19d1-4e30-950d-c2e6cc7aebd6\",\"Type\":\"personal\",\"Users\":[{\"Login\":\"MOSCOW\\\\U_M0SU5\",\"Status\":\"offline\",\"ConnDate\":\"2018-09-05T20:02:13.493\",\"FullName\":\"Деревянко Андрей Александрович\",\"JobTitle\":\"Разработчик\",\"ProfilPicURL\":\"http://alfa:80/my/User%20Photos/Profile%20Pictures/MOSCOW_U_M0SU5_FSize.jpg;#1280;#1280\",\"PublicKey\":null},{\"Login\":\"MOSCOW\\\\U_M0WY5\",\"Status\":\"offline\",\"ConnDate\":\"2018-09-05T16:16:08.113\",\"FullName\":\"Авдуевский Михаил Сергеевич\",\"JobTitle\":\"Главный разработчик\",\"ProfilPicURL\":\"http://alfa:80/my/User%20Photos/Profile%20Pictures/MOSCOW_U_M0WY5_FSize.jpg;#1280;#1280\",\"PublicKey\":null}],\"Messages\":[{\"USER_ID\":7920,\"ChatID\":\"724d8423-19d1-4e30-950d-c2e6cc7aebd6\",\"FromUser\":\"MOSCOW\\\\U_M0WY5\",\"ToUsers\":\"MOSCOW\\\\U_M0SU5\",\"Value\":\"Ttd\",\"CreationDate\":\"2018-09-05T13:11:40.52\",\"CodeKey\":\"\",\"CodeVector\":\"\",\"Status\":[{\"ChatID\":\"724d8423-19d1-4e30-950d-c2e6cc7aebd6\",\"MessageID\":7920,\"Status\":\"sent\",\"UserLogin\":\"MOSCOW\\\\U_M0SU5\"}]},{\"USER_ID\":7921,\"ChatID\":\"724d8423-19d1-4e30-950d-c2e6cc7aebd6\",\"FromUser\":\"MOSCOW\\\\U_M0WY5\",\"ToUsers\":\"MOSCOW\\\\U_M0SU5\",\"Value\":\"Ttd\",\"CreationDate\":\"2018-09-05T13:11:48.083\",\"CodeKey\":\"\",\"CodeVector\":\"\",\"Status\":[{\"ChatID\":\"724d8423-19d1-4e30-950d-c2e6cc7aebd6\",\"MessageID\":7921,\"Status\":\"sent\",\"UserLogin\":\"MOSCOW\\\\U_M0SU5\"}]},{\"USER_ID\":7922,\"ChatID\":\"724d8423-19d1-4e30-950d-c2e6cc7aebd6\",\"FromUser\":\"MOSCOW\\\\U_M0WY5\",\"ToUsers\":\"MOSCOW\\\\U_M0SU5\",\"Value\":\"Thanks\",\"CreationDate\":\"2018-09-05T14:00:28.27\",\"CodeKey\":\"\",\"CodeVector\":\"\",\"Status\":[{\"ChatID\":\"724d8423-19d1-4e30-950d-c2e6cc7aebd6\",\"MessageID\":7922,\"Status\":\"sent\",\"UserLogin\":\"MOSCOW\\\\U_M0SU5\"}]}]}";
        Gson gson = new Gson();
        ChatRaw chatRaw = gson.fromJson(json, ChatRaw.class);
        Chat chat = chatMapper.apply(chatRaw);
    }
}
