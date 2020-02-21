package alfamir.alfabank.ru.air.network;

import retrofit2.Retrofit;
import ru.alfabank.alfamir.data.dto.api.Request;
import ru.alfabank.alfamir.data.dto.api.RequestBody;
import ru.alfabank.alfamir.data.source.remote.api.ApiClient;

public class ApiTester {

    private Retrofit mRetrofit;
    private ApiClient mClient;
    private Request mRequest;


    private Request createRequest(){
        String isMerryCrismass = "{wcf:'service_functions',platform:'ios',airversion:'2.0',userlogin:'moscow\\u_m0wy5;49801',request:{action:'isnewyearcome'}}";
        Request request = new Request(new RequestBody(isMerryCrismass));


//        Request request = RequestFactory.formLoadChatList();
        return request;
    }

}

