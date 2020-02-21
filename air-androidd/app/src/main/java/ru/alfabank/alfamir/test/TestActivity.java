package ru.alfabank.alfamir.test;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.text.format.DateUtils;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.base_elements.BaseActivity;
import ru.alfabank.alfamir.data.source.remote.api.WebService;
import ru.alfabank.alfamir.messenger.domain.mapper.ChatMapper;
import ru.alfabank.alfamir.messenger.domain.mapper.MessageMapper;
import ru.alfabank.alfamir.messenger.domain.mapper.StatusMapper;
import ru.alfabank.alfamir.messenger.domain.mapper.UserMapper;
import ru.alfabank.alfamir.messenger.presentation.dto.DisplayableMessageItem;
import ru.alfabank.alfamir.messenger.presentation.dto.Message;
import ru.alfabank.alfamir.messenger.presentation.dto.MessageTimeBubble;
import ru.alfabank.alfamir.utility.date_formatter.DateFormatterImpl;

import static ru.alfabank.alfamir.Constants.DateFormat.DATE_PATTERN_4;
import static ru.alfabank.alfamir.Constants.DateFormatter.DATE_PATTERN_10;
import static ru.alfabank.alfamir.Constants.Messenger.MESSAGE_TEXT;
import static ru.alfabank.alfamir.Constants.UI.FRAGMENT_TO_BACKSTACK;

public class TestActivity extends BaseActivity {

    private ChatMapper mChatMapper;
    private DateFormatterImpl mDateFormatter;
    private int oldSize = 0;
    private int newSize = 0;

    @BindView(R.id.text_input_ed_input) EditText editText;
    @Inject WebService webService;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        ButterKnife.bind(this);

        Button button = findViewById(R.id.buton_test);
        button.setOnClickListener(view -> {
            getDashData();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void getDashData(){
        mCompositeDisposable.add(
                webService.requestX("{'type':'surveys','subtype':'survey','action':'getresulttable','id':'48'}")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            Gson gson = new Gson();
                            Stupid stupid = gson.fromJson(response, Stupid.class);
                            String text = stupid.getText();
                            editText.setText(text);
                        }));
    }

