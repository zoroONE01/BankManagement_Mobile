package vn.edu.ptithcm.bankmanagement.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import vn.edu.ptithcm.bankmanagement.data.LoginRepository;
import vn.edu.ptithcm.bankmanagement.data.OnCompleteCallBack;
import vn.edu.ptithcm.bankmanagement.data.Result;
import vn.edu.ptithcm.bankmanagement.data.model.LoggedInUser;
import vn.edu.ptithcm.bankmanagement.R;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private MutableLiveData<LoginResult>registerResult = new MutableLiveData<>();
    private MutableLiveData<LoggedInUser> loggedInUser;
    private MutableLiveData<LoggedInUser> registerInUser;
    private  LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }
    LoginViewModel() {
        this.loginRepository = LoginRepository.getInstance();
    }


    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    LiveData<LoggedInUser> getLoggedInUser() {
        return loggedInUser;
    }

    LiveData<LoggedInUser> getRegisterInUser() {
        return registerInUser;
    }
    LiveData<LoginResult> getRegisterResult() {
        return registerResult;
    }
    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
     loginRepository.login(username, password, new OnCompleteCallBack() {
         @Override
         public void done(Object o) {
                LoggedInUser logged= (LoggedInUser) o;
             if(logged!=null)
             {
                 loginResult.setValue(new LoginResult(new LoggedInUserView(logged.getTaiKhoan(),logged.getUserId(),logged.getKhachHangID())));
             }
             else{
                 loginResult.setValue(new LoginResult(R.string.login_failed));
             }
         }
     });


    }

    public void register(String cmnd,String ho,String ten,String taiKhoan, String matKhau) {
        // can be launched in a separate asynchronous job
        loginRepository.register(cmnd, ho, ten,taiKhoan,matKhau, new OnCompleteCallBack() {
            @Override
            public void done(Object o) {
                LoggedInUser logged= (LoggedInUser) o;
                if(logged!=null)
                {
                    registerResult.setValue(new LoginResult(new LoggedInUserView(logged.getTaiKhoan(),logged.getUserId(),logged.getKhachHangID())));
                }
                else{
                    registerResult.setValue(new LoginResult(R.string.register_failed));
                }
            }
        });


    }
    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}