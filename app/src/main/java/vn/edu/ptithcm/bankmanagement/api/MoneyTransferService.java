package vn.edu.ptithcm.bankmanagement.api;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.Header;
import retrofit2.http.OPTIONS;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface MoneyTransferService {
    static final String key1 = "soTK_Chuyen";
    static final String key2 = "soTien";
    static final String key3 = "soTK_Nhan";

    @OPTIONS("api-money-tranfer")
    Call<JsonObject> getTransfer();

    @POST("api-money-tranfer")
    Call<JsonObject> doTransfer(@Header ("Cookie") String sessionId, @Body Map<String, String> body);
}