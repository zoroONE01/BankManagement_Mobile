package vn.edu.ptithcm.bankmanagement.ui.depositwithdraw;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.google.gson.JsonObject;

import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.ptithcm.bankmanagement.R;
import vn.edu.ptithcm.bankmanagement.api.ApiClient;
import vn.edu.ptithcm.bankmanagement.api.DepositWithdrawService;
import vn.edu.ptithcm.bankmanagement.api.UserStatisticService;
import vn.edu.ptithcm.bankmanagement.utility.Utility;

public class DepositWithdrawActivity extends AppCompatActivity {
    private final String TAG = DepositWithdrawActivity.class.getName();

    TextView title;
    EditText sotien;
    Button btn;

    AppCompatImageButton b_back;


    ApiClient apiClient;
    DepositWithdrawService depositWithdrawService;
    UserStatisticService userStatisticService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_withdraw);

        initComponents();
    }

    void doDepositOrWithdraw(String id, String amount, String gd) {
        HashMap<String, String> values = new HashMap<>();
        values.put(DepositWithdrawService.key1, id);
        values.put(DepositWithdrawService.key2, amount);
        values.put(DepositWithdrawService.key3, gd);

        Call<JsonObject> call = depositWithdrawService.depositOrWithdraw(Utility.COOKIE, values);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    // TODO show chuyen tien ok
                    Log.d(TAG, "dpwd Response: " + (response.body() != null ? response.body().toString() : "dpwd response ok"));
                    if (response.body().toString().contains("FOREIGN")) {
                        Toast.makeText(DepositWithdrawActivity.this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                    } else if (response.body().toString().contains("CHECK")) {
                        Toast.makeText(DepositWithdrawActivity.this, "Số dư không đủ", Toast.LENGTH_SHORT).show();
                    } else {
                        finish();
                    }
                } else if (response.code() == HttpsURLConnection.HTTP_UNAUTHORIZED) {
                    // Handle unauthorized
                    Log.d(TAG, "dpwd 401");
                } else {
                    try {
                        if (response.errorBody() == null) {
                            Log.d(TAG, "transfer Response Error. No message");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Log.d(TAG, "Failure");
                t.printStackTrace();
            }
        });
    }

    private void initComponents() {
        apiClient = new ApiClient();
        depositWithdrawService = apiClient.getDepositWithdrawService();
        userStatisticService = apiClient.getUserStatisticService();

        title = findViewById(R.id.title);
        sotien = findViewById(R.id.et_transaction_value);
        btn = findViewById(R.id.b_confirm_transaction);

        b_back = findViewById(R.id.b_menu);
        b_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Intent intent = this.getIntent();

        if (intent.getIntExtra("INTENT", 0) == 0) {
            // intent nap tien
            title.setText(R.string.action_recharge);

            btn.setOnClickListener(view -> {
                if (!checkInput()) {
                    return;
                }
                String stk = Utility.LIST_TK.get(0).getSoTK();
                doDepositOrWithdraw(stk, sotien.getText().toString(), "GT");
            });
        } else {
            // intent rut tien
            title.setText(R.string.action_withdraw);

            btn.setOnClickListener(view -> {
                if (!checkInput()) {
                    return;
                }
                String stk = Utility.LIST_TK.get(0).getSoTK();
                doDepositOrWithdraw(stk, sotien.getText().toString(), "RT");
            });
        }
    }

    boolean checkInput() {
        String amount = sotien.getText().toString();

        if (amount.isEmpty()) {
            Toast.makeText(this, "Hãy nhập số tiền", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (Double.parseDouble(amount) < 100000) {
            Toast.makeText(this, "Chỉ chuyển số tiền > 100.000đ", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}