package vn.edu.ptithcm.bankmanagement.ui.transactionhistory;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import vn.edu.ptithcm.bankmanagement.R;
import vn.edu.ptithcm.bankmanagement.api.ApiClient;
import vn.edu.ptithcm.bankmanagement.api.UserStatisticService;
import vn.edu.ptithcm.bankmanagement.data.model.TaiKhoan;
import vn.edu.ptithcm.bankmanagement.data.model.ThongKeGD;
import vn.edu.ptithcm.bankmanagement.utility.Helper;
import vn.edu.ptithcm.bankmanagement.utility.Utility;

public class ActivityTransactionHistory extends AppCompatActivity {
    ApiClient apiClient;
    UserStatisticService userStatisticService;

    ListView listView;
    Spinner spinner;

    ArrayAdapter<String> listTkAdapter;
    List<String> listTk;

    TransactionAdapter listGdAdapter;
    List<ThongKeGD> listGd;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        initComponents();
    }

    private void initComponents() {
        apiClient = new ApiClient();
        userStatisticService = apiClient.getUserStatisticService();

        listView = findViewById(R.id.listView);
        spinner = findViewById(R.id.spinner);

        if (!Utility.LIST_TK.isEmpty()) {
            for (TaiKhoan tk : Utility.LIST_TK) {
                listTk.add(tk.getSoTK());
            }
            listTkAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listTk);
        }

        listTkAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(listTkAdapter);

        String selected = spinner.getSelectedItem().toString();

        listGd = Helper.doGetListTransactions(userStatisticService, selected);
        listGdAdapter = new TransactionAdapter(this, listGd);
    }
}