    public void test() {
        Intent intent = new Intent(this, TestActivity2.class);
        intent.putExtra("fuck_you", "omg_omg");
        intent.putExtra("wtf", "hahaha");
        intent.putExtra(FRAGMENT_TO_BACKSTACK, false);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "show_messenger")
                .setSmallIcon(R.drawable.ic_app_small)
                .setContentTitle("title")
                .setContentText("message")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        mBuilder.build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, mBuilder.build());
    }

    private List<DisplayableMessageItem> sort(List<DisplayableMessageItem> unsortedItems){
        List<DisplayableMessageItem> itemsNotSorted = unsortedItems;
        List<DisplayableMessageItem> itemsSorted = new ArrayList<>();

        for (int i = 0; i < itemsNotSorted.size(); i++){
            DisplayableMessageItem item = itemsNotSorted.get(i);
            if(item.getType() == MESSAGE_TEXT){
                itemsSorted.add(item);
                if (i == itemsNotSorted.size()-1) break;

                DisplayableMessageItem nextItem = itemsNotSorted.get(i + 1);
                Message message = (Message) item;
                Message nextMessage = (Message) nextItem;
                long lDate = message.getLDate();
                long newLDate = nextMessage.getLDate();
                String date = "";
                if(DateUtils.isToday(lDate)){
                    date = "Сегодня";
                } else if (DateUtils.isToday(lDate + TimeUnit.DAYS.toMillis(1))) {
                    date = "Вчера";
                } else {
                    date = mDateFormatter.formatDate(lDate, DATE_PATTERN_10, DATE_PATTERN_4);
                }
                String newDate = "";
                if(DateUtils.isToday(newLDate)){
                    newDate = "Сегодня";
                } else if (DateUtils.isToday(newLDate + TimeUnit.DAYS.toMillis(1))) {
                    newDate = "Вчера";
                } else {
                    newDate = mDateFormatter.formatDate(newLDate, DATE_PATTERN_10, DATE_PATTERN_4);
                }

                if(!date.equals(newDate)){
                    itemsSorted.add(new MessageTimeBubble.Builder()
                            .date(date)
                            .build());
                }
            }
        }

        return itemsSorted;
    }

    private void initialisation(){
        mDateFormatter = new DateFormatterImpl();
        StatusMapper statusMapper = new StatusMapper(mDateFormatter);
        UserMapper userMapper = new UserMapper();
        MessageMapper messageMapper = new MessageMapper(mDateFormatter, statusMapper, userMapper);
        mChatMapper = new ChatMapper(messageMapper, userMapper);
    }

    public String getJson(){

        return "{\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"FirstMessageID\":9918,\"FirstUnreadMessageID\":10048,\"LastMessageID\":10072,\"Messages\":[{\"Attachment\":[],\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"CodeKey\":\"\",\"CodeVector\":\"\",\"CreationDate\":\"18.10.2018 11:36:02.313\",\"FromUser\":{\"ConnDate\":\"18.10.2018 11:44:15.033\",\"FullName\":\"Субботин Олег Олегович\",\"JobTitle\":\"Ведущий разработчик\",\"Login\":\"MOSCOW\\\\U_M0YFV\",\"ProfilPicURL\":\"http://alfa:80/my/User%20Photos/Profile%20Pictures/MOSCOW_U_M0YFV_FSize.jpg;#750;#420\",\"PublicKey\":null,\"Status\":1},\"USER_ID\":10048,\"Status\":[{\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"MessageID\":10048,\"Status\":\"sent\",\"TimeStamp\":\"18.10.2018 11:36:02.377\",\"UserLogin\":\"MOSCOW\\\\U_M0WY5\"},{\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"MessageID\":10048,\"Status\":\"delivered\",\"TimeStamp\":\"18.10.2018 11:36:34.720\",\"UserLogin\":\"MOSCOW\\\\U_M0YFV\"}],\"ToUsers\":\"MOSCOW\\\\U_M0WY5\",\"Value\":\"1\"},{\"Attachment\":[],\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"CodeKey\":\"\",\"CodeVector\":\"\",\"CreationDate\":\"18.10.2018 11:36:03.487\",\"FromUser\":{\"ConnDate\":\"18.10.2018 11:44:15.033\",\"FullName\":\"Субботин Олег Олегович\",\"JobTitle\":\"Ведущий разработчик\",\"Login\":\"MOSCOW\\\\U_M0YFV\",\"ProfilPicURL\":\"http://alfa:80/my/User%20Photos/Profile%20Pictures/MOSCOW_U_M0YFV_FSize.jpg;#750;#420\",\"PublicKey\":null,\"Status\":1},\"USER_ID\":10049,\"Status\":[{\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"MessageID\":10049,\"Status\":\"sent\",\"TimeStamp\":\"18.10.2018 11:36:03.533\",\"UserLogin\":\"MOSCOW\\\\U_M0WY5\"},{\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"MessageID\":10049,\"Status\":\"delivered\",\"TimeStamp\":\"18.10.2018 11:36:34.767\",\"UserLogin\":\"MOSCOW\\\\U_M0YFV\"}],\"ToUsers\":\"MOSCOW\\\\U_M0WY5\",\"Value\":\"2\"},{\"Attachment\":[],\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"CodeKey\":\"\",\"CodeVector\":\"\",\"CreationDate\":\"17.10.2018 11:36:04.173\",\"FromUser\":{\"ConnDate\":\"18.10.2018 11:44:15.033\",\"FullName\":\"Субботин Олег Олегович\",\"JobTitle\":\"Ведущий разработчик\",\"Login\":\"MOSCOW\\\\U_M0YFV\",\"ProfilPicURL\":\"http://alfa:80/my/User%20Photos/Profile%20Pictures/MOSCOW_U_M0YFV_FSize.jpg;#750;#420\",\"PublicKey\":null,\"Status\":1},\"USER_ID\":10050,\"Status\":[{\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"MessageID\":10050,\"Status\":\"sent\",\"TimeStamp\":\"18.10.2018 11:36:04.207\",\"UserLogin\":\"MOSCOW\\\\U_M0WY5\"},{\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"MessageID\":10050,\"Status\":\"delivered\",\"TimeStamp\":\"18.10.2018 11:36:34.783\",\"UserLogin\":\"MOSCOW\\\\U_M0YFV\"}],\"ToUsers\":\"MOSCOW\\\\U_M0WY5\",\"Value\":\"3\"},{\"Attachment\":[],\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"CodeKey\":\"\",\"CodeVector\":\"\",\"CreationDate\":\"17.10.2018 11:36:04.737\",\"FromUser\":{\"ConnDate\":\"18.10.2018 11:44:15.033\",\"FullName\":\"Субботин Олег Олегович\",\"JobTitle\":\"Ведущий разработчик\",\"Login\":\"MOSCOW\\\\U_M0YFV\",\"ProfilPicURL\":\"http://alfa:80/my/User%20Photos/Profile%20Pictures/MOSCOW_U_M0YFV_FSize.jpg;#750;#420\",\"PublicKey\":null,\"Status\":1},\"USER_ID\":10051,\"Status\":[{\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"MessageID\":10051,\"Status\":\"sent\",\"TimeStamp\":\"18.10.2018 11:36:04.753\",\"UserLogin\":\"MOSCOW\\\\U_M0WY5\"},{\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"MessageID\":10051,\"Status\":\"delivered\",\"TimeStamp\":\"18.10.2018 11:36:34.813\",\"UserLogin\":\"MOSCOW\\\\U_M0YFV\"}],\"ToUsers\":\"MOSCOW\\\\U_M0WY5\",\"Value\":\"4\"},{\"Attachment\":[],\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"CodeKey\":\"\",\"CodeVector\":\"\",\"CreationDate\":\"17.10.2018 11:36:05.190\",\"FromUser\":{\"ConnDate\":\"18.10.2018 11:44:15.033\",\"FullName\":\"Субботин Олег Олегович\",\"JobTitle\":\"Ведущий разработчик\",\"Login\":\"MOSCOW\\\\U_M0YFV\",\"ProfilPicURL\":\"http://alfa:80/my/User%20Photos/Profile%20Pictures/MOSCOW_U_M0YFV_FSize.jpg;#750;#420\",\"PublicKey\":null,\"Status\":1},\"USER_ID\":10052,\"Status\":[{\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"MessageID\":10052,\"Status\":\"sent\",\"TimeStamp\":\"18.10.2018 11:36:05.220\",\"UserLogin\":\"MOSCOW\\\\U_M0WY5\"},{\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"MessageID\":10052,\"Status\":\"delivered\",\"TimeStamp\":\"18.10.2018 11:36:34.860\",\"UserLogin\":\"MOSCOW\\\\U_M0YFV\"}],\"ToUsers\":\"MOSCOW\\\\U_M0WY5\",\"Value\":\"5\"},{\"Attachment\":[],\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"CodeKey\":\"\",\"CodeVector\":\"\",\"CreationDate\":\"16.10.2018 11:36:05.580\",\"FromUser\":{\"ConnDate\":\"18.10.2018 11:44:15.033\",\"FullName\":\"Субботин Олег Олегович\",\"JobTitle\":\"Ведущий разработчик\",\"Login\":\"MOSCOW\\\\U_M0YFV\",\"ProfilPicURL\":\"http://alfa:80/my/User%20Photos/Profile%20Pictures/MOSCOW_U_M0YFV_FSize.jpg;#750;#420\",\"PublicKey\":null,\"Status\":1},\"USER_ID\":10053,\"Status\":[{\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"MessageID\":10053,\"Status\":\"sent\",\"TimeStamp\":\"18.10.2018 11:36:05.610\",\"UserLogin\":\"MOSCOW\\\\U_M0WY5\"},{\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"MessageID\":10053,\"Status\":\"delivered\",\"TimeStamp\":\"18.10.2018 11:36:34.877\",\"UserLogin\":\"MOSCOW\\\\U_M0YFV\"}],\"ToUsers\":\"MOSCOW\\\\U_M0WY5\",\"Value\":\"6\"},{\"Attachment\":[],\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"CodeKey\":\"\",\"CodeVector\":\"\",\"CreationDate\":\"16.10.2018 11:36:06.110\",\"FromUser\":{\"ConnDate\":\"18.10.2018 11:44:15.033\",\"FullName\":\"Субботин Олег Олегович\",\"JobTitle\":\"Ведущий разработчик\",\"Login\":\"MOSCOW\\\\U_M0YFV\",\"ProfilPicURL\":\"http://alfa:80/my/User%20Photos/Profile%20Pictures/MOSCOW_U_M0YFV_FSize.jpg;#750;#420\",\"PublicKey\":null,\"Status\":1},\"USER_ID\":10054,\"Status\":[{\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"MessageID\":10054,\"Status\":\"sent\",\"TimeStamp\":\"18.10.2018 11:36:06.143\",\"UserLogin\":\"MOSCOW\\\\U_M0WY5\"},{\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"MessageID\":10054,\"Status\":\"delivered\",\"TimeStamp\":\"18.10.2018 11:36:34.923\",\"UserLogin\":\"MOSCOW\\\\U_M0YFV\"}],\"ToUsers\":\"MOSCOW\\\\U_M0WY5\",\"Value\":\"7\"},{\"Attachment\":[],\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"CodeKey\":\"\",\"CodeVector\":\"\",\"CreationDate\":\"16.10.2018 11:36:06.423\",\"FromUser\":{\"ConnDate\":\"18.10.2018 11:44:15.033\",\"FullName\":\"Субботин Олег Олегович\",\"JobTitle\":\"Ведущий разработчик\",\"Login\":\"MOSCOW\\\\U_M0YFV\",\"ProfilPicURL\":\"http://alfa:80/my/User%20Photos/Profile%20Pictures/MOSCOW_U_M0YFV_FSize.jpg;#750;#420\",\"PublicKey\":null,\"Status\":1},\"USER_ID\":10055,\"Status\":[{\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"MessageID\":10055,\"Status\":\"sent\",\"TimeStamp\":\"18.10.2018 11:36:06.457\",\"UserLogin\":\"MOSCOW\\\\U_M0WY5\"},{\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"MessageID\":10055,\"Status\":\"delivered\",\"TimeStamp\":\"18.10.2018 11:36:34.957\",\"UserLogin\":\"MOSCOW\\\\U_M0YFV\"}],\"ToUsers\":\"MOSCOW\\\\U_M0WY5\",\"Value\":\"8\"},{\"Attachment\":[],\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"CodeKey\":\"\",\"CodeVector\":\"\",\"CreationDate\":\"15.10.2018 11:36:06.720\",\"FromUser\":{\"ConnDate\":\"18.10.2018 11:44:15.033\",\"FullName\":\"Субботин Олег Олегович\",\"JobTitle\":\"Ведущий разработчик\",\"Login\":\"MOSCOW\\\\U_M0YFV\",\"ProfilPicURL\":\"http://alfa:80/my/User%20Photos/Profile%20Pictures/MOSCOW_U_M0YFV_FSize.jpg;#750;#420\",\"PublicKey\":null,\"Status\":1},\"USER_ID\":10056,\"Status\":[{\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"MessageID\":10056,\"Status\":\"sent\",\"TimeStamp\":\"18.10.2018 11:36:06.753\",\"UserLogin\":\"MOSCOW\\\\U_M0WY5\"},{\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"MessageID\":10056,\"Status\":\"delivered\",\"TimeStamp\":\"18.10.2018 11:36:34.987\",\"UserLogin\":\"MOSCOW\\\\U_M0YFV\"}],\"ToUsers\":\"MOSCOW\\\\U_M0WY5\",\"Value\":\"9\"},{\"Attachment\":[],\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"CodeKey\":\"\",\"CodeVector\":\"\",\"CreationDate\":\"14.10.2018 11:36:07.550\",\"FromUser\":{\"ConnDate\":\"18.10.2018 11:44:15.033\",\"FullName\":\"Субботин Олег Олегович\",\"JobTitle\":\"Ведущий разработчик\",\"Login\":\"MOSCOW\\\\U_M0YFV\",\"ProfilPicURL\":\"http://alfa:80/my/User%20Photos/Profile%20Pictures/MOSCOW_U_M0YFV_FSize.jpg;#750;#420\",\"PublicKey\":null,\"Status\":1},\"USER_ID\":10057,\"Status\":[{\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"MessageID\":10057,\"Status\":\"sent\",\"TimeStamp\":\"18.10.2018 11:36:07.580\",\"UserLogin\":\"MOSCOW\\\\U_M0WY5\"},{\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"MessageID\":10057,\"Status\":\"delivered\",\"TimeStamp\":\"18.10.2018 11:36:35.017\",\"UserLogin\":\"MOSCOW\\\\U_M0YFV\"}],\"ToUsers\":\"MOSCOW\\\\U_M0WY5\",\"Value\":\"10 \"},{\"Attachment\":[],\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"CodeKey\":\"\",\"CodeVector\":\"\",\"CreationDate\":\"13.10.2018 11:36:10.597\",\"FromUser\":{\"ConnDate\":\"18.10.2018 11:44:15.033\",\"FullName\":\"Субботин Олег Олегович\",\"JobTitle\":\"Ведущий разработчик\",\"Login\":\"MOSCOW\\\\U_M0YFV\",\"ProfilPicURL\":\"http://alfa:80/my/User%20Photos/Profile%20Pictures/MOSCOW_U_M0YFV_FSize.jpg;#750;#420\",\"PublicKey\":null,\"Status\":1},\"USER_ID\":10058,\"Status\":[{\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"MessageID\":10058,\"Status\":\"sent\",\"TimeStamp\":\"18.10.2018 11:36:10.627\",\"UserLogin\":\"MOSCOW\\\\U_M0WY5\"},{\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"MessageID\":10058,\"Status\":\"delivered\",\"TimeStamp\":\"18.10.2018 11:36:35.050\",\"UserLogin\":\"MOSCOW\\\\U_M0YFV\"}],\"ToUsers\":\"MOSCOW\\\\U_M0WY5\",\"Value\":\"11\"},{\"Attachment\":[],\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"CodeKey\":\"\",\"CodeVector\":\"\",\"CreationDate\":\"13.10.2018 11:36:11.550\",\"FromUser\":{\"ConnDate\":\"18.10.2018 11:44:15.033\",\"FullName\":\"Субботин Олег Олегович\",\"JobTitle\":\"Ведущий разработчик\",\"Login\":\"MOSCOW\\\\U_M0YFV\",\"ProfilPicURL\":\"http://alfa:80/my/User%20Photos/Profile%20Pictures/MOSCOW_U_M0YFV_FSize.jpg;#750;#420\",\"PublicKey\":null,\"Status\":1},\"USER_ID\":10059,\"Status\":[{\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"MessageID\":10059,\"Status\":\"sent\",\"TimeStamp\":\"18.10.2018 11:36:11.580\",\"UserLogin\":\"MOSCOW\\\\U_M0WY5\"},{\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"MessageID\":10059,\"Status\":\"delivered\",\"TimeStamp\":\"18.10.2018 11:36:35.097\",\"UserLogin\":\"MOSCOW\\\\U_M0YFV\"}],\"ToUsers\":\"MOSCOW\\\\U_M0WY5\",\"Value\":\"12\"},{\"Attachment\":[],\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"CodeKey\":\"\",\"CodeVector\":\"\",\"CreationDate\":\"13.10.2018 11:36:12.393\",\"FromUser\":{\"ConnDate\":\"18.10.2018 11:44:15.033\",\"FullName\":\"Субботин Олег Олегович\",\"JobTitle\":\"Ведущий разработчик\",\"Login\":\"MOSCOW\\\\U_M0YFV\",\"ProfilPicURL\":\"http://alfa:80/my/User%20Photos/Profile%20Pictures/MOSCOW_U_M0YFV_FSize.jpg;#750;#420\",\"PublicKey\":null,\"Status\":1},\"USER_ID\":10060,\"Status\":[{\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"MessageID\":10060,\"Status\":\"sent\",\"TimeStamp\":\"18.10.2018 11:36:12.440\",\"UserLogin\":\"MOSCOW\\\\U_M0WY5\"},{\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"MessageID\":10060,\"Status\":\"delivered\",\"TimeStamp\":\"18.10.2018 11:36:35.127\",\"UserLogin\":\"MOSCOW\\\\U_M0YFV\"}],\"ToUsers\":\"MOSCOW\\\\U_M0WY5\",\"Value\":\"13\"},{\"Attachment\":[],\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"CodeKey\":\"\",\"CodeVector\":\"\",\"CreationDate\":\"11.10.2018 11:36:13.110\",\"FromUser\":{\"ConnDate\":\"18.10.2018 11:44:15.033\",\"FullName\":\"Субботин Олег Олегович\",\"JobTitle\":\"Ведущий разработчик\",\"Login\":\"MOSCOW\\\\U_M0YFV\",\"ProfilPicURL\":\"http://alfa:80/my/User%20Photos/Profile%20Pictures/MOSCOW_U_M0YFV_FSize.jpg;#750;#420\",\"PublicKey\":null,\"Status\":1},\"USER_ID\":10061,\"Status\":[{\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"MessageID\":10061,\"Status\":\"sent\",\"TimeStamp\":\"18.10.2018 11:36:13.143\",\"UserLogin\":\"MOSCOW\\\\U_M0WY5\"},{\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"MessageID\":10061,\"Status\":\"delivered\",\"TimeStamp\":\"18.10.2018 11:36:35.157\",\"UserLogin\":\"MOSCOW\\\\U_M0YFV\"}],\"ToUsers\":\"MOSCOW\\\\U_M0WY5\",\"Value\":\"14\"},{\"Attachment\":[],\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"CodeKey\":\"\",\"CodeVector\":\"\",\"CreationDate\":\"18.10.2018 11:36:13.800\",\"FromUser\":{\"ConnDate\":\"18.10.2018 11:44:15.033\",\"FullName\":\"Субботин Олег Олегович\",\"JobTitle\":\"Ведущий разработчик\",\"Login\":\"MOSCOW\\\\U_M0YFV\",\"ProfilPicURL\":\"http://alfa:80/my/User%20Photos/Profile%20Pictures/MOSCOW_U_M0YFV_FSize.jpg;#750;#420\",\"PublicKey\":null,\"Status\":1},\"USER_ID\":10062,\"Status\":[{\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"MessageID\":10062,\"Status\":\"sent\",\"TimeStamp\":\"18.10.2018 11:36:13.830\",\"UserLogin\":\"MOSCOW\\\\U_M0WY5\"},{\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"MessageID\":10062,\"Status\":\"delivered\",\"TimeStamp\":\"18.10.2018 11:36:35.190\",\"UserLogin\":\"MOSCOW\\\\U_M0YFV\"}],\"ToUsers\":\"MOSCOW\\\\U_M0WY5\",\"Value\":\"15\"},{\"Attachment\":[],\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"CodeKey\":\"\",\"CodeVector\":\"\",\"CreationDate\":\"18.10.2018 11:36:14.393\",\"FromUser\":{\"ConnDate\":\"18.10.2018 11:44:15.033\",\"FullName\":\"Субботин Олег Олегович\",\"JobTitle\":\"Ведущий разработчик\",\"Login\":\"MOSCOW\\\\U_M0YFV\",\"ProfilPicURL\":\"http://alfa:80/my/User%20Photos/Profile%20Pictures/MOSCOW_U_M0YFV_FSize.jpg;#750;#420\",\"PublicKey\":null,\"Status\":1},\"USER_ID\":10063,\"Status\":[{\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"MessageID\":10063,\"Status\":\"sent\",\"TimeStamp\":\"18.10.2018 11:36:14.423\",\"UserLogin\":\"MOSCOW\\\\U_M0WY5\"},{\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"MessageID\":10063,\"Status\":\"delivered\",\"TimeStamp\":\"18.10.2018 11:36:35.220\",\"UserLogin\":\"MOSCOW\\\\U_M0YFV\"}],\"ToUsers\":\"MOSCOW\\\\U_M0WY5\",\"Value\":\"16\"},{\"Attachment\":[],\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"CodeKey\":\"\",\"CodeVector\":\"\",\"CreationDate\":\"18.10.2018 11:36:14.923\",\"FromUser\":{\"ConnDate\":\"18.10.2018 11:44:15.033\",\"FullName\":\"Субботин Олег Олегович\",\"JobTitle\":\"Ведущий разработчик\",\"Login\":\"MOSCOW\\\\U_M0YFV\",\"ProfilPicURL\":\"http://alfa:80/my/User%20Photos/Profile%20Pictures/MOSCOW_U_M0YFV_FSize.jpg;#750;#420\",\"PublicKey\":null,\"Status\":1},\"USER_ID\":10064,\"Status\":[{\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"MessageID\":10064,\"Status\":\"sent\",\"TimeStamp\":\"18.10.2018 11:36:14.970\",\"UserLogin\":\"MOSCOW\\\\U_M0WY5\"},{\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"MessageID\":10064,\"Status\":\"delivered\",\"TimeStamp\":\"18.10.2018 11:36:35.253\",\"UserLogin\":\"MOSCOW\\\\U_M0YFV\"}],\"ToUsers\":\"MOSCOW\\\\U_M0WY5\",\"Value\":\"17\"},{\"Attachment\":[],\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"CodeKey\":\"\",\"CodeVector\":\"\",\"CreationDate\":\"18.10.2018 11:36:15.767\",\"FromUser\":{\"ConnDate\":\"18.10.2018 11:44:15.033\",\"FullName\":\"Субботин Олег Олегович\",\"JobTitle\":\"Ведущий разработчик\",\"Login\":\"MOSCOW\\\\U_M0YFV\",\"ProfilPicURL\":\"http://alfa:80/my/User%20Photos/Profile%20Pictures/MOSCOW_U_M0YFV_FSize.jpg;#750;#420\",\"PublicKey\":null,\"Status\":1},\"USER_ID\":10065,\"Status\":[{\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"MessageID\":10065,\"Status\":\"sent\",\"TimeStamp\":\"18.10.2018 11:36:15.800\",\"UserLogin\":\"MOSCOW\\\\U_M0WY5\"},{\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"MessageID\":10065,\"Status\":\"delivered\",\"TimeStamp\":\"18.10.2018 11:36:35.267\",\"UserLogin\":\"MOSCOW\\\\U_M0YFV\"}],\"ToUsers\":\"MOSCOW\\\\U_M0WY5\",\"Value\":\"18\"},{\"Attachment\":[],\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"CodeKey\":\"\",\"CodeVector\":\"\",\"CreationDate\":\"18.10.2018 11:36:16.377\",\"FromUser\":{\"ConnDate\":\"18.10.2018 11:44:15.033\",\"FullName\":\"Субботин Олег Олегович\",\"JobTitle\":\"Ведущий разработчик\",\"Login\":\"MOSCOW\\\\U_M0YFV\",\"ProfilPicURL\":\"http://alfa:80/my/User%20Photos/Profile%20Pictures/MOSCOW_U_M0YFV_FSize.jpg;#750;#420\",\"PublicKey\":null,\"Status\":1},\"USER_ID\":10066,\"Status\":[{\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"MessageID\":10066,\"Status\":\"sent\",\"TimeStamp\":\"18.10.2018 11:36:16.410\",\"UserLogin\":\"MOSCOW\\\\U_M0WY5\"},{\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"MessageID\":10066,\"Status\":\"delivered\",\"TimeStamp\":\"18.10.2018 11:36:35.313\",\"UserLogin\":\"MOSCOW\\\\U_M0YFV\"}],\"ToUsers\":\"MOSCOW\\\\U_M0WY5\",\"Value\":\"19\"},{\"Attachment\":[],\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"CodeKey\":\"\",\"CodeVector\":\"\",\"CreationDate\":\"18.10.2018 11:36:20.923\",\"FromUser\":{\"ConnDate\":\"18.10.2018 11:44:15.033\",\"FullName\":\"Субботин Олег Олегович\",\"JobTitle\":\"Ведущий разработчик\",\"Login\":\"MOSCOW\\\\U_M0YFV\",\"ProfilPicURL\":\"http://alfa:80/my/User%20Photos/Profile%20Pictures/MOSCOW_U_M0YFV_FSize.jpg;#750;#420\",\"PublicKey\":null,\"Status\":1},\"USER_ID\":10067,\"Status\":[{\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"MessageID\":10067,\"Status\":\"sent\",\"TimeStamp\":\"18.10.2018 11:36:20.970\",\"UserLogin\":\"MOSCOW\\\\U_M0WY5\"},{\"ChatID\":\"bb1ec541-2072-4a68-909f-d2b7bcde82cf\",\"MessageID\":10067,\"Status\":\"delivered\",\"TimeStamp\":\"18.10.2018 11:36:35.347\",\"UserLogin\":\"MOSCOW\\\\U_M0YFV\"}],\"ToUsers\":\"MOSCOW\\\\U_M0WY5\",\"Value\":\"20\"}],\"Type\":\"personal\",\"Users\":[{\"ConnDate\":\"18.10.2018 11:35:29.813\",\"FullName\":\"Авдуевский Михаил Сергеевич\",\"JobTitle\":\"Главный разработчик\",\"Login\":\"MOSCOW\\\\U_M0WY5\",\"ProfilPicURL\":\"http://alfa:80/my/User%20Photos/Profile%20Pictures/MOSCOW_U_M0WY5_FSize.jpg;#1280;#1280\",\"PublicKey\":null,\"Status\":0},{\"ConnDate\":\"18.10.2018 11:44:15.033\",\"FullName\":\"Субботин Олег Олегович\",\"JobTitle\":\"Ведущий разработчик\",\"Login\":\"MOSCOW\\\\U_M0YFV\",\"ProfilPicURL\":\"http://alfa:80/my/User%20Photos/Profile%20Pictures/MOSCOW_U_M0YFV_FSize.jpg;#750;#420\",\"PublicKey\":null,\"Status\":1}]}\n";
    }
}
