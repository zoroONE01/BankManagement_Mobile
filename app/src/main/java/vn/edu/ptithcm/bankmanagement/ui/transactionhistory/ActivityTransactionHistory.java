package vn.edu.ptithcm.bankmanagement.ui.transactionhistory;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.ptithcm.bankmanagement.R;
import vn.edu.ptithcm.bankmanagement.api.ApiClient;
import vn.edu.ptithcm.bankmanagement.api.UserStatisticService;
import vn.edu.ptithcm.bankmanagement.data.model.ThongKeGD;
import vn.edu.ptithcm.bankmanagement.ui.home.RecentTransactionAdapter;
import vn.edu.ptithcm.bankmanagement.utility.Helper;
import vn.edu.ptithcm.bankmanagement.utility.Utility;

public class ActivityTransactionHistory extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    final String TAG = ActivityTransactionHistory.class.getName();
    private static final int PERMISSION_REQUEST_CODE = 200;

    ApiClient apiClient;
    AppCompatImageButton b_back, b_more;
    UserStatisticService userStatisticService;


    private RecyclerView rvRecentTransaction;
    private List<ThongKeGD> transactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        initComponents();
    }

    private void initComponents() {
        apiClient = new ApiClient();
        userStatisticService = apiClient.getUserStatisticService();

        rvRecentTransaction = findViewById(R.id.rv_recent_transaction);
        rvRecentTransaction.setLayoutManager(new LinearLayoutManager(this));

        b_back = findViewById(R.id.b_back);
        b_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        b_more = findViewById(R.id.b_more);
        b_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(ActivityTransactionHistory.this, view);
                popup.setOnMenuItemClickListener(ActivityTransactionHistory.this);
                popup.inflate(R.menu.menu_more);
                popup.show();
            }
        });



        if (!Utility.LIST_TK.isEmpty()) {
            doGetListTransactions(userStatisticService, Utility.LIST_TK.get(0).getSoTK());
        }
    }

    void doGetListTransactions(UserStatisticService userStatisticService, String stk) {
        List<ThongKeGD> list = new ArrayList<>();

        Call<JsonArray> call = userStatisticService.getTransactionHistory(Utility.COOKIE, stk, "2011-1-1", "2031-1-1");

        call.enqueue(new Callback<JsonArray>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "statistic Response: " + (response.body() != null ? response.body().toString() : "statistic response ok"));

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
                    list.sort(new Comparator<ThongKeGD>() {
                        @Override
                        public int compare(ThongKeGD t, ThongKeGD other) {
                            return -1 * t.getNgayGD().compareTo(other.getNgayGD());
                        }
                    });
                    transactions = list;
                    rvRecentTransaction.setAdapter(new RecentTransactionAdapter(transactions));
                } else if (response.code() == HttpsURLConnection.HTTP_UNAUTHORIZED) {
                    // TODO go back to login
                    Log.d(TAG, "list tk 401");
                } else {
                    try {
                        if (response.errorBody() == null) {
                            Log.d(TAG, "list transfer Response Error. No message");
                        } else if (response.errorBody().string().contains("FOREIGN")) {
                            Log.d(TAG, "so tk k ton tai");
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

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.export_pdf:
                if (!checkPermission()) {
                    requestPermission();
                }
                generatePDF();
                return true;
            default:
                return false;
        }
    }

    private void generatePDF() {
        Document document = new Document();

        try {
            BaseFont bf = BaseFont.createFont("res/font/roboto1.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            File file = new File(Environment.getExternalStorageDirectory(), "DSGD.pdf");
            PdfWriter.getInstance(document,
                    new FileOutputStream(file));

            document.open();

            Phrase soTK = new Phrase("Số tài khoản: "+ Utility.LIST_TK.get(0).getSoTK(),new Font(bf, 16));
            // Table
            PdfPTable table = new PdfPTable(5);

            // Header
            PdfPCell cell1 = new PdfPCell(new Phrase("Số dư trước",new Font(bf, 14)));
            PdfPCell cell2 = new PdfPCell(new Phrase("Ngày giao dịch",new Font(bf, 14)));
            PdfPCell cell3 = new PdfPCell(new Phrase("Loại giao dịch",new Font(bf, 14)));
            PdfPCell cell4 = new PdfPCell(new Phrase("Số tiền",new Font(bf, 14)));
            PdfPCell cell5 = new PdfPCell(new Phrase("Số dư sau",new Font(bf, 14)));
            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);
            table.addCell(cell4);
            table.addCell(cell5);

            for (ThongKeGD i : transactions) {
                PdfPCell cell1x = new PdfPCell(new Phrase(String.valueOf(i.getBalanceBefore())));
                PdfPCell cell2x = new PdfPCell(new Phrase(String.valueOf(new Date(i.getNgayGD())),new Font(bf, 14)));
                PdfPCell cell3x = new PdfPCell(new Phrase(i.getLoaiGD(),new Font(bf, 14)));
                PdfPCell cell4x = new PdfPCell(new Phrase(String.valueOf(i.getSoTien())));
                PdfPCell cell5x = new PdfPCell(new Phrase(String.valueOf(i.getBalanceAfter())));
                table.addCell(cell1x);
                table.addCell(cell2x);
                table.addCell(cell3x);
                table.addCell(cell4x);
                table.addCell(cell5x);
            }

            document.add(soTK);
            document.add(table);

            document.close();
            Toast.makeText(this, "Create PDF successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.d("------actTransHis", e.getMessage());
        }
    }

    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Log.d("---exportPDF", "Permission Granted");
                } else {
                    Toast.makeText(this, "Permission Denined.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }
}