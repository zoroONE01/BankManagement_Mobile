package vn.edu.ptithcm.bankmanagement.ui.login;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.JsonObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.ptithcm.bankmanagement.R;
import vn.edu.ptithcm.bankmanagement.api.ApiClient;
import vn.edu.ptithcm.bankmanagement.api.LoginService;
import vn.edu.ptithcm.bankmanagement.data.model.LoggedInUser;
import vn.edu.ptithcm.bankmanagement.databinding.FragmentLoginBinding;
import vn.edu.ptithcm.bankmanagement.firebase.MyFirebaseMessagingService;
import vn.edu.ptithcm.bankmanagement.ui.home.HomeActivity;
import vn.edu.ptithcm.bankmanagement.utility.Utility;

public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;
    private FragmentLoginBinding binding;
    // Initialize variables
    SignInButton btSignIn;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth firebaseAuth;
    LoginService loginService;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory(getContext()))
                .get(LoginViewModel.class);
        ApiClient apiClient = new ApiClient();
        loginService = apiClient.getLoginService();

        final EditText usernameEditText = binding.etEmail;
        final EditText passwordEditText = binding.etPassword;
        final Button loginButton = binding.bSignIn;
        final Button btChangeLogin = binding.bChangeLogin;
        Button forgotPwdBtn = binding.bForgotPassword;
        btSignIn = binding.btSignIn;
        ProgressBar loadingProgressBar = binding.progressbar;

        setGooglePlusButtonText(btSignIn, "Đăng nhập với Google");

        SharedPreferences sh = getActivity().getSharedPreferences(MyFirebaseMessagingService.firebaseToken, getContext().MODE_PRIVATE);

        String firebaseToken = sh.getString("token", "khong lay duoc token");
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken("917260150782-voiko8j72gegt5i8338rlupc6gpbr1j5.apps.googleusercontent.com")
                .requestEmail()
                .build();

        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(getActivity()
                , googleSignInOptions);
        firebaseAuth = FirebaseAuth.getInstance();
        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // When request code is equal to 100
                        // Initialize task
                        Log.d("----googleLogin", result.getData().toString());
                        Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn
                                .getSignedInAccountFromIntent(result.getData());

                        // check condition
                        if (signInAccountTask.isSuccessful()) {
                            // Initialize sign in account
                            try {
                                // Initialize sign in account
                                GoogleSignInAccount googleSignInAccount = signInAccountTask
                                        .getResult(ApiException.class);
                                // Check condition
                                if (googleSignInAccount != null) {
                                    // When sign in account is not equal to null
                                    // Initialize auth credential
                                    AuthCredential authCredential = GoogleAuthProvider
                                            .getCredential(googleSignInAccount.getIdToken()
                                                    , null);
                                    // Check credential
                                    firebaseAuth.signInWithCredential(authCredential)
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    // Check condition
                                                    if (task.isSuccessful()) {
                                                        // Display Toast
                                                        firebaseAuth = FirebaseAuth.getInstance();

                                                        // Initialize firebase user
                                                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                                                        // Check condition
                                                        if (firebaseUser != null) {
                                                            Log.d("----googleLogin", firebaseUser.getPhotoUrl().toString());
                                                            Log.d("----googleLogin", firebaseUser.getDisplayName());
                                                            loadingProgressBar.setVisibility(View.GONE);
                                                            Utility.verifiedBy = "google";
                                                            try {
                                                                HashMap<String, String> values = new HashMap<>();
                                                                values.put("taiKhoan", firebaseUser.getEmail());

                                                                loginService.login(Utility.COOKIE, "google", values).enqueue(new Callback<LoggedInUser>() {

                                                                    @Override
                                                                    public void onResponse(@NonNull Call<LoggedInUser> call, @NonNull retrofit2.Response<LoggedInUser> response) {
                                                                        if (response.body() != null) {
                                                                            LoggedInUser logged = response.body();
                                                                            Log.d("LoginRemote", "user login Response: " + response.body().toString());

                                                                            Utility.USER.setUserId(logged.getUserId());
                                                                            Utility.USER.setImageUrl(logged.getImageUrl());
                                                                            Utility.USER.setKhachHangID(logged.getKhachHangID());

                                                                            HashMap<String, String> values = new HashMap<>();
                                                                            values.put("firebaseToken", firebaseToken);
                                                                            values.put("userId", Utility.USER.getUserId());
                                                                            loginService.updateFirebaseToken(Utility.COOKIE, "updateFirebaseToken", values).enqueue(new Callback<JsonObject>() {
                                                                                @Override
                                                                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                                                                    Log.d("----updateFirebaseToken", "thanh cong");
                                                                                    startActivity(new Intent(getActivity()
                                                                                            , HomeActivity.class)
                                                                                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                                                                }

                                                                                @Override
                                                                                public void onFailure(Call<JsonObject> call, Throwable t) {
                                                                                    Log.e("----updateFirebaseToken", t.getMessage());
                                                                                }
                                                                            });
                                                                        } else {
                                                                            //trường hợp chưa đăng ký thì yêu cầu nhập thêm cmnd
                                                                            NavHostFragment.findNavController(LoginFragment.this).navigate(R.id.action_SignIn_to_SignUpFragment);
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onFailure(@NonNull Call<LoggedInUser> call, Throwable t) {
                                                                        t.printStackTrace();
                                                                    }
                                                                });
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    } else {
                                                        // When task is unsuccessful
                                                        // Display Toast

                                                        Toast.makeText(getActivity(), "Authentication Failed :" + task.getException()
                                                                .getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                } else {
                                    Log.d("----googleLogin", "googleSignInAccount null");
                                }
                            } catch (ApiException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d("----googleLogin", "googleSignInTask unsuccessful");
                        }
                    }
                });

        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                // Initialize sign in intent
                Intent intent = googleSignInClient.getSignInIntent();
                // Start activity for result
                someActivityResultLauncher.launch(intent);
            }
        });
        forgotPwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecoverPasswordDialog();
            }
        });

        loginViewModel.getLoginFormState().observe(getViewLifecycleOwner(), new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(getViewLifecycleOwner(), new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }

                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                } else if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                    // NavHostFragment.findNavController(LoginFragment.this).popBackStack(R.id.SignInFragment,true);
                }
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(
                        usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(), passwordEditText.getText().toString(), firebaseToken);
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                firebaseAuth.signInWithEmailAndPassword(usernameEditText.getText().toString(), passwordEditText.getText().toString())
                        .addOnCompleteListener(
                                new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(
                                            @NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            loadingProgressBar.setVisibility(View.GONE);
                                            loginViewModel.login(usernameEditText.getText().toString(), "", firebaseToken);
                                        } else {

                                            // sign-in failed
                                            Toast.makeText(getActivity(),
                                                    "Login firebase failed!!",
                                                    Toast.LENGTH_LONG)
                                                    .show();
                                        }
                                    }
                                });
            }
        });

        btChangeLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(LoginFragment.this).navigate(R.id.action_SignIn_to_SignUpFragment);
            }
        });
    }

    private void showRecoverPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Quên Mật Khẩu");
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setGravity(Gravity.CENTER);
        final EditText emailet = new EditText(getActivity());

        // write the email using which you registered
        emailet.setHint("Email");
        emailet.setMinEms(16);
        emailet.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        linearLayout.addView(emailet);
        linearLayout.setPadding(10, 10, 10, 10);
        builder.setView(linearLayout);

        // Click on Recover and a email will be sent to your registered email id
        builder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = emailet.getText().toString().trim();
                beginRecovery(email);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    ProgressDialog loadingBar;

    private void beginRecovery(String email) {
        loadingBar = new ProgressDialog(getActivity());
        loadingBar.setMessage("Sending Email....");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        // calling sendPasswordResetEmail
        // open your email and write the new
        // password and then you can login
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                loadingBar.dismiss();
                if (task.isSuccessful()) {
                    // if isSuccessful then done message will be shown
                    // and you can change the password
                    Toast.makeText(getActivity(), "Đã gửi email reset mật khẩu!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Có lỗi xảy ra với firebase!", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingBar.dismiss();
                Toast.makeText(getActivity(), "Error Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = "Xin chào " + model.getDisplayName();

        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(getContext().getApplicationContext(), welcome, Toast.LENGTH_LONG).show();

            Intent i = new Intent(getActivity(), HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);


            getActivity().startActivity(i);
        }
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(getContext().getApplicationContext(), errorString, Toast.LENGTH_LONG).show();
        }
    }

    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                return;
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}