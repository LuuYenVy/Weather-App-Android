package com.example.project_dbreathuit_app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChartActivity extends AppCompatActivity {
    private BarChart barChart;
    private API_SERVICE apiService;
    private TokenManager tokenManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_chart);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), DashBoardMainActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_map) {
                startActivity(new Intent(getApplicationContext(), MapActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_chart) {
                return true;
            } else if (item.getItemId() == R.id.bottom_profile) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }

            return false;
        });
        tokenManager = new TokenManager(this);
        apiService = API_CLIENT.getClient("https://uiot.ixxc.dev/", tokenManager.getAccessToken()).create(API_SERVICE.class);
        BarChart barChart = findViewById(R.id.barChart);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://uiot.ixxc.dev/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<YourDataModel> call = apiService.getChartData();
        call.enqueue(new Callback<YourDataModel>() {
            @Override
            public void onResponse(Call<YourDataModel> call, Response<YourDataModel> response) {
                if (response.isSuccessful()) {
                    // Xử lý dữ liệu nhận được và hiển thị lên biểu đồ
                } else {
                    // Xử lý khi nhận được mã lỗi không thành công
                }
            }

            @Override
            public void onFailure(Call<YourDataModel> call, Throwable t) {
                // Xử lý khi gặp lỗi trong quá trình gọi API
            }
        });

//        List<BarEntry> entries = new ArrayList<>();
//
//        // Dữ liệu mẫu
//        for (int i = 0; i < 25; i++) {
//            entries.add(new BarEntry(i, (float) Math.random() * 10));
//        }
//
//        BarDataSet dataSet = new BarDataSet(entries, "Dữ liệu");
//        dataSet.setColor(Color.BLUE);
//
//        BarData barData = new BarData(dataSet);
//        barChart.setData(barData);
//
//        // Đặt label cho từng thanh bar
//        XAxis xAxis = barChart.getXAxis();
//        xAxis.setValueFormatter(new ValueFormatter() {
//            @Override
//            public String getAxisLabel(float value, AxisBase axis) {
//                // Lấy thời gian hiện tại
//                long currentTimestamp = System.currentTimeMillis();
//
//                // Lấy lịch hiện tại
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTimeInMillis(currentTimestamp);
//
//                // Lấy giờ hiện tại
//                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
//
//                // Thiết lập giờ bắt đầu từ giờ hiện tại và cộng thêm giờ vào
//                calendar.set(Calendar.HOUR_OF_DAY, currentHour);
//                calendar.set(Calendar.MINUTE, 0);
//                calendar.set(Calendar.SECOND, 0);
//                calendar.set(Calendar.MILLISECOND, 0);
//
//                // Cộng thêm giờ vào từng nhãn
//                calendar.add(Calendar.HOUR_OF_DAY, (int) value);
//
//                // Lấy thời điểm của nhãn
//                long labelTimestamp = calendar.getTimeInMillis();
//
//                // Chuyển đổi giá trị trên trục x thành label
//                String label = getFormattedDate(labelTimestamp);
//                return label;
//            }
//        });
//
//        // Hiển thị
//        barChart.invalidate();
    }

    private String getFormattedDate(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(date);
    }
}
