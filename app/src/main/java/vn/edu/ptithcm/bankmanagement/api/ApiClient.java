package vn.edu.ptithcm.bankmanagement.api;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// singleton class
public class ApiClient {
    private static final String BASE_URL =  "http://localhost:8080/web_forbank/";

    Context context;
    Retrofit retrofit;

    public ApiClient(Context context) {
        this.context = context;

        OkHttpClient client = new OkHttpClient();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        client = builder.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public UserService getUserService() {
        return retrofit.create(UserService.class);
    }

    public DepositWithdrawService getDepositWithdrawService() {
        return retrofit.create(DepositWithdrawService.class);
    }

    public MoneyTransferService getMoneyTransferService() {
        return retrofit.create(MoneyTransferService.class);
    }

    public LoadImageService getImageService() {
        return retrofit.create(LoadImageService.class);
    }




//    private static class SessionCookieJar implements CookieJar {
//
//        private List<Cookie> cookies;
//
//        @Override
//        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
//            if (url.encodedPath().endsWith("login")) {
//                this.cookies = new ArrayList<>(cookies);
//            }
//        }
//
//        @Override
//        public List<Cookie> loadForRequest(HttpUrl url) {
//            if (!url.encodedPath().endsWith("login") && cookies != null) {
//                return cookies;
//            }
//            return Collections.emptyList();
//        }
//    }
}