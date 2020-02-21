package alfamir.alfabank.ru.air.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;

import ru.alfabank.alfamir.alfa_tv.data.dto.ShowRaw;
import ru.alfabank.alfamir.messenger.data.dto.MessageRaw;
import ru.alfabank.alfamir.messenger.data.dto.PollDataRaw;
import ru.alfabank.alfamir.messenger.data.dto.StatusRawTest;
import ru.alfabank.alfamir.messenger.data.serialization.PollDataAdapter;
import ru.alfabank.alfamir.profile.data.dto.ProfileRaw;
import ru.alfabank.alfamir.profile.data.dto.ProfileWrapper;

public class Serialization {


    public void testPollData(){
        String json = "[{\"Login\":\"MOSCOW\\\\U_M0WY5\",\"Type\":\"user\",\"Value\":{\"Login\":\"MOSCOW\\\\U_M0YFV\", \"Status\":\"offline\",\"ConnDate\":\"2018-09-05T08:28:25.206621Z\",\"FullName\":\"Субботин Олег Олегович\",\"JobTitle\":\"Ведущий разработчик\",\"ProfilPicURL\":\"http://alfa:80/my/user%20photos/profile%20pictures/u_m0yfv_lthumb.jpg;#300;#300\",\"PublicKey\":null},\"CreationDate\":\"2018-09-05T08:28:26.45\",\"Guid\":\"d95ac1c6-38c0-4c06-a0af-9e7939048465\"},{\"Login\":\"MOSCOW\\\\U_M0WY5\",\"Type\":\"message\",\"Value\":{\"USER_ID\":7869,\"ChatID\":\"0f8bf5f7-1c88-43ff-9b67-1fa4add25c16\",\"FromUser\":\"MOSCOW\\\\U_M0SU5\",\"ToUsers\":\"MOSCOW\\\\U_M0WY5\",\"Value\":\"Тест \",\"CreationDate\":\"2018-09-05T08:31:38.7736913Z\",\"CodeKey\":null,\"CodeVector\":null,\"Status\":null},\"CreationDate\":\"2018-09-05T08:31:38.79\",\"Guid\":\"88478828-943c-4c90-88cc-d536c4051ba2\"}]";
//        String json = "{\"ChatID\":\"724d8423-19d1-4e30-950d-c2e6cc7aebd6\",\"Type\":\"personal\",\"Users\":[{\"Login\":\"MOSCOW\\\\U_M0SU5\",\"Status\":\"offline\",\"ConnDate\":\"2018-09-05T20:02:13.493\",\"FullName\":\"Деревянко Андрей Александрович\",\"JobTitle\":\"Разработчик\",\"ProfilPicURL\":\"http://alfa:80/my/User%20Photos/Profile%20Pictures/MOSCOW_U_M0SU5_FSize.jpg;#1280;#1280\",\"PublicKey\":null},{\"Login\":\"MOSCOW\\\\U_M0WY5\",\"Status\":\"offline\",\"ConnDate\":\"2018-09-05T16:16:08.113\",\"FullName\":\"Авдуевский Михаил Сергеевич\",\"JobTitle\":\"Главный разработчик\",\"ProfilPicURL\":\"http://alfa:80/my/User%20Photos/Profile%20Pictures/MOSCOW_U_M0WY5_FSize.jpg;#1280;#1280\",\"PublicKey\":null}],\"Messages\":[{\"USER_ID\":7920,\"ChatID\":\"724d8423-19d1-4e30-950d-c2e6cc7aebd6\",\"FromUser\":\"MOSCOW\\\\U_M0WY5\",\"ToUsers\":\"MOSCOW\\\\U_M0SU5\",\"Value\":\"Ttd\",\"CreationDate\":\"2018-09-05T13:11:40.52\",\"CodeKey\":\"\",\"CodeVector\":\"\",\"Status\":[{\"ChatID\":\"724d8423-19d1-4e30-950d-c2e6cc7aebd6\",\"MessageID\":7920,\"Status\":\"sent\",\"UserLogin\":\"MOSCOW\\\\U_M0SU5\"}]},{\"USER_ID\":7921,\"ChatID\":\"724d8423-19d1-4e30-950d-c2e6cc7aebd6\",\"FromUser\":\"MOSCOW\\\\U_M0WY5\",\"ToUsers\":\"MOSCOW\\\\U_M0SU5\",\"Value\":\"Ttd\",\"CreationDate\":\"2018-09-05T13:11:48.083\",\"CodeKey\":\"\",\"CodeVector\":\"\",\"Status\":[{\"ChatID\":\"724d8423-19d1-4e30-950d-c2e6cc7aebd6\",\"MessageID\":7921,\"Status\":\"sent\",\"UserLogin\":\"MOSCOW\\\\U_M0SU5\"}]},{\"USER_ID\":7922,\"ChatID\":\"724d8423-19d1-4e30-950d-c2e6cc7aebd6\",\"FromUser\":\"MOSCOW\\\\U_M0WY5\",\"ToUsers\":\"MOSCOW\\\\U_M0SU5\",\"Value\":\"Thanks\",\"CreationDate\":\"2018-09-05T14:00:28.27\",\"CodeKey\":\"\",\"CodeVector\":\"\",\"Status\":[{\"ChatID\":\"724d8423-19d1-4e30-950d-c2e6cc7aebd6\",\"MessageID\":7922,\"Status\":\"sent\",\"UserLogin\":\"MOSCOW\\\\U_M0SU5\"}]}]}";
//        String json = "[{\"USER_ID\":7920,\"ChatID\":\"724d8423-19d1-4e30-950d-c2e6cc7aebd6\",\"FromUser\":\"MOSCOW\\\\U_M0WY5\",\"ToUsers\":\"MOSCOW\\\\U_M0SU5\",\"Value\":\"Ttd\",\"CreationDate\":\"2018-09-05T13:11:40.52\",\"CodeKey\":\"\",\"CodeVector\":\"\",\"Status\":[{\"ChatID\":\"724d8423-19d1-4e30-950d-c2e6cc7aebd6\",\"MessageID\":7920,\"Status\":\"sent\",\"UserLogin\":\"MOSCOW\\\\U_M0SU5\"}]},{\"USER_ID\":7921,\"ChatID\":\"724d8423-19d1-4e30-950d-c2e6cc7aebd6\",\"FromUser\":\"MOSCOW\\\\U_M0WY5\",\"ToUsers\":\"MOSCOW\\\\U_M0SU5\",\"Value\":\"Ttd\",\"CreationDate\":\"2018-09-05T13:11:48.083\",\"CodeKey\":\"\",\"CodeVector\":\"\",\"Status\":[{\"ChatID\":\"724d8423-19d1-4e30-950d-c2e6cc7aebd6\",\"MessageID\":7921,\"Status\":\"sent\",\"UserLogin\":\"MOSCOW\\\\U_M0SU5\"}]},{\"USER_ID\":7922,\"ChatID\":\"724d8423-19d1-4e30-950d-c2e6cc7aebd6\",\"FromUser\":\"MOSCOW\\\\U_M0WY5\",\"ToUsers\":\"MOSCOW\\\\U_M0SU5\",\"Value\":\"Thanks\",\"CreationDate\":\"2018-09-05T14:00:28.27\",\"CodeKey\":\"\",\"CodeVector\":\"\",\"Status\":[{\"ChatID\":\"724d8423-19d1-4e30-950d-c2e6cc7aebd6\",\"MessageID\":7922,\"Status\":\"sent\",\"UserLogin\":\"MOSCOW\\\\U_M0SU5\"}]}]";
        Gson gson = new GsonBuilder().registerTypeAdapter(PollDataRaw.class,
                new PollDataAdapter()).create();
        PollDataRaw[] pollDataRaw = gson.fromJson(json, PollDataRaw[].class);
        System.out.println("i have finished");
    }

//    @Test
    public void testMessages(){
        String json = "[{\"USER_ID\":7920,\"ChatID\":\"724d8423-19d1-4e30-950d-c2e6cc7aebd6\",\"FromUser\":\"MOSCOW\\\\U_M0WY5\",\"ToUsers\":\"MOSCOW\\\\U_M0SU5\",\"Value\":\"Ttd\",\"CreationDate\":\"2018-09-05T13:11:40.52\",\"CodeKey\":\"\",\"CodeVector\":\"\",\"Status\":[{\"ChatID\":\"724d8423-19d1-4e30-950d-c2e6cc7aebd6\",\"MessageID\":7920,\"Status\":\"sent\",\"UserLogin\":\"MOSCOW\\\\U_M0SU5\"}]},{\"USER_ID\":7921,\"ChatID\":\"724d8423-19d1-4e30-950d-c2e6cc7aebd6\",\"FromUser\":\"MOSCOW\\\\U_M0WY5\",\"ToUsers\":\"MOSCOW\\\\U_M0SU5\",\"Value\":\"Ttd\",\"CreationDate\":\"2018-09-05T13:11:48.083\",\"CodeKey\":\"\",\"CodeVector\":\"\",\"Status\":[{\"ChatID\":\"724d8423-19d1-4e30-950d-c2e6cc7aebd6\",\"MessageID\":7921,\"Status\":\"sent\",\"UserLogin\":\"MOSCOW\\\\U_M0SU5\"}]},{\"USER_ID\":7922,\"ChatID\":\"724d8423-19d1-4e30-950d-c2e6cc7aebd6\",\"FromUser\":\"MOSCOW\\\\U_M0WY5\",\"ToUsers\":\"MOSCOW\\\\U_M0SU5\",\"Value\":\"Thanks\",\"CreationDate\":\"2018-09-05T14:00:28.27\",\"CodeKey\":\"\",\"CodeVector\":\"\",\"Status\":[{\"ChatID\":\"724d8423-19d1-4e30-950d-c2e6cc7aebd6\",\"MessageID\":7922,\"Status\":\"sent\",\"UserLogin\":\"MOSCOW\\\\U_M0SU5\"}]}]";
        Gson gson = new Gson();
        MessageRaw[] messageRaw = gson.fromJson(json, MessageRaw[].class);
    }

