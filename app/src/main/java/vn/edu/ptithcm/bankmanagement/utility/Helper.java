package vn.edu.ptithcm.bankmanagement.utility;

import com.itextpdf.text.Font;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.itextpdf.text.Document;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.ptithcm.bankmanagement.api.MoneyTransferService;
import vn.edu.ptithcm.bankmanagement.api.ProfileService;
import vn.edu.ptithcm.bankmanagement.api.UserService;
import vn.edu.ptithcm.bankmanagement.api.UserStatisticService;
import vn.edu.ptithcm.bankmanagement.data.model.KhachHang;
import vn.edu.ptithcm.bankmanagement.data.model.TaiKhoan;
import vn.edu.ptithcm.bankmanagement.data.model.ThongKeGD;

public class Helper {
    static final String TAG = Helper.class.getName();

    public static boolean doLogin(UserService userService, String username, String password) {
        Call<JsonObject> call = userService.login(username, password);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "login Response: " + response.body().toString());
                    Log.d(TAG, "headers values: " + response.headers());

                    Utility.COOKIE = response.headers().get("Set-Cookie");
                } else {
                    Log.d(TAG, "Login Response Error" + response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Log.d(TAG, "Login Failure");
                t.printStackTrace();
            }
        });
        if (Utility.COOKIE == null || Utility.COOKIE.isEmpty()) {
            Log.d(TAG, "Error: no session id");
            return false;
        }
        return true;
    }

    public static void doGetAllTk(UserStatisticService userStatisticService, String cmnd) {
        Utility.LIST_TK.clear();

        Call<JsonArray> call = userStatisticService.getAllTk(Utility.COOKIE, cmnd);

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "list tk Response: " + (response.body() != null ? response.body().toString() : "list tk response ok"));

                    JsonArray listTK = (JsonArray) response.body();
                    Utility.LIST_TK.clear();

                    for (JsonElement ele : listTK) {
                        JsonObject e = ele.getAsJsonObject();
                        Log.d(TAG, "tk: " + e.toString());

                        TaiKhoan tk = new TaiKhoan(e.get("soTK").getAsString(),
                                e.get("cmnd").getAsString(),
                                e.get("soDu").getAsDouble(),
                                e.get("maCN").getAsString().trim(),
                                e.get("ngayGD").getAsLong());

                        Utility.LIST_TK.add(tk);
                        Log.d(TAG, tk.toString());
                    }

                } else if (response.code() == HttpsURLConnection.HTTP_UNAUTHORIZED) {
                    // Handle unauthorized
                    // TODO go back to login
                    Log.d(TAG, "list tk 401");
                } else {
                    try {
                        if (response.errorBody() == null) {
                            Log.d(TAG, "list tk Response Error. No message");
                        } else if (response.errorBody().string().contains("FOREIGN")) {
                            Log.d(TAG, "list tk k ton tai");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Log.d(TAG, "list tk Failure");
                t.printStackTrace();
            }
        });
    }

    public static List<ThongKeGD> doGetListTransactions(UserStatisticService userStatisticService, String stk) {
        List<ThongKeGD> list = new ArrayList<>();

        Call<JsonArray> call = userStatisticService.getTransactionHistory(Utility.COOKIE, stk, "2011-1-1", "2031-1-1");

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "statistic Response: " + (response.body() != null ? response.body().toString() : "dpwd response ok"));

                    JsonArray array = (JsonArray) response.body();

                    for (JsonElement ele : array) {
                        JsonObject e = ele.getAsJsonObject();
                        Log.d(TAG, "tk: " + e.toString());

                        ThongKeGD tk = new ThongKeGD(
                                e.get("balanceBefore").getAsDouble(),
                                e.get("ngayGD").getAsLong(),
                                e.get("loaiGD").getAsString(),
                                e.get("soTien").getAsDouble(),
                                e.get("balanceAfter").getAsDouble());
                        list.add(tk);
                    }
                } else if (response.code() == HttpsURLConnection.HTTP_UNAUTHORIZED) {
                    // Handle unauthorized
                    // TODO go back to login
                    Log.d(TAG, "list tk 401");
                } else {
                    try {
                        if (response.errorBody() == null) {
                            Log.d(TAG, "transfer Response Error. No message");
                        } else if (response.errorBody().string().contains("FOREIGN")) {
                            Log.d(TAG, "list tk k ton tai");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Log.d(TAG, "list tk Failure");
                t.printStackTrace();
            }
        });
        return list;
    }

    public static List<ThongKeGD> doGetListTransactions(UserStatisticService userStatisticService, String stk, String date1, String date2) {
        List<ThongKeGD> list = new ArrayList<>();

        Call<JsonArray> call = userStatisticService.getTransactionHistory(Utility.COOKIE, stk, date1, date2);

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "list transaction Response: " + (response.body() != null ? response.body().toString() : "list transaction response ok"));

                    JsonArray array = (JsonArray) response.body();

                    for (JsonElement ele : array) {
                        JsonObject e = ele.getAsJsonObject();
                        Log.d(TAG, "tk: " + e.toString());

                        ThongKeGD tk = new ThongKeGD(
                                e.get("balanceBefore").getAsDouble(),
                                e.get("ngayGD").getAsLong(),
                                e.get("loaiGD").getAsString(),
                                e.get("soTien").getAsDouble(),
                                e.get("balanceAfter").getAsDouble());
                        list.add(tk);
                    }
                } else if (response.code() == HttpsURLConnection.HTTP_UNAUTHORIZED) {
                    // Handle unauthorized
                    // TODO go back to login
                    Log.d(TAG, "list transaction 401");
                } else {
                    try {
                        if (response.errorBody() == null) {
                            Log.d(TAG, "list transaction Error. No message");
                        } else if (response.errorBody().string().contains("FOREIGN")) {
                            Log.d(TAG, "list transaction k ton tai");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Log.d(TAG, "list tk Failure");
                t.printStackTrace();
            }
        });
        return list;
    }

    public void doTransfer(MoneyTransferService transferService, String id, String amount, String otherId) {
        if (Utility.COOKIE.isEmpty()) {
            // TODO: go back to login activity
            Log.d(TAG, "Session id empty");
            return;
        }

        HashMap<String, String> values = new HashMap<>();
        values.put(MoneyTransferService.key1, id);
        values.put(MoneyTransferService.key2, amount);
        values.put(MoneyTransferService.key3, otherId);

        Call<JsonObject> call = transferService.doTransfer(Utility.COOKIE, values);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "transfer Response: " + (response.body() != null ? response.body().toString() : "dpwd response ok"));
                    // TODO: show transfer success activity
                } else if (response.code() == 401) {
                    // Handle unauthorized
                    // TODO go back to login
                    Log.d(TAG, "transfer 401");
                } else {
                    try {
                        if (response.errorBody() == null) {
                            Log.d(TAG, "transfer Response Error. No message");
                        } else if (response.errorBody().string().contains("FOREIGN KEY")) {
                            Log.d(TAG, "tai khoan k ton tai");
                        } else if (response.errorBody().string().contains("CONSTRAINT")) {
                            Log.d(TAG, "tai khoan k du tien");
                        }
                        Log.d(TAG, "transfer Response Error: " + response.errorBody().charStream());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Log.d(TAG, "Transfer Failure");
            }
        });
    }

    public static void doGetUserData(ProfileService userInfoService, String cmnd) {
        Call<KhachHang> call = userInfoService.getCustomer(Utility.COOKIE, cmnd);

        call.enqueue(new Callback<KhachHang>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "user data Response: " + response.body().toString());
                    KhachHang kh = (KhachHang) response.body();
                } else {
                    Log.d(TAG, "user data Error" + response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Log.d(TAG, "Login Failure");
                t.printStackTrace();
            }
        });
    }

    public static String showGia(Double gia) {
        return String.format(Locale.CHINESE, "%,.0f", gia);
    }

    public static double getTongTienGiaoDich(List<ThongKeGD> data) {
        double result = 0;
        for (ThongKeGD gd : data) {
            result += gd.getSoTien();
        }
        return result;
    }

    public static String getNgayFromEpoch(long epoch) {
        Utility.DATE_TIME_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(epoch);

        return Utility.DATE_TIME_FORMAT.format(c.getTime());
    }

    public static String getDateString() {
        Utility.API_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);

        return Utility.API_DATE_FORMAT.format(c.getTime());
    }

    public static String getDateStringOneDayEarlier() {
        Utility.API_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);

        return Utility.API_DATE_FORMAT.format(c.getTime());
    }

    public static String getDateStringOneWeekEarlier() {
        Utility.API_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -7);

        return Utility.API_DATE_FORMAT.format(c.getTime());
    }

    public static String getDateStringOneMonthEarlier() {
        Utility.API_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);

        return Utility.API_DATE_FORMAT.format(c.getTime());
    }


}