package vn.edu.ptithcm.bankmanagement.ui.profile;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
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
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.ptithcm.bankmanagement.R;
import vn.edu.ptithcm.bankmanagement.api.ApiClient;
import vn.edu.ptithcm.bankmanagement.api.DepositWithdrawService;
import vn.edu.ptithcm.bankmanagement.api.ImageService;
import vn.edu.ptithcm.bankmanagement.api.LoginService;
import vn.edu.ptithcm.bankmanagement.api.ProfileService;
import vn.edu.ptithcm.bankmanagement.data.model.KhachHang;
import vn.edu.ptithcm.bankmanagement.data.model.LoggedInUser;
import vn.edu.ptithcm.bankmanagement.ui.depositwithdraw.DepositWithdrawActivity;
import vn.edu.ptithcm.bankmanagement.utility.Image;
import vn.edu.ptithcm.bankmanagement.utility.Utility;

public class ProfileActivity extends AppCompatActivity {
    final int MyVersion = Build.VERSION.SDK_INT;
    String avatarPath;
    boolean isChangeAvatar = false;
    ApiClient apiClient;
    ImageService imageService;
    ProfileService profileService;
    LoginService userAccountService;

    ImageView avatar;
    TextView tvCmnd;
    EditText txtHo;
    EditText txtTen;
    EditText txtDiaChi;
    Spinner snPhai;
    DatePicker dpNgayCap;
    EditText txtSdt;

    AppCompatImageButton b_back;

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

                        if (picturePath!=null) {
                            avatarPath = picturePath;
                            isChangeAvatar = true;
                        }
                        Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                        avatar.setImageBitmap(bitmap);
                    }
                }
            });

    void loadCustomer(String cmnd){
        Log.d("-----customer", "cmnd: " + cmnd);
        if (Utility.COOKIE.isEmpty()) {
            Toast.makeText(this, "Error: no session id", Toast.LENGTH_SHORT).show();
            return;
        }

        profileService.getCustomer(Utility.COOKIE, cmnd).enqueue(new Callback<KhachHang>() {

            @Override
            public void onResponse(@NonNull Call<KhachHang> call, @NonNull retrofit2.Response<KhachHang> response) {
                if (response.body() != null) {
                    KhachHang cus = response.body();
                    tvCmnd.setText(cus.getCmnd());
                    txtHo.setText(cus.getHo());
                    txtTen.setText(cus.getTen());
                    txtDiaChi.setText(cus.getDiachi());
                    snPhai.setSelection(cus.getPhai().equals("Nam") ? 0:1);

                    Date temp =new Date(cus.getNgayCap().getTime());
                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(temp);
                    dpNgayCap.updateDate(calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH) + 1,
                            calendar.get(Calendar.DAY_OF_MONTH));

                    txtSdt.setText(cus.getSdt());
                } else {
                    Log.d("-----customer", "get customer response body fail");
                }
            }

            @Override
            public void onFailure(@NonNull Call<KhachHang> call, Throwable t) {
                Log.d("-----customer", "Failure " + t.getMessage());
            }
        });
    }

    void updateCustomer(String cmnd, String ho, String ten, String diaChi, String phai, String ngayCap, String sdt) {
        if (Utility.COOKIE.isEmpty()) {
            Toast.makeText(this, "Error: no session id", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, String> values = new HashMap<>();
        values.put("cmnd", cmnd);
        values.put("ho", ho);
        values.put("ten", ten);
        values.put("diaChi", diaChi);
        values.put("phai", phai);
        values.put("ngayCap", ngayCap);
        values.put("soDT", sdt);

        Call<JsonObject> call = profileService.updateCustomer(Utility.COOKIE, values);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    JsonObject object = (JsonObject) response.body();
                    String message = object.get("message").getAsString();
                    Log.d("-------", "Response: " + object.toString());

                    Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        Toast.makeText(ProfileActivity.this, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Toast.makeText(ProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onClickUpdate(View view) {
        String strCmnd = tvCmnd.getText().toString();
        String strHo = txtHo.getText().toString();
        String strTen = txtTen.getText().toString();
        String strDiaChi = txtDiaChi.getText().toString();
        String strPhai = snPhai.getSelectedItem().toString();
        String strNgayCap = dpNgayCap.getDayOfMonth()+"-"+ (dpNgayCap.getMonth()+1) +"-" +dpNgayCap.getYear() ;
        String strSdt = txtSdt.getText().toString();

        //replace old image
        if (isChangeAvatar) {
            Utility.USER.setImageUrl(Image.save(imageService, "avatar", strCmnd, avatarPath));

            HashMap<String, String> values = new HashMap<>();
            values.put("imageUrl", Utility.USER.getImageUrl());
            values.put("userId", Utility.USER.getUserId());
            Call<JsonObject> call = userAccountService.updateImage(Utility.COOKIE, values);

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()){
                        JsonObject object = response.body();
                        String message = object.get("message").getAsString();
                        Log.d("-------", "Response: " + object.toString());

                        Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("-------", "Response: " + (response.body() != null ? response.errorBody().toString() : "response ok"));

                        Toast.makeText(ProfileActivity.this, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("-------", "failure: " + t.getMessage());

                    Toast.makeText(ProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        }
        updateCustomer(strCmnd, strHo, strTen, strDiaChi, strPhai, strNgayCap, strSdt);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        apiClient = new ApiClient();
        imageService = apiClient.getImageService();
        profileService = apiClient.getProfileService();
        userAccountService = apiClient.getLoginService();

        avatar = findViewById(R.id.avatar);
        tvCmnd = findViewById(R.id.cmnd);
        txtHo = findViewById(R.id.ho);
        txtTen = findViewById(R.id.ten);
        txtDiaChi = findViewById(R.id.diaChi);
        snPhai = findViewById(R.id.phai);
        dpNgayCap = findViewById(R.id.ngayCap);
        txtSdt = findViewById(R.id.sdt);

        loadCustomer(Utility.USER.getKhachHangID());
        Image.doLoadImage(imageService, Utility.USER.getImageUrl(), avatar);

        b_back = findViewById(R.id.b_back);
        b_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
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