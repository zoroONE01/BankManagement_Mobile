package vn.edu.ptithcm.bankmanagement.api;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import vn.edu.ptithcm.bankmanagement.data.model.TaiKhoan;

public interface UserStatisticService {
    @GET("api-bank-account-statement")
    Call<JsonObject> getStat(@Header("Cookie") String sessionId, @Query("soTK") String sotk, @Query("tuNgay") String ngay1, @Query("denNgay") String ngay2);

    @GET("api-tk")
    Call<List<TaiKhoan>> getAllTk(@Header("Cookie") String sessionId, @Query("cmnd") String cmnd);
}