    public void testStatus(){
        String json = "{\"ChatID\":\"705c7d70-fe58-41c5-b8ea-ae7711d405dc\",\"MessageID\":7941,\"Status\":\"sent\",\"UserLogin\":\"MOSCOW\\\\U_M0W6Q\",\"TimeStamp\":\"2018-09-06T13:20:49.667\"}";
        Gson gson = new Gson();
        StatusRawTest statusRaw = gson.fromJson(json, StatusRawTest.class);
    }


    public void testShowRaw(){
        String json = "{\"id\":5185,\"start\":\"11.09.2018 17:00:00\",\"end\":\"11.09.2018 20:00:00\",\"title\":\"РБК тест\",\"description\":\"тест\",\"author\":{\"id\":\"MOSCOW\\\\U_M0D2T\",\"fullname\":\"Федосеенков Виталий Юрьевич\",\"photobase64\":\"http://alfa:80/my/user%20photos/profile%20pictures/u_m0d2t_lthumb.jpg;#-1;#-1\",\"jobtitle\":\"Главный эксперт\"},\"room\":\"Для тестов\",\"passwordexist\":1,\"password\":\"550\",\"streaming\":1},{\"id\":2953,\"start\":\"12.09.2018 09:00:00\",\"end\":\"12.09.2018 10:30:00\",\"title\":\"Вебинар МСКБ_Уровень 1_Кредитные продукты\",\"description\":\"Вебинар МСКБ_Уровень 1_Кредитные продукты\",\"author\":{\"id\":\"REGIONS\\\\U_180DV\",\"fullname\":\"Лещинский Андрей Владимирович\",\"photobase64\":\"http://alfa:80/my/user%20photos/profile%20pictures/u_180dv_lthumb.jpg;#-1;#-1\",\"jobtitle\":\"Главный специалист по подбору и обучению\"},\"room\":\"Зал 1\",\"passwordexist\":1,\"password\":\"3429\",\"streaming\":0}";
        Gson gson = new Gson();
        ShowRaw showRaw = gson.fromJson(json, ShowRaw.class);
    }

