package com.example.project_dbreathuit_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_dbreathuit_app.model.UserResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AccountActivity extends AppCompatActivity {
    private API_SERVICE apiService;
    private TokenManager tokenManager;
    Context context;
    Resources resources;
    private Button backButton ;
    String token="Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJoREkwZ2hyVlJvaE5zVy1wSXpZeDBpT2lHMzNlWjJxV21sRk4wWGE1dWkwIn0.eyJleHAiOjE3MDIyMjAzMTMsImlhdCI6MTcwMjEzMzkxMywiYXV0aF90aW1lIjoxNzAyMTMzOTEzLCJqdGkiOiI0Y2RkZWI0My1mZjU3LTQxYzItYTY2MC02OWI5MzNhNTViMGMiLCJpc3MiOiJodHRwczovL3Vpb3QuaXh4Yy5kZXYvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjY2NDc2MGQxLTU5MjgtNDdjOS04YTJlLThmMDgyYmU3MDJiNCIsInR5cCI6IkJlYXJlciIsImF6cCI6Im9wZW5yZW1vdGUiLCJzZXNzaW9uX3N0YXRlIjoiZGJiODM2YTItMzkzOC00Y2RhLWE2MjYtYmExNDM2MGM5M2NhIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovL3Vpb3QuaXh4Yy5kZXYiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtbWFzdGVyIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoicHJvZmlsZSBlbWFpbCIsInNpZCI6ImRiYjgzNmEyLTM5MzgtNGNkYS1hNjI2LWJhMTQzNjBjOTNjYSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwicHJlZmVycmVkX3VzZXJuYW1lIjoieWVudnkxMjMiLCJlbWFpbCI6Inl2eTEyM0BnbWFpbC5jb20ifQ.ctcyuPsHYziU9KPrzqNgMwuac3hWG6DUITtnqC59AH5AMemP_URag0RwmflITNTDBPF4wFaOjt7ayXZz5YaYRFFftaaOHhSMbbRQxphr7D4LKKa4y-4QDanCvuxINLZSltXDcPsBYFa985zRqilJ6zsBTzGlN1I-H6qcAtm3GMcpgvcZ4BozQNwSpW2nSdptI3LHlKeWwSMFucHd8z_x-ypkKUm_nKCMTT-5ATFNGSRAADN2hqPOl41hGtLhAZBJ04pfStI8AVQ8eW_FCgIftBBSMVhoo-ohkjN50LcM5x_Bu9JZwuMIkkucdFoe1dZq16FAJqN-VRqf5nUQL6RxoA";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
        String savedLanguage = preferences.getString("language", "");

        if (!savedLanguage.isEmpty()) {
            context = LocaleHelper.setLocale(AccountActivity.this, savedLanguage);
            resources = context.getResources();
            recursivelySetLanguage(findViewById(android.R.id.content), resources);}
        tokenManager = new TokenManager(this);
        apiService = API_CLIENT.getClient("https://uiot.ixxc.dev/", tokenManager.getAccessToken()).create(API_SERVICE.class);
        setContentView(R.layout.activity_account);
        TextView iduser = findViewById(R.id.iduser);
        TextView username = findViewById(R.id.username);
        TextView email= findViewById(R.id.emailadd);
        TextView date = findViewById(R.id.create_on);
        TextView status =findViewById(R.id.status);
        TextView username1=findViewById(R.id.txtName);
        TextView email2=findViewById(R.id.txtEmail);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://uiot.ixxc.dev/api/master/user/user/") // Replace with the base URL of your API
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API_SERVICE authService = retrofit.create(API_SERVICE.class);
        Call<UserResponseModel> calllUser = apiService.getUser();
        calllUser.enqueue(new Callback<UserResponseModel>() {
            @Override
            public void onResponse(Call<UserResponseModel> call, Response<UserResponseModel> response) {
                if (response.isSuccessful()) {
                    UserResponseModel profile = response.body();
                    iduser.setText( profile.id);
                    username.setText(profile.username);
                    email.setText(profile.email);
                    date.setText(profile.createdOn);
                    status.setText(profile.enabled);
                    username1.setText(profile.username);
                    email2.setText(profile.email);

                }
                else {
                    Toast.makeText(AccountActivity.this, "Fail to take user information", Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onFailure(Call<UserResponseModel> call, Throwable t) {
                // Handle network or API errors
                Log.d("API call:", t.getMessage().toString());
            }
        });
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, ProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }
    private void startActivityWithAnimation() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);

        // Apply custom animations
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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

