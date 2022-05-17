package vn.edu.ptithcm.bankmanagement.utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.ptithcm.bankmanagement.api.ImageService;

public class Image {
    public static void doLoadImage(ImageService imageService, String path, ImageView imageView) {
        //    /avatar/ycGK.png
        Call<ResponseBody> call = imageService.getImage(path);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    ResponseBody content = (ResponseBody) response.body();
                    if (content != null) {
                        Bitmap bmp = BitmapFactory.decodeStream(content.byteStream());
                        imageView.setImageBitmap(bmp);
                    } else {

                    }
                } else {

                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {

            }
        });
    }

    public static String save(ImageService imageService, String folderToSave, String fileName, String realPath) {
        //  folderToSave     avatar
        //  fileName         111111111
        File file = new File(realPath);
        // convert the file name into string
        String realFileName = file.getName();
        String extension = ""; //đuôi file

        int index = realFileName.lastIndexOf('.');
        if (index > 0) {
            extension = realFileName.substring(index);
        }

        RequestBody requestFile =
                RequestBody.create(file, MediaType.parse("image/*"));

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", fileName + extension, requestFile);

        Call<JsonObject> call = imageService.saveImage(folderToSave, body);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.v("--------", "success");
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("--------Failure", t.getMessage());
            }
        });

        return "/" + folderToSave + "/" + fileName + extension;
    }
}