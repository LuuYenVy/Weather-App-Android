package com.example.project_dbreathuit_app;
import android.os.Handler;
import android.text.Spanned;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_dbreathuit_app.model.Asset;
import com.example.project_dbreathuit_app.model.UserResponseModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    private API_SERVICE apiService;
    private TokenManager tokenManager;
    private Timer timer;
    private Handler handler;
    private TextView profile;
    private TextView changelanguage;
    Context context;
    Resources resources;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        handler = new Handler();

        RelativeLayout logoutLayout = findViewById(R.id.LayoutLogout);
        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        SharedPreferences preferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
        String savedLanguage = preferences.getString("language", "");

        if (!savedLanguage.isEmpty()) {
            context = LocaleHelper.setLocale(ProfileActivity.this, savedLanguage);
            resources = context.getResources();
            recursivelySetLanguage(findViewById(android.R.id.content), resources);}
        profile=findViewById(R.id.profile2);
        profile.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();

            }

        });
        changelanguage=findViewById(R.id.Setting);
        changelanguage.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                MyDialogFragment dialogFragment = new MyDialogFragment();
                dialogFragment.show(getSupportFragmentManager(), "Dialog to change language");

            }

        });
        tokenManager = new TokenManager(this);
        apiService  = API_CLIENT.getClient("https://uiot.ixxc.dev/",tokenManager.getAccessToken()).create(API_SERVICE.class);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_profile);

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
                startActivity(new Intent(getApplicationContext(), ChartActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_profile) {
                return true;
            }

            return false;
        });

        scheduleApiCall();
    }
    private void logout() {
        // Clear tokens and restart the app
        stopApiCallTimer(); // Stop the timer
        tokenManager.clearToken();

        Intent intent = new Intent(ProfileActivity.this, LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//
        // Restart the app
        startActivity(intent);
        finish();
        System.exit(0); // Optional: Terminate the current process to ensure a clean restart
    }
    private void scheduleApiCall() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    getUser();
                });
            }
        }, 0, 5000); // 5000 milliseconds = 5 seconds
    }
    private void getUser(){
        Call<UserResponseModel> calllUser = apiService.getUser();
        calllUser.enqueue(new Callback<UserResponseModel>() {
            @Override
            public void onResponse(Call<UserResponseModel> call, Response<UserResponseModel> response) {
                if(response.isSuccessful()){
                    UserResponseModel dataUser = response.body();
                    TextView username = findViewById(R.id.txtName);
                    TextView email = findViewById(R.id.txtEmail);

                    username.setText(String.valueOf(dataUser.getUsername()));
                    email.setText(String.valueOf(dataUser.getEmail()));

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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove callbacks to prevent memory leaks
        handler.removeCallbacksAndMessages(null);
    }
    private void stopApiCallTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
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