package vn.edu.ptithcm.bankmanagement.utility;

import android.content.Context;
import android.security.keystore.UserNotAuthenticatedException;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import vn.edu.ptithcm.bankmanagement.api.ApiClient;
import vn.edu.ptithcm.bankmanagement.api.UserService;
import vn.edu.ptithcm.bankmanagement.api.UserStatisticService;
import vn.edu.ptithcm.bankmanagement.data.model.TaiKhoan;
import vn.edu.ptithcm.bankmanagement.data.model.ThongKeGD;
import vn.edu.ptithcm.bankmanagement.exception.AuthenticatedException;

public class Helper {
    static final String TAG = Helper.class.getName();

    public static boolean doLogin(Context context, UserService userService, String username, String password) {
        Call<JsonObject> call = userService.login(username, password);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "login Response: " + response.body().toString());
                    Log.d(TAG, "headers values: " + response.headers());

                    Utility.COOKIE = response.headers().get("Set-Cookie");
                } else {
                    Log.d(TAG, "Login Response Error" + response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Log.d(TAG, "Login Failure");
                t.printStackTrace();
            }
        });
        if (Utility.COOKIE == null || Utility.COOKIE.isEmpty()) {
            Log.d(TAG, "Error: no session id");
            return false;
        }
        return true;
    }

    public static void doGetAllTk(Context context, UserStatisticService userStatisticService, String cmnd) throws AuthenticatedException {
        Utility.LIST_TK.clear();

        Call<JsonArray> call = userStatisticService.getAllTk(Utility.COOKIE, "123456789");

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "list tk Response: " + (response.body() != null ? response.body().toString() : "list tk response ok"));

                    JsonArray listTK = (JsonArray) response.body();
                    Utility.LIST_TK.clear();

                    for (JsonElement ele : listTK) {
                        JsonObject e = ele.getAsJsonObject();
                        Log.d(TAG, "tk: " + e.toString());

                        TaiKhoan tk = new TaiKhoan(e.get("soTK").getAsString(),
                                e.get("cmnd").getAsString(),
                                e.get("soDu").getAsDouble(),
                                e.get("maCN").getAsString().trim(),
                                e.get("ngayGD").getAsLong());

                        Utility.LIST_TK.add(tk);
                        Log.d(TAG, tk.toString());
                    }

                } else if (response.code() == HttpsURLConnection.HTTP_UNAUTHORIZED) {
                    // Handle unauthorized
                    // TODO go back to login
                    Log.d(TAG, "list tk 401");
                } else {
                    try {
                        if (response.errorBody() == null) {
                            Log.d(TAG, "list tk Response Error. No message");
                        } else if (response.errorBody().string().contains("FOREIGN")) {
                            Log.d(TAG, "list tk k ton tai");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Log.d(TAG, "list tk Failure");
                t.printStackTrace();
            }
        });
    }


    public static List<ThongKeGD> doGetListTransactions(Context context, UserStatisticService userStatisticService, String stk) {
        List<ThongKeGD> list = new ArrayList<>();

        if (Utility.LIST_TK.isEmpty()) {
            Call<JsonArray> call = userStatisticService.getStat(Utility.COOKIE, stk, "2022-1-1", "2025-1-1");

            call.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "statistic Response: " + (response.body() != null ? response.body().toString() : "dpwd response ok"));

                        JsonArray array = (JsonArray) response.body();

                        for (JsonElement ele : array) {
                            JsonObject e = ele.getAsJsonObject();
                            Log.d(TAG, "tk: " + e.toString());
                            ThongKeGD tk = new ThongKeGD(e.get("balanceBefore").getAsLong(),
                                    e.get("ngayGD").getAsLong(),
                                    e.get("loaiGD").getAsString(),
                                    e.get("soTien").getAsLong(),
                                    e.get("balanceAfter").getAsLong());
                            list.add(tk);
                        }
                    } else if (response.code() == HttpsURLConnection.HTTP_UNAUTHORIZED) {
                        // Handle unauthorized
                        // TODO go back to login
                        Log.d(TAG, "list tk 401");
                    } else {
                        try {
                            if (response.errorBody() == null) {
                                Log.d(TAG, "transfer Response Error. No message");
                            } else if (response.errorBody().string().contains("FOREIGN")) {
                                Log.d(TAG, "list tk k ton tai");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                    Log.d(TAG, "list tk Failure");
                    t.printStackTrace();
                }
            });
        }
        return list;
    }
}