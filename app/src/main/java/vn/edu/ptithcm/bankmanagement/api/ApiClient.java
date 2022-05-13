package vn.edu.ptithcm.bankmanagement.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://localhost:8080/web_forbank/";

    Retrofit retrofit;

    public ApiClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
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

    public UserStatisticService getUserStatisticService() {
        return retrofit.create(UserStatisticService.class);
    }

    public LoginService getLoginService() {
        return retrofit.create(LoginService.class);
    }

}