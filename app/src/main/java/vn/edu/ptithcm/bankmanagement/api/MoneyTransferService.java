package vn.edu.ptithcm.bankmanagement.api;

import com.google.gson.JsonObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.OPTIONS;
import retrofit2.http.POST;

public interface MoneyTransferService {
    String key1 = "soTK_Chuyen";
    String key2 = "soTien";
    String key3 = "soTK_Nhan";

    @OPTIONS("api-money-tranfer")
    Call<JsonObject> getTransfer();

    @POST("api-money-tranfer")
    Call<JsonObject> doTransfer(@Header("Cookie") String sessionId, @Body HashMap<String, String> body);
}