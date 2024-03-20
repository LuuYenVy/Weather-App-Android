package com.example.project_dbreathuit_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Spanned;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_dbreathuit_app.model.Asset;
import com.example.project_dbreathuit_app.model.UserResponseModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DashBoardMainActivity extends AppCompatActivity {
    private API_SERVICE apiService;
    private Button ButtonWelcome;
    private TokenManager tokenManager;
    private Timer timer;
    private TextView currentDateTextView;
    Context context;
    Resources resources;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board_main);

        SharedPreferences preferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
        String savedLanguage = preferences.getString("language", "");

        if (!savedLanguage.isEmpty()) {
            context = LocaleHelper.setLocale(DashBoardMainActivity.this, savedLanguage);
            resources = context.getResources();
            recursivelySetLanguage(findViewById(android.R.id.content), resources);}

        tokenManager = new TokenManager(this);
        apiService = API_CLIENT.getClient("https://uiot.ixxc.dev/", tokenManager.getAccessToken()).create(API_SERVICE.class);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);
        currentDateTextView = findViewById(R.id.TxtCurrentTime);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_home) {
                // Do something for bottom_home
                return true;
            } else if (item.getItemId() == R.id.bottom_map) {
                startActivity(new Intent(getApplicationContext(), MapActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_chart) {
                startActivity(new Intent(getApplicationContext(), ChartActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_profile) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }

            return false;
        });
        scheduleApiCall();
        setCurrentDate();
    }
    private void scheduleApiCall() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    getAssetInfo();
                    getAssetInfoAQI();
                    getUser();
                });
            }
        }, 0, 500000); // 5000 milliseconds = 5 seconds
    }
    private void getAssetInfo(){
        Call<Asset> calllasset = apiService.getAsset("5zI6XqkQVSfdgOrZ1MyWEf");
        calllasset.enqueue(new Callback<Asset>() {
            @Override
            public void onResponse(Call<Asset> call, Response<Asset> response) {
                if(response.isSuccessful()){
                    Asset dataasset = response.body();
                    TextView rainfall = findViewById(R.id.TxtRainfallValue);
                    TextView temperature = findViewById(R.id.TxtTemperatureValue);
                    TextView place = findViewById(R.id.TxtPlaceValue);

                    rainfall.setText(String.valueOf(dataasset.getAttributes().getRainfall().getValue()));
                    temperature.setText(String.valueOf(dataasset.getAttributes().getTemperature().getValue())+"°C");
                    place.setText(dataasset.getAttributes().getPlace().getValue());

                }
                else{
                    Log.e("API Call", "Failed to retrieve data from the API");
                }
            }
            @Override
            public void onFailure(Call<Asset> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    private void getAssetInfoAQI(){
        Call<Asset> calllasset = apiService.getAsset("6Wo9Lv1Oa1zQleuRVfADP4");
        calllasset.enqueue(new Callback<Asset>() {
            @Override
            public void onResponse(Call<Asset> call, Response<Asset> response) {
                if(response.isSuccessful()){
                    Asset dataasset = response.body();
                    TextView aqi = findViewById(R.id.TxtAQIValue);
                    TextView co2avg = findViewById(R.id.TxtCO2AverageValue);
                    TextView additionalInfo = findViewById(R.id.TxtAdditionalInfo);
                    LinearLayout linearlayoutaqi = findViewById(R.id.LinearLayoutAQI); // Replace with your actual LinearLayout ID

                    aqi.setText(String.valueOf(dataasset.getAttributes().getAQI().getValue()));
                    Log.e("AQI",String.valueOf(dataasset.getAttributes().getAQI().getValue()));
                    co2avg.setText(String.valueOf(dataasset.getAttributes().getCO2Average().getValue()));
                    // Set background color based on AQI value
                    setLinearLayoutBackgroundColor(linearlayoutaqi, dataasset.getAttributes().getAQI().getValue());
                    // Show or hide additional info TextView based on AQI value
                    showAdditionalInfoTextView(additionalInfo, dataasset.getAttributes().getAQI().getValue());
                }else{
                    Log.e("API Call", "Failed to retrieve data from the API");
                }
            }
            @Override
            public void onFailure(Call<Asset> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getUser(){
        Call<UserResponseModel> calllUser = apiService.getUser();
        calllUser.enqueue(new Callback<UserResponseModel>() {
            @Override
            public void onResponse(Call<UserResponseModel> call, Response<UserResponseModel> response) {
                if(response.isSuccessful()){
                    UserResponseModel dataUser = response.body();
                    TextView username = findViewById(R.id.ButtonWelcome);

                    username.setText(String.valueOf(dataUser.getUsername()));

                }
                else{
                    Log.e("API Call", "Failed to retrieve data from the API");
                }
            }
            @Override
            public void onFailure(Call<UserResponseModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    private void setCurrentDate() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(calendar.getTime());
        // Set the current date in the TextView
        currentDateTextView.setText(currentDate);
    }
    private void setLinearLayoutBackgroundColor(LinearLayout linearlayoutaqi, float aqiValue) {
        // Set background color based on AQI value
        if (aqiValue <= 1) {
            linearlayoutaqi.setBackgroundResource(R.color.colorGreen); // Assuming you have a color resource for green
        } else if (aqiValue <= 2) {
            linearlayoutaqi.setBackgroundResource(R.color.colorYellow);
        }
          else if (aqiValue <= 3) {
            linearlayoutaqi.setBackgroundResource(R.color.colorOrange);
        } else if (aqiValue <= 4) {
            // Set a default color or handle other cases
            linearlayoutaqi.setBackgroundResource(R.color.colorRed); // Assuming you have a default color resource
        }
          else {
            linearlayoutaqi.setBackgroundResource(R.color.colorDefault);
        }
    }
    private void showAdditionalInfoTextView(TextView additionalInfoTextView, float aqiValue) {
        // Show additional info TextView if AQI is equal to 1
        if (aqiValue <= 1) {
            additionalInfoTextView.setVisibility(View.VISIBLE);
            additionalInfoTextView.setText("Good");
        } else if (aqiValue <= 2) {
//            additionalInfoTextView.setVisibility(View.GONE);
            additionalInfoTextView.setVisibility(View.VISIBLE);
            additionalInfoTextView.setText("Moderate");
        } else if (aqiValue <= 3) {
//            additionalInfoTextView.setVisibility(View.GONE);
            additionalInfoTextView.setVisibility(View.VISIBLE);
            additionalInfoTextView.setText("Unhealthy");
        } else if (aqiValue <= 4) {
//            additionalInfoTextView.setVisibility(View.GONE);
            additionalInfoTextView.setVisibility(View.VISIBLE);
            additionalInfoTextView.setText("Very Unhealthy");
        } else {
            additionalInfoTextView.setText("Moderate");
            additionalInfoTextView.setVisibility(View.GONE);
        }
    }
    private void recursivelySetLanguage(View view, Resources resources) {
        if (view instanceof TextView) {
            if (view.getId() != View.NO_ID) {
                // Lấy ID của TextView
                int viewId = view.getId();
                CharSequence originalText = ((TextView) view).getText();
                if (originalText instanceof Spanned) {
                    originalText = ((Spanned) originalText).toString();
                }
                String resourceName = resources.getResourceEntryName(viewId);
                Log.d("resourceId", "resourceId: " + originalText);
                int resourceId = resources.getIdentifier(resourceName, "string", getPackageName());

                if (resourceId != 0) {
                    ((TextView) view).setText(resources.getString(resourceId));
                }

            }
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                recursivelySetLanguage(((ViewGroup) view).getChildAt(i), resources);
            }
        }
    }
}