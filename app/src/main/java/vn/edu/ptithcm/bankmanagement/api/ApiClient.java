package vn.edu.ptithcm.bankmanagement.api;

import android.content.Context;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://localhost:8080/web_forbank/";

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

    public UserStatisticService getUserStatisticService() {
        return retrofit.create(UserStatisticService.class);
    }

}