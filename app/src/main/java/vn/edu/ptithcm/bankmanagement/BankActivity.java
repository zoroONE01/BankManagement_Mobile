package vn.edu.ptithcm.bankmanagement;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import vn.edu.ptithcm.bankmanagement.api.ApiClient;
import vn.edu.ptithcm.bankmanagement.databinding.ActivityBankBinding;
import vn.edu.ptithcm.bankmanagement.utility.Helper;

public class BankActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityBankBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBankBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        ApiClient api = new ApiClient();
        Helper.doLogin(api.getUserService(), "adminCN1", "Admin1234.");

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_bank);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_bank);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}