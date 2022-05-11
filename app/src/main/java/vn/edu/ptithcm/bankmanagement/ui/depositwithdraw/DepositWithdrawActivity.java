package vn.edu.ptithcm.bankmanagement.ui.depositwithdraw;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

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
import vn.edu.ptithcm.bankmanagement.model.TaiKhoan;
import vn.edu.ptithcm.bankmanagement.utility.Utility;

public class DepositWithdrawActivity extends AppCompatActivity {
    private String TAG = DepositWithdrawActivity.class.getName();

    String sessionId;

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

        Call<JsonObject> call = depositWithdrawService.depositOrWithdraw(sessionId, values);

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
        apiClient = new ApiClient(this);
        depositWithdrawService = apiClient.getDepositWithdrawService();
        userStatisticService = apiClient.getUserStatisticService();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // get session id in preference
        sessionId = prefs.getString(Utility.PREF_COOKIES, "");

        if (sessionId.isEmpty()) {
            // TODO: go back to login activity
            return;
        }

        // get list tk
        if (Utility.LIST_TK.isEmpty()) {
            Call<List<TaiKhoan>> call = userStatisticService.getAllTk(sessionId, Utility.USER_CMND);

            call.enqueue(new Callback<List<TaiKhoan>>() {
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "list tk Response: " + (response.body() != null ? response.body().toString() : "dpwd response ok"));

                        List<TaiKhoan> listTK = (List<TaiKhoan>) response.body();
                    } else if (response.code() == HttpsURLConnection.HTTP_UNAUTHORIZED) {
                        // Handle unauthorized
                        // TODO go back to login
                        Log.d(TAG, "list tk 401");
                    } else {
                        try {
                            if (response.errorBody() == null) {
                                Log.d(TAG, "transfer Response Error. No message");
                            } else if (response.errorBody().string().contains("FOREIGN")) {
                                Toast.makeText(DepositWithdrawActivity.this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                            } else if (response.errorBody().string().contains("CHECK")) {
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

        sotien = findViewById(R.id.fieldAmount);
        radioGroup = findViewById(R.id.radioGroup);
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

                // TODO: get stk from logged user
                String stk = "00000000000";

                if (selectedId == R.id.radioGT) {
                    doDepositOrWithdraw(stk, DepositWithdrawActivity.this.sotien.getText().toString(), "GT");
                } else {
                    doDepositOrWithdraw(stk, DepositWithdrawActivity.this.sotien.getText().toString(), "RT");
                }
            }
        });
    }
}