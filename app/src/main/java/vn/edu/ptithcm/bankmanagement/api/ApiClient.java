package vn.edu.ptithcm.bankmanagement.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// singleton class
public class ApiClient {
    private static final String BASE_URL =  "http://localhost:8080/web_forbank/";

    private static ApiClient instance;
    Retrofit retrofit;

    // k dc goi constructor truc tiep
    private ApiClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized ApiClient getInstance() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance;
    }

    public UserService getUserService() {
        return retrofit.create(UserService.class);
    }

}