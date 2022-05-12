package vn.edu.ptithcm.bankmanagement.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import vn.edu.ptithcm.bankmanagement.data.model.LoggedInUser;
import vn.edu.ptithcm.bankmanagement.data.model.LoginRequest;
import vn.edu.ptithcm.bankmanagement.data.model.RegisterRequest;

public interface LoginService {
    @POST("/Web_ForBank/user-login")
    Call<LoggedInUser> login(@Body LoginRequest loginRequest);
    @POST("/Web_ForBank/user-account")
    Call<LoggedInUser> register(@Body RegisterRequest registerRequest);
}