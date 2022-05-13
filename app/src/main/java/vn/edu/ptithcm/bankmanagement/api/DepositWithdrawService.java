package vn.edu.ptithcm.bankmanagement.api;

import com.google.gson.JsonObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface DepositWithdrawService {
    String key1 = "soTK";
    String key2 = "soTien";
    String key3 = "loaiGD";

    @GET("api-deposit-withdraw")
    Call<JsonObject> getDepositOrWithdraw();

    @POST("api-deposit-withdraw")
    Call<JsonObject> depositOrWithdraw(@Header("Cookie") String sessionId, @Body HashMap<String, String> body);
}