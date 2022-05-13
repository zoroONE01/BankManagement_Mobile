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
import vn.edu.ptithcm.bankmanagement.utility.Helper;
import vn.edu.ptithcm.bankmanagement.utility.Utility;

public class MainActivity extends AppCompatActivity {
    //    static String TAG = MainActivity.class.getName();
    static String TAG = "----------";
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
        apiClient = new ApiClient(this);
        userService = apiClient.getUserService();
        transferService = apiClient.getMoneyTransferService();
        depositWithdrawService = apiClient.getDepositWithdrawService();

        button = findViewById(R.id.b_sign_in);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //login

            }
        });
        Helper.doLogin(this, apiClient.getUserService(), "adminCN1", "Admin1234.");
    }

}