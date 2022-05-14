package vn.edu.ptithcm.bankmanagement.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;

import vn.edu.ptithcm.bankmanagement.data.model.LoggedInUser;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    private final Context context;
    private SQLiteDatabase mDatabase;

    public LoginDataSource(Context context) {
        this.context = context;
    }

    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            LoggedInUser fakeUser = new LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe");
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}