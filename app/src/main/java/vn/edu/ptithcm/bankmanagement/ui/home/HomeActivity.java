package vn.edu.ptithcm.bankmanagement.ui.home;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setUpUi();
    }
    public void setUpUi(){
        rvRecentTransaction = findViewById(R.id.rv_recent_transaction);
        tvUserCardName = findViewById(R.id.tv_user_card_name);
        tvUserCardDesc = findViewById(R.id.tv_user_card_desc);
        tvBalanceValue = findViewById(R.id.tv_balance_value);
        tvTranferValue = findViewById(R.id.tv_tranfer_value);
        ivUserAvatar = findViewById(R.id.iv_user_avatar);

        fakeData();
        tvUserCardName.setText(String.valueOf(user.getHo() + " " + user.getTen()));
        tvUserCardDesc.setText(String.valueOf("account ending with " + user.getCmnd().substring(user.getCmnd().length() - 4, user.getCmnd().length())));
//        tvBalanceValue.setText();
//        tvTranferValue.setText(); //Todo: Số dư với số tiền giao dịch
        recentTransactionAdapter = new RecentTransactionAdapter(transactions);
        rvRecentTransaction.setLayoutManager(new LinearLayoutManager(this));
        rvRecentTransaction.setAdapter(recentTransactionAdapter);
    }

    public void fakeData(){
        user = new KhachHang("123123123", "Bùi Minh", "Tơ", "Thủ Đức, TP. Hồ Chí Minh", "nam", 0L, "0929123123");
        transactions = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ThongKeGD transaction = new ThongKeGD(0L, 0L, "", 1000L, 1000L);
            transactions.add(transaction);
        }
    }
}