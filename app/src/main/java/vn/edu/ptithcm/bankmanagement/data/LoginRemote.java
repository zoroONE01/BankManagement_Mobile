package vn.edu.ptithcm.bankmanagement.data;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.ptithcm.bankmanagement.api.ApiClient;
import vn.edu.ptithcm.bankmanagement.api.LoginService;
import vn.edu.ptithcm.bankmanagement.data.model.LoggedInUser;
import vn.edu.ptithcm.bankmanagement.data.model.LoginRequest;
import vn.edu.ptithcm.bankmanagement.data.model.RegisterRequest;
import vn.edu.ptithcm.bankmanagement.utility.Utility;

public class LoginRemote {

    private static LoginRemote instance;
    private final LoginService loginService;
    private final MutableLiveData<LoggedInUser> loginInUser = new MutableLiveData<>();
    private final MutableLiveData<LoggedInUser> registeredInUser = new MutableLiveData<>();

    public LoginRemote() {
        ApiClient apiClient = new ApiClient();
        loginService = apiClient.getLoginService();
    }

    public static LoginRemote getInstance() {
        if (instance == null) {
            instance = new LoginRemote();
        }
        return instance;
    }

    public LoginService getLoginService() {
        return loginService;
    }

    public void login(String username, String password, OnCompleteCallBack<LoggedInUser> onCompleteCallBack, String firebaseToken) {
        try {
            LoginRequest loginRequest = new LoginRequest(username, password);
            loginService.login(Utility.COOKIE, loginRequest).enqueue(new Callback<LoggedInUser>() {

                @Override
                public void onResponse(@NonNull Call<LoggedInUser> call, @NonNull retrofit2.Response<LoggedInUser> response) {
                    if (response.body() != null) {
                        LoggedInUser logged = response.body();
                        Log.d("LoginRemote", "user login Response: " + response.body().toString());

                        Utility.USER.setUserId(logged.getUserId());
                        Utility.USER.setImageUrl(logged.getImageUrl());
                        Utility.USER.setKhachHangID(logged.getKhachHangID());

                        HashMap<String, String> values = new HashMap<>();
                        values.put("firebaseToken", firebaseToken);
                        values.put("userId", Utility.USER.getUserId());
                        loginService.updateFirebaseToken(Utility.COOKIE, "updateFirebaseToken", values).enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                Log.d("----updateFirebaseToken", "thanh cong");
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                Log.e("----updateFirebaseToken", t.getMessage());
                            }
                        });

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
                    t.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void register(String cmnd, String ho, String ten, String username, String password, String imageUrl, OnCompleteCallBack<LoggedInUser> onCompleteCallBack) {
        try {
            RegisterRequest registerRequest = new RegisterRequest(cmnd, ho, ten, username, password, imageUrl);

            loginService.register(Utility.COOKIE, registerRequest).enqueue(new Callback<LoggedInUser>() {
                @Override
                public void onResponse(@NonNull Call<LoggedInUser> call, @NonNull retrofit2.Response<LoggedInUser> response) {
                    if (response.body() != null) {
                        Log.d("LoginRemote", "register Response: " + response.body().toString());

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
                    t.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
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