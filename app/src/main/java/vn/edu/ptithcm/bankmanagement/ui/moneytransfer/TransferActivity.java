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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.ptithcm.bankmanagement.R;
import vn.edu.ptithcm.bankmanagement.api.ApiClient;
import vn.edu.ptithcm.bankmanagement.api.MoneyTransferService;
import vn.edu.ptithcm.bankmanagement.api.UserService;
import vn.edu.ptithcm.bankmanagement.api.UserStatisticService;
import vn.edu.ptithcm.bankmanagement.data.model.TaiKhoan;
import vn.edu.ptithcm.bankmanagement.data.model.ThongKeGD;
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
        doLogin("admin", "admin");
    }

    void doLogin(String username, String password) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        UserService userService = apiClient.getUserService();

        Call<JsonObject> call = userService.login(username, password);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "login Response: " + response.body().toString());
                    Log.d(TAG, "headers values: " + response.headers());

//                    String sessionId = response.headers().get("Set-Cookie");
                    Utility.COOKIE = response.headers().get("Set-Cookie");

                    if (Utility.COOKIE == null || Utility.COOKIE.isEmpty()) {
                        Log.d(TAG, "Error: no session id");

                        return;
                    }

                    // save session id in preference
//                    SharedPreferences.Editor editor = prefs.edit();
//                    editor.putString(Utility.PREF_COOKIES, sessionId);
//                    editor.apply();
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

    void doTransfer(String id, String amount, String otherId) {
        if (Utility.COOKIE.isEmpty()) {
            // TODO: go back to login activity
            Toast.makeText(TransferActivity.this, "Đã có lỗi xảy ra", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Session id empty");
            return;
        }

        HashMap<String, String> values = new HashMap<>();
        values.put(MoneyTransferService.key1, id);
        values.put(MoneyTransferService.key2, amount);
        values.put(MoneyTransferService.key3, otherId);

        Call<JsonObject> call = transferService.doTransfer(Utility.COOKIE, values);

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

    void doGetAllTk(String cmnd) {
        Log.d(TAG, "session id: " + Utility.COOKIE);

        if (Utility.LIST_TK.isEmpty()) {
            Call<JsonArray> call = userStatisticService.getAllTk(Utility.COOKIE, cmnd);

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
        } else {
            Log.d(TAG, "list tk: " + Utility.LIST_TK);
        }
    }

    private void initComponents() {
        apiClient = new ApiClient(this);
        transferService = apiClient.getMoneyTransferService();
        userStatisticService = apiClient.getUserStatisticService();

        soTk = findViewById(R.id.fieldStk);
        soTien = findViewById(R.id.fieldAmount);
        btn = findViewById(R.id.btnTransfer);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                // check fields
//                String tk = soTk.getText().toString();
//                String amount = soTien.getText().toString();
//
//                // TODO: check if tkId is the same as the logged user
//                if (tk.isEmpty() || tk.matches("^\\d{9}$") || tk.equals("000000000")) {
//                    Toast.makeText(view.getContext(), "Hãy nhập lại số tài khoản", Toast.LENGTH_SHORT).show();
//                    return;
//                } else if (amount.isEmpty()) {
//                    Toast.makeText(view.getContext(), "Hãy nhập số tiền", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                double sotien = Double.parseDouble(amount);
//
//                if (sotien < 100000) {
//                    Toast.makeText(view.getContext(), "Chỉ chuyển số tiền > 100.000đ", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                // TODO: get logged user account id
//                String stk = "000000000";
//                doTransfer(stk, amount, tk);
//                doGetAllTk("123456789");
                List<ThongKeGD> list = Helper.doGetListTransactions(view.getContext(), apiClient, "000000000");
                Log.d(TAG, "onClick: " + list);
            }
        });
    }
}