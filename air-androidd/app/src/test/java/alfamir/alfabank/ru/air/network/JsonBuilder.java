package alfamir.alfabank.ru.air.network;

import com.google.gson.Gson;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import ru.alfabank.alfamir.data.dto.api.Request;
import ru.alfabank.alfamir.data.dto.api.RequestBody;

public class JsonBuilder {

    @Test
    public void test(){
        List<String> chatIds = new ArrayList<>();
        chatIds.add("MOSCOW\\U_M0ECB");
        chatIds.add("MOSCOW\\U_P0116");
        chatIds.add("MOSCOW\\U_M0SU5");
        chatIds.add("MOSCOW\\U_M0W6Q");
        chatIds.add("MOSCOW\\U_M0D2T");
        Request request = formLongPollRequest(chatIds);
        String body = request.getBody().getData();
        System.out.println(body);

    }

    public static Request formLongPollRequest(List<String> chatIds){
        String groupedIds = "";
        groupedIds = new Gson().toJson(chatIds);

//        if(chatIds.isEmpty()) {
//
//        } else {
//            for (int i = 0; i < chatIds.size(); i++){
//                String id = chatIds.get(i);
//                if(i == 0){
//                    groupedIds = id;
//                } else {
//                    groupedIds = groupedIds + ";#" + id;
//                }
//            }
//        }

        return new Request(new RequestBody("}"));
    }

}
