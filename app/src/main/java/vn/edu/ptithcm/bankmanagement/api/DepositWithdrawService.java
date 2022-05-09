package vn.edu.ptithcm.bankmanagement.api;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface DepositWithdrawService {
    static final String key1 = "soTK";
    static final String key2 = "soTien";
    static final String key3 = "loaiGD";

    @GET("api-deposit-withdraw")
    Call<JsonObject> getDepositOrWithdraw();

    @POST("api-deposit-withdraw")
    Call<JsonObject> depositOrWithdraw(@Header ("Cookie") String sessionId, @Body HashMap<String, String> body);
}