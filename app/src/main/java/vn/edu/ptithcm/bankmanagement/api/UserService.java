package vn.edu.ptithcm.bankmanagement.api;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface UserService {
    @GET("api-user-login")
    Call<JsonObject> login(@Header("user") String username, @Header("password") String password);
}