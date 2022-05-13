package vn.edu.ptithcm.bankmanagement.api;

import com.google.gson.JsonObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

public interface ChangePasswordService {
    static final String key1 = "matKhauCu";
    static final String key2 = "matKhauMoi";
    static final String key3 = "nhapLaiMatKhauMoi";

    @GET("api-deposit-withdraw")
    Call<JsonObject> getDepositOrWithdraw();

    @Multipart
    @POST("api-deposit-withdraw")
    Call<JsonObject> depositOrWithdraw(@Body HashMap<String, String> body);
}
