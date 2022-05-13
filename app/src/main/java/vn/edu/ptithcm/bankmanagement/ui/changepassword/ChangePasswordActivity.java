package vn.edu.ptithcm.bankmanagement.ui.changepassword;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import vn.edu.ptithcm.bankmanagement.R;
import vn.edu.ptithcm.bankmanagement.api.ApiClient;
import vn.edu.ptithcm.bankmanagement.api.ChangePasswordService;
import vn.edu.ptithcm.bankmanagement.api.DepositWithdrawService;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText matKhauCu;
    EditText matKhauMoi;
    EditText nhapLaiMatKhauMoi;
    Button btn;

    ApiClient apiClient;
    ChangePasswordService depositWithdrawService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
    }
}