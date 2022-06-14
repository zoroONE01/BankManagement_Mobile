package vn.edu.ptithcm.bankmanagement.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import vn.edu.ptithcm.bankmanagement.R;
import vn.edu.ptithcm.bankmanagement.databinding.CreateAccountBinding;
import vn.edu.ptithcm.bankmanagement.utility.Utility;

public class SignUpFragment extends Fragment {

    private LoginViewModel loginViewModel;
    private CreateAccountBinding binding;
    FirebaseAuth firebaseAuth;
    Button registerBt;
    EditText cmndEd;
    EditText hoEd;
    EditText tenEd;
    EditText taiKhoanEd;
    EditText matKhauEd;
    EditText matKhauXacNhanEd;
    ProgressBar loadingProgressBar;
    FirebaseUser firebaseUser;

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = CreateAccountBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        registerBt = binding.bRegister;
        cmndEd = binding.etCmnd;
        hoEd = binding.tvHo;
        tenEd = binding.etTen;
        taiKhoanEd = binding.etEmail;
        matKhauEd = binding.etPassword;
        matKhauXacNhanEd = binding.etConfirmPassword;
        loadingProgressBar = binding.progressbar;
        final Button btChangeLogin = binding.bChangeLogin;
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory(getContext()))
                .get(LoginViewModel.class);
        loginViewModel.getRegisterResult().observe(getViewLifecycleOwner(), new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                //loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
            }
        });
        registerBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                if (Utility.verifiedBy.equals("google")) {
                    loginViewModel.register(cmndEd.getText().toString(),
                            hoEd.getText().toString(), tenEd.getText().toString(),
                            taiKhoanEd.getText().toString(), "", firebaseUser.getPhotoUrl().toString());
                    loadingProgressBar.setVisibility(View.GONE);
                } else {
                    firebaseAuth.createUserWithEmailAndPassword(taiKhoanEd.getText().toString(), matKhauEd.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        loginViewModel.register(cmndEd.getText().toString(),
                                                hoEd.getText().toString(), tenEd.getText().toString(),
                                                taiKhoanEd.getText().toString(), "", "");
                                        loadingProgressBar.setVisibility(View.GONE);
                                    } else {

                                        // Registration failed
                                        Toast.makeText(
                                                getActivity(),
                                                "Registration by firebase failed!!"
                                                        + " Please try again later",
                                                Toast.LENGTH_LONG)
                                                .show();
                                    }
                                }
                            });
                }

            }
        });
        btChangeLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SignUpFragment.this).navigate(R.id.action_SignUpFragment_to_SignInFragment);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Utility.verifiedBy.equals("google")) {
            firebaseUser = firebaseAuth.getCurrentUser();

            String fullname = firebaseUser.getDisplayName();
            //Nguyen Manh Tuong
            hoEd.setText(fullname.substring(0, fullname.lastIndexOf(" "))); //Nguyen Manh
            tenEd.setText(fullname.substring(fullname.lastIndexOf(" ") + 1)); //Tuong
            taiKhoanEd.setText(firebaseUser.getEmail());

            hoEd.setEnabled(false);
            tenEd.setEnabled(false);
            taiKhoanEd.setEnabled(false);
            matKhauEd.setVisibility(View.GONE);
            matKhauXacNhanEd.setVisibility(View.GONE);
        } else {
            hoEd.setEnabled(true);
            tenEd.setEnabled(true);
            taiKhoanEd.setEnabled(true);
            matKhauEd.setVisibility(View.VISIBLE);
            matKhauXacNhanEd.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        // TODO: Use the ViewModel
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(getContext().getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        }
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(
                    getContext().getApplicationContext(),
                    errorString,
                    Toast.LENGTH_LONG).show();
        }
    }
}