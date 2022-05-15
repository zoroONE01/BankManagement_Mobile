package vn.edu.ptithcm.bankmanagement.ui.statistic;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.ptithcm.bankmanagement.R;
import vn.edu.ptithcm.bankmanagement.api.ApiClient;
import vn.edu.ptithcm.bankmanagement.api.ProfileService;
import vn.edu.ptithcm.bankmanagement.api.UserStatisticService;
import vn.edu.ptithcm.bankmanagement.data.model.ThongKeGD;
import vn.edu.ptithcm.bankmanagement.utility.Helper;
import vn.edu.ptithcm.bankmanagement.utility.Utility;

public class StatisticFragment extends Fragment {
    static final String TAG = StatisticFragment.class.getName();
    ApiClient apiClient;
    UserStatisticService userStatisticService;

    LineChart chart;
    int mode;
    int colorChart = Color.rgb(90, 117, 255);

    public StatisticFragment(int mode) {
        this.mode = mode;
    }

    private void setupChart(LineChart chart, LineData data, int color) {

        ((LineDataSet) data.getDataSetByIndex(0)).setCircleHoleColor(color);

        // no description text
        chart.getDescription().setEnabled(false);


        // chart.setDrawHorizontalGrid(false);
        //
        // enable / disable grid background
        chart.setDrawGridBackground(false);
//        chart.getRenderer().getGridPaint().setGridColor(Color.WHITE & 0x70FFFFFF);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setBackgroundColor(Color.WHITE);

        // set custom chart offsets (automatic offset calculation is hereby disabled)
        chart.setViewPortOffsets(10, 0, 10, 0);

        // add data
        chart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();
        l.setEnabled(false);

        chart.getAxisLeft().setEnabled(false);
        chart.getAxisLeft().setSpaceTop(40);
        chart.getAxisLeft().setSpaceBottom(40);
        chart.getAxisRight().setEnabled(false);

        chart.getXAxis().setEnabled(false);

        // animate calls invalidate()...
        chart.animateX(2500);
    }

    private LineData getData(List<ThongKeGD> listGD) {
        ArrayList<Entry> values = new ArrayList<>();
        for (int i = 0; i < listGD.size(); i++) {
            Log.d("-------", listGD.get(i).getBalanceAfter() + " vnd");
            double val = listGD.get(i).getBalanceAfter();
            values.add(new Entry(i, (float) val));
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(values, "DataSet 1");
         set1.setFillAlpha(110);
         set1.setFillColor(colorChart);

        set1.setLineWidth(1.75f);
        set1.setCircleRadius(5f);
        set1.setCircleHoleRadius(2.5f);
        set1.setColor(colorChart);
        set1.setCircleColor(Color.WHITE);
        set1.setHighLightColor(colorChart);
        set1.setDrawValues(true);
        set1.setDrawFilled(true);

        // create a data object with the data sets
        return new LineData(set1);
    }

   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_statistic, container, false);
       apiClient = new ApiClient();
       userStatisticService = apiClient.getUserStatisticService();
       chart = view.findViewById(R.id.chart1);
       if (mode == 0){
           doGetListTransactions(userStatisticService, Utility.LIST_TK.get(0).getSoTK(), Helper.getDateStringOneMonthEarlier(), Helper.getDateString());
       } else if (mode == 1) {
           doGetListTransactions(userStatisticService, Utility.LIST_TK.get(0).getSoTK(), Helper.getDateStringOneWeekEarlier(), Helper.getDateString());
       } else {
           doGetListTransactions(userStatisticService, Utility.LIST_TK.get(0).getSoTK(), Helper.getDateStringOneDayEarlier(), Helper.getDateString());
       }

       return view;
    }

    private List<ThongKeGD> doGetListTransactions(UserStatisticService userStatisticService, String stk, String date1, String date2) {
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

                    LineData data = getData(list);
                    // add some transparency to the color with "& 0x90FFFFFF"
                    setupChart(chart, data, colorChart);
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
}