package vn.edu.ptithcm.bankmanagement.ui.transactionhistory;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.ptithcm.bankmanagement.R;
import vn.edu.ptithcm.bankmanagement.api.ApiClient;
import vn.edu.ptithcm.bankmanagement.api.ProfileService;
import vn.edu.ptithcm.bankmanagement.api.UserStatisticService;
import vn.edu.ptithcm.bankmanagement.data.model.TaiKhoan;
import vn.edu.ptithcm.bankmanagement.data.model.ThongKeGD;
import vn.edu.ptithcm.bankmanagement.ui.home.RecentTransactionAdapter;
import vn.edu.ptithcm.bankmanagement.utility.Helper;
import vn.edu.ptithcm.bankmanagement.utility.Utility;

public class ActivityTransactionHistory extends AppCompatActivity {
    final String TAG = ActivityTransactionHistory.class.getName();

    ApiClient apiClient;
    UserStatisticService userStatisticService;


    private RecyclerView rvRecentTransaction;
    private List<ThongKeGD> transactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        initComponents();
    }

    private void initComponents() {
        apiClient = new ApiClient();
        userStatisticService = apiClient.getUserStatisticService();

        rvRecentTransaction = findViewById(R.id.rv_recent_transaction);
        rvRecentTransaction.setLayoutManager(new LinearLayoutManager(this));

        if (!Utility.LIST_TK.isEmpty()) {
            doGetListTransactions(userStatisticService, Utility.LIST_TK.get(0).getSoTK());
        }
    }

    void doGetListTransactions(UserStatisticService userStatisticService, String stk) {
        List<ThongKeGD> list = new ArrayList<>();

        Call<JsonArray> call = userStatisticService.getTransactionHistory(Utility.COOKIE, stk, "2011-1-1", "2031-1-1");

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "statistic Response: " + (response.body() != null ? response.body().toString() : "statistic response ok"));

                    JsonArray array = (JsonArray) response.body();

                    for (JsonElement ele : array) {
                        JsonObject e = ele.getAsJsonObject();
                        Log.d(TAG, "tk: " + e.toString());

                        ThongKeGD tk = new ThongKeGD(
                                e.get("balanceBefore").getAsDouble(),
                                e.get("ngayGD").getAsLong(),
                                e.get("loaiGD").getAsString(),
                                e.get("soTien").getAsDouble(),
                                e.get("balanceAfter").getAsDouble());
                        list.add(tk);
                    }
                    transactions = list;
                    rvRecentTransaction.setAdapter(new RecentTransactionAdapter(transactions));
                } else if (response.code() == HttpsURLConnection.HTTP_UNAUTHORIZED) {
                    // TODO go back to login
                    Log.d(TAG, "list tk 401");
                } else {
                    try {
                        if (response.errorBody() == null) {
                            Log.d(TAG, "list transfer Response Error. No message");
                        } else if (response.errorBody().string().contains("FOREIGN")) {
                            Log.d(TAG, "so tk k ton tai");
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
    }
}