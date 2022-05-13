package vn.edu.ptithcm.bankmanagement;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import vn.edu.ptithcm.bankmanagement.api.ApiClient;
import vn.edu.ptithcm.bankmanagement.api.DepositWithdrawService;
import vn.edu.ptithcm.bankmanagement.api.MoneyTransferService;
import vn.edu.ptithcm.bankmanagement.api.UserService;
import vn.edu.ptithcm.bankmanagement.utility.Helper;

public class MainActivity extends AppCompatActivity {
    static String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
    }
}