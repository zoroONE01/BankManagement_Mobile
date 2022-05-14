package vn.edu.ptithcm.bankmanagement.ui.moneytransfer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import vn.edu.ptithcm.bankmanagement.R;
import vn.edu.ptithcm.bankmanagement.api.ApiClient;
import vn.edu.ptithcm.bankmanagement.api.MoneyTransferService;
import vn.edu.ptithcm.bankmanagement.api.UserStatisticService;
import vn.edu.ptithcm.bankmanagement.data.model.ThongKeGD;
import vn.edu.ptithcm.bankmanagement.utility.Helper;

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
        soTien = findViewById(R.id.fieldAmount);
        btn = findViewById(R.id.btnTransfer);

        btn.setOnClickListener(view -> {
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
            List<ThongKeGD> list = Helper.doGetListTransactions(apiClient.getUserStatisticService(), "000000000");
            Log.d(TAG, "onClick: " + list);
        });
    }
}