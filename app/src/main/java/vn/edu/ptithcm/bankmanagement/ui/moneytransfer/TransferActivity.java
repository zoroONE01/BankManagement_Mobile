package vn.edu.ptithcm.bankmanagement.ui.moneytransfer;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import java.util.HashMap;

import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.ptithcm.bankmanagement.R;
import vn.edu.ptithcm.bankmanagement.api.ApiClient;
import vn.edu.ptithcm.bankmanagement.api.MoneyTransferService;
import vn.edu.ptithcm.bankmanagement.api.UserStatisticService;
import vn.edu.ptithcm.bankmanagement.utility.Helper;
import vn.edu.ptithcm.bankmanagement.utility.Utility;

public class TransferActivity extends AppCompatActivity {
    final String TAG = TransferActivity.class.getName();

    EditText soTk;
    EditText soTien;
    Button btn;

    ApiClient apiClient;
    MoneyTransferService transferService;
    UserStatisticService userStatisticService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        initComponents();
    }

    private void initComponents() {
        apiClient = new ApiClient();
        transferService = apiClient.getMoneyTransferService();
        userStatisticService = apiClient.getUserStatisticService();


        soTk = findViewById(R.id.et_send_to);
        soTien = findViewById(R.id.et_transaction_value);
        btn = findViewById(R.id.btnTransfer);

        btn.setOnClickListener(view -> {
            // check fields
            String tk = soTk.getText().toString();
            String amount = soTien.getText().toString();

            String stk = Utility.LIST_TK.get(0).getSoTK();

            // TODO: check if tkId is the same as the logged user
            if (tk.isEmpty() || !tk.matches("^\\d{0,9}$") || tk.equals(stk)) {
                Toast.makeText(view.getContext(), "Hãy nhập lại số tài khoản", Toast.LENGTH_SHORT).show();
                return;
            } else if (amount.isEmpty()) {
                Toast.makeText(view.getContext(), "Hãy nhập số tiền", Toast.LENGTH_SHORT).show();
                return;
            }
            double sotien = Double.parseDouble(amount);

            if (sotien < Utility.MIN_TRANSFER_AMOUNT) {
                Toast.makeText(view.getContext(), "Chỉ chuyển số tiền > " + Helper.showGia(Utility.MIN_TRANSFER_AMOUNT) + "đ", Toast.LENGTH_SHORT).show();
                return;
            }
            doTransfer(stk, amount, tk);
        });
    }

    void doTransfer(String id, String amount, String otherId) {
        HashMap<String, String> values = new HashMap<>();
        values.put(MoneyTransferService.key1, id);
        values.put(MoneyTransferService.key2, amount);
        values.put(MoneyTransferService.key3, otherId);

        Call<JsonObject> call = transferService.doTransfer(Utility.COOKIE, values);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "transfer Response: " + (response.body() != null ? response.body().toString() : "transfer response ok"));
                    // TODO: show transfer success activity
                } else if (response.code() == 401) {
                    // TODO go back to login
                    Log.d(TAG, "transfer 401");
                } else {
                    try {
                        if (response.errorBody() == null) {
                            Log.d(TAG, "transfer Response Error. No message");
                        } else if (response.errorBody().string().contains("FOREIGN KEY")) {
                            Toast.makeText(TransferActivity.this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "tai khoan k ton tai");
                        } else if (response.errorBody().string().contains("CONSTRAINT")) {
                            Toast.makeText(TransferActivity.this, "Tài khoản không đủ số dư", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "tai khoan k du tien");
                        }
                        Log.d(TAG, "transfer Response Error: " + response.errorBody().charStream());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Log.d(TAG, "Transfer Failure");
                t.printStackTrace();
            }
        });
    }
}