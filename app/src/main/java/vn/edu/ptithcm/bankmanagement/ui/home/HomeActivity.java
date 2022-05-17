package vn.edu.ptithcm.bankmanagement.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import vn.edu.ptithcm.bankmanagement.BankActivity;
import vn.edu.ptithcm.bankmanagement.R;
import vn.edu.ptithcm.bankmanagement.api.ApiClient;
import vn.edu.ptithcm.bankmanagement.api.ProfileService;
import vn.edu.ptithcm.bankmanagement.api.UserStatisticService;
import vn.edu.ptithcm.bankmanagement.data.model.KhachHang;
import vn.edu.ptithcm.bankmanagement.data.model.TaiKhoan;
import vn.edu.ptithcm.bankmanagement.data.model.ThongKeGD;
import vn.edu.ptithcm.bankmanagement.ui.depositwithdraw.DepositWithdrawActivity;
import vn.edu.ptithcm.bankmanagement.ui.moneytransfer.TransferActivity;
import vn.edu.ptithcm.bankmanagement.ui.profile.ProfileActivity;
import vn.edu.ptithcm.bankmanagement.ui.statistic.StatisticActivity;
import vn.edu.ptithcm.bankmanagement.ui.transactionhistory.ActivityTransactionHistory;
import vn.edu.ptithcm.bankmanagement.utility.Helper;
import vn.edu.ptithcm.bankmanagement.utility.Image;
import vn.edu.ptithcm.bankmanagement.utility.Utility;

public class HomeActivity extends AppCompatActivity {
    final String TAG = HomeActivity.class.getName();

    private RecyclerView rvRecentTransaction;
    private RecentTransactionAdapter recentTransactionAdapter;
    private KhachHang user;
    private TextView tvUserCardName, tvUserCardDesc, tvBalanceValue, tvTranferValue;
    private ImageView ivUserAvatar, ivAvatarNav;
    private NavigationView nvNav;
    private DrawerLayout dlDrawer;
    private AppCompatImageButton bOpenDrawer;
    private AppCompatImageButton bOpenProfile;


    androidx.appcompat.widget.AppCompatImageButton nap, chuyen, rut;

    // drawer
    View header;
    TextView name;

    View card;

    Button logout;

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

        setUpUi();
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadThongTinTaiKhoan(Utility.USER.getKhachHangID());
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

        nap = findViewById(R.id.b_recharge);
        chuyen = findViewById(R.id.b_tranfer);
        rut = findViewById(R.id.b_withdraw);

        logout = findViewById(R.id.b_logout);
        logout.setOnClickListener(view -> {
            Intent i = new Intent(HomeActivity.this, BankActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);

            startActivity(i);
        });

        card = findViewById(R.id.ll_user_card);

        header = nvNav.getHeaderView(0);
        name = header.findViewById(R.id.tv_user_card_name);
        ivAvatarNav = header.findViewById(R.id.iv_user_avatar);

        nvNav.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        dlDrawer.closeDrawers();

                        Intent intent;
                        switch (menuItem.getItemId()) {
                            case R.id.action_trans_history:
                                intent = new Intent(HomeActivity.this, ActivityTransactionHistory.class);
                                startActivity(intent);
                                break;
                            case R.id.action_recharge:
                                intent = new Intent(HomeActivity.this, DepositWithdrawActivity.class);
                                intent.putExtra("INTENT", 0);
                                startActivity(intent);
                                break;
                            case R.id.action_withdraw:
                                intent = new Intent(HomeActivity.this, DepositWithdrawActivity.class);
                                intent.putExtra("INTENT", 1);
                                startActivity(intent);
                                break;
                            case R.id.action_tranfer:
                                intent = new Intent(HomeActivity.this, TransferActivity.class);
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

        rut.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, DepositWithdrawActivity.class);
            intent.putExtra("INTENT", 1);
            startActivity(intent);
        });

        nap.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, DepositWithdrawActivity.class);
            intent.putExtra("INTENT", 0);
            startActivity(intent);
        });

        chuyen.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, TransferActivity.class);
            startActivity(intent);
        });

        card.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        recentTransactionAdapter = new RecentTransactionAdapter(new ArrayList<>());
        rvRecentTransaction.setLayoutManager(new LinearLayoutManager(this));
        rvRecentTransaction.setAdapter(recentTransactionAdapter);
    }

    void loadCustomer(String cmnd) {
        if (Utility.COOKIE.isEmpty()) {
            Toast.makeText(this, "Lỗi: không có COOKIE", Toast.LENGTH_SHORT).show();
            return;
        }

        profileService.getCustomer(Utility.COOKIE, cmnd).enqueue(new Callback<KhachHang>() {
            @Override
            public void onResponse(@NonNull Call<KhachHang> call, @NonNull retrofit2.Response<KhachHang> response) {
                if (response.body() != null) {
                    user = response.body();

                    Image.doLoadImage(apiClient.getImageService(), Utility.USER.getImageUrl(), ivUserAvatar);
                    Image.doLoadImage(apiClient.getImageService(), Utility.USER.getImageUrl(), ivAvatarNav);
                    tvUserCardName.setText(String.valueOf(user.getHo() + " " + user.getTen()));
                    tvUserCardDesc.setText(String.valueOf("Số tài khoản: " + Utility.LIST_TK.get(0).getSoTK()));

                    name.setText(String.valueOf(user.getHo() + " " + user.getTen()));
                } else {
                    Log.d(TAG, "get customer info null");
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

                        TaiKhoan tk = new TaiKhoan(
                                e.get("soTK").getAsString(),
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

                        loadCustomer(Utility.USER.getKhachHangID());
                        doGetListTransactions(userStatisticService, Utility.LIST_TK.get(0).getSoTK());
                    }
                } else if (response.code() == HttpsURLConnection.HTTP_UNAUTHORIZED) {
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
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "get list transaction Response: " + (response.body() != null ? response.body().toString() : "dpwd response ok"));

                    JsonArray array = (JsonArray) response.body();

                    for (JsonElement ele : array) {
                        JsonObject e = ele.getAsJsonObject();
                        Log.d(TAG, "tk: " + e.toString());

                        ThongKeGD tk = new ThongKeGD(
                                e.get("source").getAsString(),
                                e.get("balanceBefore").getAsDouble(),
                                e.get("ngayGD").getAsLong(),
                                e.get("loaiGD").getAsString(),
                                e.get("soTien").getAsDouble(),
                                e.get("balanceAfter").getAsDouble());
                        list.add(tk);
                    }
                    Log.d(TAG, "onResponse: " + list);

                    list.sort((t, other) -> -1 * t.getNgayGD().compareTo(other.getNgayGD()));

                    recentTransactionAdapter.setList(list.size() >= 3 ? list.subList(0, 3) : list);

                    String tongtien = Helper.showGia(Helper.getTongTienGiaoDich(list));
                    tvTranferValue.setText(String.valueOf(tongtien + "đ"));
                } else if (response.code() == HttpsURLConnection.HTTP_UNAUTHORIZED) {
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