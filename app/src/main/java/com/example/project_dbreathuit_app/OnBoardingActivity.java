package com.example.project_dbreathuit_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

//
public class OnBoardingActivity extends AppCompatActivity {
    private Button loginButton, signupButton;
    private TokenManager tokenManager;
    Context context;
    Resources resources;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        loginButton = findViewById(R.id.ButtonLogin);
        signupButton = findViewById(R.id.ButtonSignup);
        tokenManager = new TokenManager(this);

        // Set spinner and change language
        List<SpinnerItem> items = new ArrayList<>();
        items.add(new SpinnerItem(R.drawable.englishlanguage, "English"));
        items.add(new SpinnerItem(R.drawable.vietnamlanguage, "VietNam"));
        items.add(new SpinnerItem(R.drawable.francelanguage, "France"));
        items.add(new SpinnerItem(R.drawable.thailandlanguage, "ThaiLand"));
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, items);
        Spinner spinner = findViewById(R.id.ChooseLanguage);
        spinner.setAdapter(adapter);
        SharedPreferences preferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
        String savedLanguage = preferences.getString("language", "");

        if (!savedLanguage.isEmpty()) {
            context = LocaleHelper.setLocale(OnBoardingActivity.this, savedLanguage);
            resources = context.getResources();
            recursivelySetLanguage(findViewById(android.R.id.content), resources);
            if(savedLanguage.equals("en")){
                spinner.setSelection(0);
            } else if (savedLanguage.equals("vi")) {
                spinner.setSelection(1);
            } else  if (savedLanguage.equals("fr"))  {
                spinner.setSelection(2);
            }
            else {
                spinner.setSelection(3);
            }
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                SpinnerItem Item = adapter.getItem(position);
                String selectedItem = Item.getText();
                String language="en";
                if (selectedItem.equals("VietNam")) {
                    language="vi";
                } else if (selectedItem.equals("English")) {
                    language="en";
                } else if(selectedItem.equals("France")){
                    language="fr";
                }
                else{
                    language="th";
                }
                context = LocaleHelper.setLocale(OnBoardingActivity.this, language);
                resources = context.getResources();

                recursivelySetLanguage(findViewById(android.R.id.content), resources);
                Log.d("LanguageSelection", "Selected Language: " + language);

                SharedPreferences preferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("language", language);
                editor.apply();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                context = LocaleHelper.setLocale(OnBoardingActivity.this, "en");
                resources = context.getResources();
                recursivelySetLanguage(findViewById(android.R.id.content), resources);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OnBoardingActivity.this, LogInActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OnBoardingActivity.this, SignUpActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        if (tokenManager.getAccessToken() != null && !tokenManager.isTokenExpired()) {
            // User is already logged in and the token is not expired
            // Proceed to the main activity
            //navigateDashBoard();
            navigateMap();
            String accessToken = tokenManager.getAccessToken();
        } else {
            tokenManager.clearToken();
        }
    }
    private void navigateMap() {
        Intent intent = new Intent(this, MapActivity.class);
        // Clear the existing task and start a new task
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();  // Finish the current activity
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

