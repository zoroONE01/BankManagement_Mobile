package vn.edu.ptithcm.bankmanagement;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.ptithcm.bankmanagement.api.ApiClient;
import vn.edu.ptithcm.bankmanagement.api.UserService;
import vn.edu.ptithcm.bankmanagement.databinding.ActivityBankBinding;
import vn.edu.ptithcm.bankmanagement.utility.Utility;

public class BankActivity extends AppCompatActivity {
    final String TAG = BankActivity.class.getName();

    private AppBarConfiguration appBarConfiguration;
    private ActivityBankBinding binding;

    ApiClient api;
    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBankBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_bank);

        api = new ApiClient();
        userService = api.getUserService();

        doLogin();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_bank);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    void doLogin() {
        Call<JsonObject> call = userService.login("adminCN1", "Admin1234.");

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "login Response: " + response.body().toString());
                    Log.d(TAG, "headers values: " + response.headers());

                    Utility.COOKIE = response.headers().get("Set-Cookie");

                    if (Utility.COOKIE == null || Utility.COOKIE.isEmpty()) {
                        Log.d(TAG, "Error: no session id");
                        Toast.makeText(BankActivity.this, "Không có session", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d(TAG, "Login Response Error" + response.body());
                    Toast.makeText(BankActivity.this, "Đã có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Log.d(TAG, "Login Failure");
                Toast.makeText(BankActivity.this, "Không thể kết nối tới máy chủ", Toast.LENGTH_SHORT).show();

                t.printStackTrace();
            }
        });
    }
}