package vn.edu.ptithcm.bankmanagement.ui.home;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
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
import vn.edu.ptithcm.bankmanagement.data.model.KhachHang;
import vn.edu.ptithcm.bankmanagement.data.model.TaiKhoan;
import vn.edu.ptithcm.bankmanagement.data.model.ThongKeGD;
import vn.edu.ptithcm.bankmanagement.ui.depositwithdraw.DepositWithdrawActivity;
import vn.edu.ptithcm.bankmanagement.ui.statistic.StatisticActivity;
import vn.edu.ptithcm.bankmanagement.ui.transactionhistory.ActivityTransactionHistory;
import vn.edu.ptithcm.bankmanagement.utility.Helper;
import vn.edu.ptithcm.bankmanagement.utility.Image;
import vn.edu.ptithcm.bankmanagement.utility.Utility;

public class HomeActivity extends AppCompatActivity {
    final String TAG = HomeActivity.class.getName();

    private RecyclerView rvRecentTransaction;
    private RecentTransactionAdapter recentTransactionAdapter;
    private List<ThongKeGD> transactions = new ArrayList<>();
    private KhachHang user;
    private TextView tvUserCardName, tvUserCardDesc, tvBalanceValue, tvTranferValue;
    private ImageView ivUserAvatar;
    private NavigationView nvNav;
    private DrawerLayout dlDrawer;
    private AppCompatImageButton bOpenDrawer;
    private AppCompatImageButton bOpenProfile;

    ApiClient apiClient;
    ProfileService profileService;
    UserStatisticService userStatisticService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        apiClient = new ApiClient();
        profileService = apiClient.getProfileService();
        userStatisticService = apiClient.getUserStatisticService();

        loadCustomer(Utility.USER.getKhachHangID());
        loadThongTinTaiKhoan(Utility.USER.getKhachHangID());

        setUpUi();
    }

    public void setUpUi() {
        rvRecentTransaction = findViewById(R.id.rv_recent_transaction);
        tvUserCardName = findViewById(R.id.tv_user_card_name);
        tvUserCardDesc = findViewById(R.id.tv_user_card_desc);
        tvBalanceValue = findViewById(R.id.tv_balance_value);
        tvTranferValue = findViewById(R.id.tv_tranfer_value);
        ivUserAvatar = findViewById(R.id.iv_user_avatar);
        dlDrawer = findViewById(R.id.dl_drawer);
        nvNav = findViewById(R.id.nv_nav_view);
        bOpenDrawer = findViewById(R.id.b_menu);
        bOpenProfile = findViewById(R.id.action_show_more);

        nvNav.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        menuItem.setChecked(!menuItem.isChecked());

                        // close drawer when item is tapped
                        dlDrawer.closeDrawers();

                        Intent intent;
                        switch (menuItem.getItemId()) {
                            case R.id.action_show_home:
                                // TODO: show home?
                                break;
                            case R.id.action_trans_history:
                                intent = new Intent(HomeActivity.this, ActivityTransactionHistory.class);
                                startActivity(intent);
                                break;
                            case R.id.action_recharge:
                                intent = new Intent(HomeActivity.this, DepositWithdrawActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.action_statistic:
                                intent = new Intent(HomeActivity.this, StatisticActivity.class);
                                startActivity(intent);
                                break;
                        }
                        return true;
                    }
                });
        bOpenDrawer.setOnClickListener(v -> dlDrawer.openDrawer(nvNav));

        recentTransactionAdapter = new RecentTransactionAdapter(transactions);
        rvRecentTransaction.setLayoutManager(new LinearLayoutManager(this));
        rvRecentTransaction.setAdapter(recentTransactionAdapter);

        Image.doLoadImage(apiClient.getImageService(), Utility.USER.getImageUrl(), ivUserAvatar);
    }

    void loadCustomer(String cmnd) {
        Log.d(TAG, "cmnd: " + cmnd);
        if (Utility.COOKIE.isEmpty()) {
            Toast.makeText(this, "Error: no session id", Toast.LENGTH_SHORT).show();
            return;
        }

        profileService.getCustomer(Utility.COOKIE, cmnd).enqueue(new Callback<KhachHang>() {

            @Override
            public void onResponse(@NonNull Call<KhachHang> call, @NonNull retrofit2.Response<KhachHang> response) {
                if (response.body() != null) {
                    user = response.body();

                    tvUserCardName.setText(String.valueOf(user.getHo() + " " + user.getTen()));
                    tvUserCardDesc.setText(String.valueOf("account ending with " + user.getCmnd().substring(user.getCmnd().length() - 4, user.getCmnd().length())));
                } else {
                    Log.d(TAG, "get customer response body fail");
                }
            }

            @Override
            public void onFailure(@NonNull Call<KhachHang> call, Throwable t) {
                Log.d(TAG, "Failure " + t.getMessage());
            }
        });
    }

    void loadThongTinTaiKhoan(String cmnd) {
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
                                e.get("ngayMoTK").getAsLong());

                        Utility.LIST_TK.add(tk);
                        Log.d(TAG, tk.toString());
                    }

                    if (!Utility.LIST_TK.isEmpty()) {
                        String sodu = Helper.showGia(Utility.LIST_TK.get(0).getSoDu());
                        tvBalanceValue.setText(String.valueOf(sodu + " đ"));

                        doGetListTransactions(userStatisticService, Utility.LIST_TK.get(0).getSoTK());
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
    }

    void doGetListTransactions(UserStatisticService userStatisticService, String stk) {
        List<ThongKeGD> list = new ArrayList<>();

        Call<JsonArray> call = userStatisticService.getTransactionHistory(Utility.COOKIE, stk, "2011-1-1", "2031-1-1");

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "statistic Response: " + (response.body() != null ? response.body().toString() : "dpwd response ok"));

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

                    String tongtien = Helper.showGia(Helper.getTongTienGiaoDich(list));
                    tvTranferValue.setText(String.valueOf(tongtien + " đ"));
                } else if (response.code() == HttpsURLConnection.HTTP_UNAUTHORIZED) {
                    // Handle unauthorized
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