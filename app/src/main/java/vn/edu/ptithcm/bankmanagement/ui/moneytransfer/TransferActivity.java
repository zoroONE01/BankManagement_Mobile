package vn.edu.ptithcm.bankmanagement.ui.moneytransfer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.ptithcm.bankmanagement.R;
import vn.edu.ptithcm.bankmanagement.api.ApiClient;
import vn.edu.ptithcm.bankmanagement.api.MoneyTransferService;
import vn.edu.ptithcm.bankmanagement.utility.Utility;

public class TransferActivity extends AppCompatActivity {
    final String TAG = TransferActivity.class.getName();
    EditText soTk;
    EditText soTien;
    Button btn;

    ApiClient apiClient;
    MoneyTransferService transferService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        initComponents();
    }

    void doTransfer(String id, String amount, String otherId) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // get session id in preference
        String sessionId = prefs.getString(Utility.PREF_COOKIES, "");

        if (sessionId.isEmpty()) {
            // TODO: go back to login activity
            Toast.makeText(TransferActivity.this, "Đã có lỗi xảy ra", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Session id empty");
            return;
        }

        HashMap<String, String> values = new HashMap<>();
        values.put(MoneyTransferService.key1, id);
        values.put(MoneyTransferService.key2, amount);
        values.put(MoneyTransferService.key3, otherId);

        Call<JsonObject> call = transferService.doTransfer(sessionId, values);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "transfer Response: " + (response.body() != null ? response.body().toString() : "dpwd response ok"));
                    // TODO: show transfer success activity
                } else if (response.code() == 401) {
                    // Handle unauthorized
                    // TODO go back to login
                    Log.d(TAG, "transfer 401");
                } else {
                    try {
                        Toast.makeText(TransferActivity.this, "Chuyển tiền không thành công", Toast.LENGTH_SHORT).show();
                        if (response.errorBody() == null) {
                            Log.d(TAG, "transfer Response Error. No message");
                        } else if (response.errorBody().string().contains("FOREIGN KEY")) {
                            Toast.makeText(TransferActivity.this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                        } else if (response.errorBody().string().contains("CONSTRAINT")) {
                            Toast.makeText(TransferActivity.this, "Số dư không đủ", Toast.LENGTH_SHORT).show();
                        }
                        Log.d(TAG, "transfer Response Error: " + response.errorBody().charStream());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Toast.makeText(TransferActivity.this, "Chuyển tiền thất bại", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Transfer Failure");
            }
        });
    }

    private void initComponents() {
        apiClient = new ApiClient(this);
        transferService = apiClient.getMoneyTransferService();

        soTk = findViewById(R.id.fieldStk);
        soTien = findViewById(R.id.fieldAmount);
        btn = findViewById(R.id.btnTransfer);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check fields
                String tk = soTk.getText().toString();
                String amount = soTien.getText().toString();

                // TODO: check if tkId is the same as the logged user
                if (tk.isEmpty() || tk.matches("^\\d{9}$") || tk.equals("000000000")) {
                    Toast.makeText(view.getContext(), "Hãy nhập lại số tài khoản", Toast.LENGTH_SHORT).show();
                    return;
                } else if (amount.isEmpty()) {
                    Toast.makeText(view.getContext(), "Hãy nhập số tiền", Toast.LENGTH_SHORT).show();
                    return;
                }
                double sotien = Double.parseDouble(amount);

                if (sotien < 100000) {
                    Toast.makeText(view.getContext(), "Chỉ chuyển số tiền > 100.000đ", Toast.LENGTH_SHORT).show();
                    return;
                }
                // TODO: get logged user account id
                String stk = "000000000";
                doTransfer(stk, amount, tk);
            }
        });
    }
}