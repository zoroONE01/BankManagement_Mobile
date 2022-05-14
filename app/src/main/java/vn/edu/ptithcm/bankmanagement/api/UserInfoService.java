package vn.edu.ptithcm.bankmanagement.api;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface UserInfoService {
    @GET("api-customer")
    Call<JsonObject> getInfo(@Header("Cookie") String sessionId, @Query("cmnd") String cmnd);
}