    @Test
    public void testProfileRaw(){
        String json = "{\"userdata\":{\"fullname\":\"Плотников Павел Анатольевич\",\"accountname\":\"MOSCOW\\\\U_M0W6Q\",\"email\":\"PAPlotnikov@alfabank.ru\",\"manager\":null,\"functionalmanager\":{\"id\":\"MOSCOW\\\\U_P0116\",\"fullname\":\"Дьяченко Юлия Сергеевна\",\"photobase64\":null,\"jobtitle\":null},\"jobtitle\":\"Главный разработчик\",\"deptitle\":\"Управление технологий централизованных систем\",\"workphone\":\"(011) 2438\",\"mobilephone\":\"+7 (926) 234-78-25\",\"address\":\"115432 г. Москва, Проспект Андропова 18, корпус 6\",\"office\":\"НЦ-813-146\",\"fulladdress\":\"МОСКВА<br>ВЫНЕСЕННЫЕ ОФИСЫ<br>НЦ<br>НЦ-8-ЭТАЖ<br>НЦ-813<br>НЦ-813-146\",\"city\":\"Москва\",\"birthdate\":\"11 июля\",\"absenceinfo\":\"29.10.2018 0:00:00;18.11.2018 0:00:00;\",\"aboutme\":\"iOS-разработчик приложения Air\",\"skills\":\"SWIFT|iOS|IOS разработчик|ыпрыапр ыапр ыпар|Журал Forbes опубликовал очередной рейтинг 20 самых дорогих|равпрапр|ыапрыапрпаырыпа|ыоыроырпаопвроофравплоравфлплавф\",\"photobase64\":\"http://alfa:80/my/User%20Photos/Profile%20Pictures/MOSCOW_U_M0W6Q_FSize.jpg;#640;#640\",\"timezone\":\"0\",\"currentuserlike\":0,\"likecount\":10,\"phoneverified\":1,\"firstname\":\"Павел\",\"lastname\":\"Плотников\",\"middlename\":\"Анатольевич\",\"infavorite\":1}}";
        Gson gson = new Gson();
        ProfileWrapper profileRaw = gson.fromJson(json, ProfileWrapper.class);
        ProfileRaw profile = profileRaw.getProfileRaw();
    }

}
