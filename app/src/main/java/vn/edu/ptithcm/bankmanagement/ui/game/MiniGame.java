package vn.edu.ptithcm.bankmanagement.ui.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.ptithcm.bankmanagement.R;
import vn.edu.ptithcm.bankmanagement.api.ApiClient;
import vn.edu.ptithcm.bankmanagement.api.DepositWithdrawService;
import vn.edu.ptithcm.bankmanagement.utility.Utility;

public class MiniGame extends AppCompatActivity {
    static String TAG = MiniGame.class.getName();

    WebView webView;
    // TODO: giảm lượng tiền thưởng
    final double reward = 100000d;

    ApiClient apiClient;
    DepositWithdrawService depositWithdrawService;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mini_game);

        apiClient = new ApiClient();
        depositWithdrawService = apiClient.getDepositWithdrawService();

        webView = findViewById(R.id.webView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(true);
        settings.setDefaultTextEncodingName("utf-8");

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.addJavascriptInterface(new JavaScriptInterface(), "AndroidInterface");

        webView.loadUrl("file:///android_asset/CrappyBird/index.html");
    }

    private class JavaScriptInterface {
        @JavascriptInterface
        public void sendReward() {
            Log.d(TAG, "send Reward");
            HashMap<String, String> values = new HashMap<>();
            values.put(DepositWithdrawService.key1, Utility.LIST_TK.get(0).getSoTK());
            values.put(DepositWithdrawService.key2, String.valueOf(reward));
            values.put(DepositWithdrawService.key3, "GT");

            Call<JsonObject> call = depositWithdrawService.depositOrWithdraw(Utility.COOKIE, values);

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "dpwd Response: " + (response.body() != null ? response.body().toString() : "dpwd response ok"));
                        if (response.body().toString().contains("FOREIGN")) {
                            Toast.makeText(MiniGame.this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                        } else {
                            finish();
                        }
                    } else if (response.code() == HttpsURLConnection.HTTP_UNAUTHORIZED) {
                        Log.d(TAG, "dpwd 401");
                    } else {
                        try {
                            if (response.errorBody() == null) {
                                Log.d(TAG, "transfer Response Error. No message");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                    Log.d(TAG, "Failure");
                    t.printStackTrace();
                }
            });
        }
    }


}