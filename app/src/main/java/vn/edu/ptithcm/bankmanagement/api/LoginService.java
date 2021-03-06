package vn.edu.ptithcm.bankmanagement.api;

import com.google.gson.JsonObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import vn.edu.ptithcm.bankmanagement.data.model.LoggedInUser;
import vn.edu.ptithcm.bankmanagement.data.model.LoginRequest;
import vn.edu.ptithcm.bankmanagement.data.model.RegisterRequest;

public interface LoginService {
    @POST("user-login")
    Call<LoggedInUser> login(@Header("Cookie") String cookie, @Body LoginRequest loginRequest);

    @POST("user-login")
    Call<LoggedInUser> login(@Header("Cookie") String cookie, @Query("verifiedBy") String account, @Body HashMap<String, String> body);

    @POST("user-account")
    Call<LoggedInUser> register(@Header("Cookie") String cookie, @Body RegisterRequest registerRequest);

    @PUT("user-account")
    Call<JsonObject> updateImage(@Header("Cookie") String cookie, @Body HashMap<String, String> body);

    //action = "updateFirebaseToken"
    @PUT("user-account")
    Call<JsonObject> updateFirebaseToken(@Header("Cookie") String cookie, @Query("action") String action, @Body HashMap<String, String> body);
}