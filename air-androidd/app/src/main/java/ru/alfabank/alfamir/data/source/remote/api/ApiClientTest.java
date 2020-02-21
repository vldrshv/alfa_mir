package ru.alfabank.alfamir.data.source.remote.api;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import ru.alfabank.alfamir.data.dto.api.Request;
import ru.alfabank.alfamir.data.dto.api.Response;

public interface ApiClientTest extends Test {

    @POST("/Orion/Dock")
    Observable<Response> requestX(@Body Request request);

    @POST("/Orion/Dock")
    Call<Response> request(@Body Request request);

}