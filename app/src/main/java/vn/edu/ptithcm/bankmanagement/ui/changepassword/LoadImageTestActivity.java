package vn.edu.ptithcm.bankmanagement.ui.changepassword;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.ptithcm.bankmanagement.R;
import vn.edu.ptithcm.bankmanagement.api.ApiClient;
import vn.edu.ptithcm.bankmanagement.api.DepositWithdrawService;
import vn.edu.ptithcm.bankmanagement.api.LoadImageService;
import vn.edu.ptithcm.bankmanagement.ui.depositwithdraw.DepositWithdrawActivity;
import vn.edu.ptithcm.bankmanagement.utility.Utility;

public class LoadImageTestActivity extends AppCompatActivity {

    final int MyVersion = Build.VERSION.SDK_INT;
    ImageView iv_jpg;
    String filePath;

    ApiClient apiClient;
    LoadImageService loadImageService;



    public void onLoadPicture(View view) {
        //request permission to stored
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyHavePermission()) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                setResult(Activity.RESULT_OK, i);
                //alter start activity for result
                someActivityResultLauncher.launch(i);
            }
        }
    }

    public void onClickSave(View view) {

        File file = new File(filePath);

        RequestBody requestFile =
                RequestBody.create(file, MediaType.parse("image/*"));

        //sửa thành thư mục mình muốn lưu
        String folderToSave = "avatar";
        //sửa thành tên file mình muốn lưu
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", file.getName(), requestFile);


        Call<JsonObject> call = loadImageService.saveImage(folderToSave, body);
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

    }

    void doLoadImage(String path) {

        Call<ResponseBody> call = loadImageService.getImage(path);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    ResponseBody content = (ResponseBody) response.body();
                    if (content != null) {
                        Bitmap bmp = BitmapFactory.decodeStream(content.byteStream());
                        iv_jpg.setImageBitmap(bmp);
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

    private boolean checkIfAlreadyHavePermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();

                        Log.d("picturePath", picturePath);
                        filePath = picturePath;
                        Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                        ImageView avatar = (ImageView) findViewById(R.id.iv_jpg);
                        avatar.setImageBitmap(bitmap);
                    }
                }
            });

    private void initComponents() {
        apiClient = new ApiClient(this);
        loadImageService = apiClient.getImageService();

        iv_jpg = findViewById(R.id.iv_jpg);

        doLoadImage("/avatar/ycGK.png");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_image_test);

        initComponents();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                setResult(Activity.RESULT_OK, i);
                //alter start activity for result
                someActivityResultLauncher.launch(i);
            } else {
                Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
            }
        }
    }
}