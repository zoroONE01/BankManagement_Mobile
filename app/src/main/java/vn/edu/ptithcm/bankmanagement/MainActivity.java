package vn.edu.ptithcm.bankmanagement;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.ptithcm.bankmanagement.api.ApiClient;
import vn.edu.ptithcm.bankmanagement.api.UserService;

public class MainActivity extends AppCompatActivity {
    static String TAG = MainActivity.class.getName();


    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        // DEMO dang nhap
        ApiClient api = ApiClient.getInstance();
        userService = api.getUserService();
        doLogin("admin", "admin");
    }

    private void doLogin(String username, String password) {
        Call<JsonObject> call = userService.login(username, password);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Response: " + response.body().toString());
                } else {
                    Log.d(TAG, "Response Error");
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d(TAG, "Failure");
            }
        });
    }
}