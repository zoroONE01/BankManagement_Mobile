package vn.edu.ptithcm.bankmanagement.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.sql.Timestamp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    //    private static final String BASE_URL = "http://192.168.43.31:8080/web_forbank/";
    private static final String BASE_URL = "http://localhost:8080/web_forbank/";

    Retrofit retrofit;

    public ApiClient() {
        GsonBuilder builder = new GsonBuilder();

        builder.registerTypeAdapter(
                Timestamp.class,
                (JsonDeserializer<Timestamp>) (json, typeOfT, context) -> new Timestamp(json.getAsJsonPrimitive().getAsLong()));

        Gson gson = builder.create();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
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

    public ImageService getImageService() {
        return retrofit.create(ImageService.class);
    }

    public UserStatisticService getUserStatisticService() {
        return retrofit.create(UserStatisticService.class);
    }

    public LoginService getLoginService() {
        return retrofit.create(LoginService.class);
    }

    public UserInfoService getUserInfoService() {
        return retrofit.create(UserInfoService.class);
    }

    public ProfileService getProfileService() {
        return retrofit.create(ProfileService.class);
    }
}