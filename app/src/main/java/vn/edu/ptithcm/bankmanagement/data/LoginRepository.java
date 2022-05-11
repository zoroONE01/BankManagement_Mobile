package vn.edu.ptithcm.bankmanagement.data;

import androidx.lifecycle.MutableLiveData;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vn.edu.ptithcm.bankmanagement.apis.LoginService;
import vn.edu.ptithcm.bankmanagement.data.model.LoggedInUser;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;

    private LoginDataSource dataSource;
    private LoginRemote loginRemote;

    private MutableLiveData<LoggedInUser> loginInUser ;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;

    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
        this.loginRemote=LoginRemote.getInstance();

    }

    public LoginRepository() {
        this.loginRemote=LoginRemote.getInstance();
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }
    public static LoginRepository getInstance() {
        if (instance == null) {
            instance = new LoginRepository();
        }
        return instance;
    }

    public MutableLiveData<LoggedInUser> getLoginInUser() {
        return loginRemote.getLoginInUser();
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }
    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public Result<LoggedInUser> login(String username, String password,OnCompleteCallBack onCompleteCallBack) {
        // handle login
        loginRemote.login(username,password,onCompleteCallBack);
        return null;
       /* Result<LoggedInUser> result ;

        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }
        return result;*/
    }
    public Result<LoggedInUser> register(String cmnd,String ho,String ten,String taiKhoan, String matKhau,OnCompleteCallBack onCompleteCallBack) {
        // handle login
        loginRemote.register(cmnd,ho,ten,taiKhoan,matKhau,onCompleteCallBack);
        return null;
       /* Result<LoggedInUser> result ;

        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }
        return result;*/
    }
}