package vn.edu.ptithcm.bankmanagement.api;

import com.google.gson.JsonObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import vn.edu.ptithcm.bankmanagement.data.model.KhachHang;

public interface ProfileService {
    @GET("api-customer")
    Call<KhachHang> getCustomer(@Header("Cookie") String sessionId, @Query("cmnd") String cmnd);

    @PUT("api-customer")
    Call<JsonObject> updateCustomer(@Header("Cookie") String sessionId, @Body HashMap<String, String> body);
}
