package vn.edu.ptithcm.bankmanagement.ui.home;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import vn.edu.ptithcm.bankmanagement.R;
import vn.edu.ptithcm.bankmanagement.data.model.KhachHang;
import vn.edu.ptithcm.bankmanagement.data.model.ThongKeGD;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView rvRecentTransaction;
    private RecentTransactionAdapter recentTransactionAdapter;
    private List<ThongKeGD> transactions;
    private KhachHang user;
    private TextView tvUserCardName, tvUserCardDesc, tvBalanceValue, tvTranferValue;
    private ImageView ivUserAvatar;
    private NavigationView nvNav;
    private DrawerLayout dlDrawer;
    private AppCompatImageButton bOpenDrawer;
    private AppCompatImageButton bOpenProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        dlDrawer.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });
        bOpenDrawer.setOnClickListener(v ->
                dlDrawer.openDrawer(nvNav)
        );
//        tvUserCardName.setText(String.valueOf(user.getHo() + " " + user.getTen()));
//        tvUserCardDesc.setText(String.valueOf("account ending with " + user.getCmnd().substring(user.getCmnd().length() - 4, user.getCmnd().length())));
        transactions = new ArrayList<>();
        recentTransactionAdapter = new RecentTransactionAdapter(transactions);
        rvRecentTransaction.setLayoutManager(new LinearLayoutManager(this));
        rvRecentTransaction.setAdapter(recentTransactionAdapter);
    }

//    public void fakeData(){
//        user = new KhachHang("123123123", "Bùi Minh", "Tơ", "Thủ Đức, TP. Hồ Chí Minh", "nam", 0L, "0929123123");
//        transactions = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            ThongKeGD transaction = new ThongKeGD(0L, 0L, "", 1000L, 1000L);
//            transactions.add(transaction);
//        }
//    }
}