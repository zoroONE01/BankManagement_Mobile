package vn.edu.ptithcm.bankmanagement.api;

import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface LoadImageService {
    String key1 = "path";

    @GET("test")
    Call<ResponseBody> getImage(@Query("path") String path);

    @Multipart
    @POST("test")
    Call<JsonObject> saveImage(@Query("folderToSave") String folderToSave, @Part MultipartBody.Part file);
}