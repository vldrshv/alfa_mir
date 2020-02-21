package ru.alfabank.alfamir.data.source.remote.api;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import ru.alfabank.alfamir.Constants;
import ru.alfabank.alfamir.utility.network.CookiesInterceptor;
//import ru.alfabank.oavdo.mars.Mars;

public class ApiClientProvider {

    private ApiClient mApiClient;

    @Inject
    ApiClientProvider() {
    }

//    public void initialize(Mars mars) {
//        try {
//            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//            trustManagerFactory.init((KeyStore) null);
//            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
//            X509TrustManager mTrustManager = (X509TrustManager) trustManagers[0];
//            OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
//            builder.sslSocketFactory(mars.getSecuredRequester("CHR").getSSLSocketFactory(), mTrustManager);
//            OkHttpClient okHttpClient = builder
//                    .connectTimeout(90, TimeUnit.SECONDS)
//                    .writeTimeout(90, TimeUnit.SECONDS)
//                    .readTimeout(90, TimeUnit.SECONDS)
//                    .build();
//            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl(Constants.Companion.getAPI_SERVER())
//                    .client(okHttpClient)
//                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                    .addConverterFactory(SimpleXmlConverterFactory.create())
//                    .build();
//            mApiClient = retrofit.create(ApiClient.class);
//
//
//        } catch (Exception ignored) { // TODO should it be ignored though?
//        }
//    }

    public void initializeHomo() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        OkHttpClient okHttpClient = builder
                .connectTimeout(90, TimeUnit.SECONDS)
                .writeTimeout(90, TimeUnit.SECONDS)
                .readTimeout(90, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.Companion.getAPI_SERVER())
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
        mApiClient = retrofit.create(ApiClient.class);
    }

    public void initialize() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        OkHttpClient okHttpClient = builder
                .connectTimeout(90, TimeUnit.SECONDS)
                .writeTimeout(90, TimeUnit.SECONDS)
                .readTimeout(90, TimeUnit.SECONDS)
                .addNetworkInterceptor(CookiesInterceptor.INSTANCE.getSaveCookies())
                .addNetworkInterceptor(CookiesInterceptor.INSTANCE.getAddCookies())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.Companion.getAPI_SERVER())
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        mApiClient = retrofit.create(ApiClient.class);
    }

    public ApiClient getApiClient() {
        return mApiClient;
    }
}
