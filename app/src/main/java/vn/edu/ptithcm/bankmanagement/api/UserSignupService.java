package vn.edu.ptithcm.bankmanagement.api;

import com.google.gson.JsonObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserSignupService {
    String key1 = "cmnd";
    String key2 = "ho";
    String key3 = "ten";
    String key4 = "taiKhoan";
    String key5 = "matKhau";

    @POST("user-account")
    Call<JsonObject> doSignup(@Header("Cookie") String sessionId, @Body HashMap<String, String> body);
}