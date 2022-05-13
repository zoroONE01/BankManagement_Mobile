package vn.edu.ptithcm.bankmanagement.data;

import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vn.edu.ptithcm.bankmanagement.api.LoginService;
import vn.edu.ptithcm.bankmanagement.data.model.LoggedInUser;
import vn.edu.ptithcm.bankmanagement.data.model.LoginRequest;
import vn.edu.ptithcm.bankmanagement.data.model.RegisterRequest;
import vn.edu.ptithcm.bankmanagement.utility.Utility;

public class LoginRemote {

    private final LoginService loginService;
    private static LoginRemote instance;
    private final MutableLiveData<LoggedInUser> loginInUser = new MutableLiveData<>();
    private final MutableLiveData<LoggedInUser> registeredInUser = new MutableLiveData<>();

    public LoginRemote() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        Interceptor interceptor2 = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                SystemClock.sleep(2000);
                return chain.proceed(chain.request());
            }
        };
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://localhost:8080/web_forbank/").client(client)
                .addConverterFactory(GsonConverterFactory.create()).build();
        loginService = retrofit.create(LoginService.class);
    }

    public LoginService getLoginService() {
        return loginService;
    }

    public static LoginRemote getInstance() {
        if (instance == null) {
            instance = new LoginRemote();
        }
        return instance;
    }

    public void login(String username, String password, OnCompleteCallBack<LoggedInUser> onCompleteCallBack) {
        try {
            LoginRequest loginRequest = new LoginRequest(username, password);
            // TODO: handle loggedInUser authentication
            loginService.login(Utility.COOKIE, loginRequest).enqueue(new Callback<LoggedInUser>() {
                @Override
                public void onResponse(Call<LoggedInUser> call, retrofit2.Response<LoggedInUser> response) {
                    if (response.body() != null) {
                        LoggedInUser logged = response.body();
                        loginInUser.setValue(logged);
                        onCompleteCallBack.done(logged);
                    } else {
                        loginInUser.setValue(null);
                        onCompleteCallBack.done(null);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<LoggedInUser> call, Throwable t) {
                    onCompleteCallBack.done(null);
                }
            });
        } catch (Exception e) {

        }
    }

    public void register(String cmnd, String ho, String ten, String username, String password, OnCompleteCallBack<LoggedInUser> onCompleteCallBack) {
        try {
            RegisterRequest registerRequest = new RegisterRequest(cmnd, ho, ten, username, password);
            // TODO: handle loggedInUser authentication
            loginService.register(Utility.COOKIE, registerRequest).enqueue(new Callback<LoggedInUser>() {
                @Override
                public void onResponse(@NonNull Call<LoggedInUser> call, retrofit2.Response<LoggedInUser> response) {
                    if (response.body() != null) {
                        LoggedInUser logged = response.body();
                        registeredInUser.setValue(logged);
                        onCompleteCallBack.done(logged);
                    } else {
                        registeredInUser.setValue(null);
                        onCompleteCallBack.done(null);
                    }
                }

                @Override
                public void onFailure(Call<LoggedInUser> call, Throwable t) {
                    onCompleteCallBack.done(null);
                }
            });
        } catch (Exception e) {

        }
    }

    public MutableLiveData<LoggedInUser> getLoginInUser() {
        return loginInUser;
    }

    public MutableLiveData<LoggedInUser> getRegisteredInUser() {
        return registeredInUser;
    }

    public void logout() {
        // TODO: revoke authentication
    }


}