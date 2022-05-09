package vn.edu.ptithcm.bankmanagement;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.ptithcm.bankmanagement.api.ApiClient;
import vn.edu.ptithcm.bankmanagement.api.DepositWithdrawService;
import vn.edu.ptithcm.bankmanagement.api.MoneyTransferService;
import vn.edu.ptithcm.bankmanagement.api.UserService;
import vn.edu.ptithcm.bankmanagement.utility.Utility;

public class MainActivity extends AppCompatActivity {
    static String TAG = MainActivity.class.getName();
    Button button;

    UserService userService;
    MoneyTransferService transferService;
    DepositWithdrawService depositWithdrawService;

    ApiClient apiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        // DEMO dang nhap + nap tien
//        apiClient = new ApiClient(this);
//        userService = apiClient.getUserService();
//        transferService = apiClient.getMoneyTransferService();
//        depositWithdrawService = apiClient.getDepositWithdrawService();
//
//        button = findViewById(R.id.b_sign_in);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                doDepositOrWithdraw("000000000", "1000321", "GT");
//            }
//        });
//
//        doLogin("admin", "admin");
    }

    private void doLogin(String username, String password) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        Call<JsonObject> call = userService.login(username, password);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "login Response: " + response.body().toString());
                    Log.d(TAG, "headers values: " + response.headers());

                    String sessionId = response.headers().get("Set-Cookie");

                    if (sessionId == null || sessionId.isEmpty()) {
                        Log.d(TAG, "Error: no session id" + response.body());

                        return;
                    }

                    // save session id in preference
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(Utility.PREF_COOKIES, sessionId);
                    editor.apply();
                } else {
                    Log.d(TAG, "Login Response Error" + response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Log.d(TAG, "Login Failure");
            }
        });
    }

    private void doDepositOrWithdraw(String id, String amount, String gd) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // get session id in preference
        String sessionId = prefs.getString(Utility.PREF_COOKIES, "");

        if (sessionId.isEmpty()) {
            Toast.makeText(this, "Error: no session id", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, String> values = new HashMap<>();
        values.put(DepositWithdrawService.key1, id);
        values.put(DepositWithdrawService.key2, amount);
        values.put(DepositWithdrawService.key3, gd);

        Call<JsonObject> call = depositWithdrawService.depositOrWithdraw(sessionId, values);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "dpwd Response: " + (response.body() != null ? response.body().toString() : "dpwd response ok"));
                } else {
                    try {
                        assert response.errorBody() != null;
                        String err = String.valueOf(response.errorBody().charStream());
                        Log.d(TAG, "dpwd Response Error: " + err);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Log.d(TAG, "dpwd Failure");
            }
        });
    }
}