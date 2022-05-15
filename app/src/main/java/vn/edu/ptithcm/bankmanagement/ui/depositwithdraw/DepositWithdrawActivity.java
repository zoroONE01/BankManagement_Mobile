package vn.edu.ptithcm.bankmanagement.ui.depositwithdraw;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

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

    EditText sotien;
    RadioGroup radioGroup;
    Button btn;

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
        DepositWithdrawService depositWithdrawService = apiClient.getDepositWithdrawService();

        HashMap<String, String> values = new HashMap<>();
        values.put(DepositWithdrawService.key1, id);
        values.put(DepositWithdrawService.key2, amount);
        values.put(DepositWithdrawService.key3, gd);

        Call<JsonObject> call = depositWithdrawService.depositOrWithdraw(Utility.COOKIE, values);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "dpwd Response: " + (response.body() != null ? response.body().toString() : "dpwd response ok"));
                } else if (response.code() == HttpsURLConnection.HTTP_UNAUTHORIZED) {
                    // Handle unauthorized
                    // TODO go back to login
                    Log.d(TAG, "dpwd 401");
                } else {
                    try {
                        Toast.makeText(DepositWithdrawActivity.this, "Chuyển tiền không thành công", Toast.LENGTH_SHORT).show();

                        if (response.errorBody() == null) {
                            Log.d(TAG, "transfer Response Error. No message");
                        } else if (response.errorBody().string().contains("FOREIGN KEY")) {
                            Toast.makeText(DepositWithdrawActivity.this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                        } else if (response.errorBody().string().contains("CONSTRAINT")) {
                            Toast.makeText(DepositWithdrawActivity.this, "Số dư không đủ", Toast.LENGTH_SHORT).show();
                        }
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

    private void initComponents() {
        apiClient = new ApiClient();
        depositWithdrawService = apiClient.getDepositWithdrawService();
        userStatisticService = apiClient.getUserStatisticService();

        sotien = findViewById(R.id.et_transaction_value);
        btn = findViewById(R.id.btnTransfer);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount = DepositWithdrawActivity.this.sotien.getText().toString();

                if (amount.isEmpty()) {
                    Toast.makeText(view.getContext(), "Hãy nhập số tiền", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Double.parseDouble(amount) < 100000) {
                    Toast.makeText(view.getContext(), "Chỉ chuyển số tiền > 100.000đ", Toast.LENGTH_SHORT).show();
                    return;
                }

                int selectedId = radioGroup.getCheckedRadioButtonId();

                String stk = Utility.LIST_TK.get(0).getCmnd();


            }
        });
    }
}