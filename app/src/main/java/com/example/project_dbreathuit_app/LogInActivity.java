package com.example.project_dbreathuit_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_dbreathuit_app.model.LoginResponseModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LogInActivity extends AppCompatActivity {
    //Login for google
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    //
    private TextInputEditText usernameEditText, passwordEditText;
    private Button backButton, loginButton, forgotButton;
    private CheckBox rememberMeCheckbox;
    private SharedPreferences sharedPreferences;
    private TextView signupButton;
    private ProgressBar progressBar;
    private TokenManager tokenManager;
    private TextView loginTextView;
    Context context;
    Resources resources;
    private Boolean validateEmail() {
        String val = usernameEditText.getText().toString();
        if (val.isEmpty()) {
            usernameEditText.setError("Email cannot be empty");
            return false;
        } else {
            usernameEditText.setError(null);
            return true;
        }
    }

    private Boolean validatePassWord() {
        String val = passwordEditText.getText().toString();
        if (val.isEmpty()) {
            passwordEditText.setError("Password cannot be empty");
            return false;
        } else {
            passwordEditText.setError(null);
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.txtUserNameLogin);
        passwordEditText = findViewById(R.id.passPassLogin);
        loginButton = findViewById(R.id.ButtonLogin);
        rememberMeCheckbox =findViewById(R.id.rememberMeCheckbox);
        signupButton = findViewById(R.id.ButtonSignup);
        forgotButton = findViewById(R.id.forgotpass);
        backButton = findViewById(R.id.ButtonBack);
        progressBar = findViewById(R.id.progressBar);
        tokenManager = new TokenManager(this);
        //Function navigate
        //Back-Navigate
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, OnBoardingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        //
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
            context = LocaleHelper.setLocale(LogInActivity.this, savedLanguage);
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
                context = LocaleHelper.setLocale(LogInActivity.this, language);
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
                context = LocaleHelper.setLocale(LogInActivity.this, "en");
                resources = context.getResources();
                recursivelySetLanguage(findViewById(android.R.id.content), resources);
            }
        });

        //SignUp-Navigate
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        //
        //ForgotPassWord-Navigate
        forgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        //LoginForGoogle-Navigate
        findViewById(R.id.ButtonGoogle).setOnClickListener(view -> signIn());
        // Configure Google Sign-In options
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        //
        //Validate Format
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateEmail() | !validatePassWord()) {

                } else {
                    loginButton.setText("");
                    progressBar.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loginButton.setText("Login");
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }, 3000);
//                    loadingDialog.startLoadingDialog();
//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            loadingDialog.dismissDialog();
//                        }
//                    },2000);
                    login();
                }

            }
        });
    }
    //Handle login for google
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN );
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign-In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                String email = account.getEmail();
                Toast.makeText(LogInActivity.this, "Google signed with email :"+email, Toast.LENGTH_SHORT).show();
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign-In failed
                Log.w("TAG", "Google sign in failed" + e.getStatusCode());
                Toast.makeText(LogInActivity.this, "Google sign in failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        // You can use the GoogleSignInAccount to authenticate with your backend server
        // or save the user's information locally.
        String idToken = acct.getIdToken();
        String displayName = acct.getDisplayName();
        String email = acct.getEmail();
        // TODO: Implement your own authentication logic here
        // For example, you can start a new activity after successful login
        startActivity(new Intent(LogInActivity.this, LogInActivity.class));
        finish();
    }

    //Handle login
    private void login() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://uiot.ixxc.dev/auth/realms/master/protocol/openid-connect/") // Replace with the base URL of your API
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API_SERVICE authService = retrofit.create(API_SERVICE.class);
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        Call<LoginResponseModel> call = authService.login("openremote",username,password,"password");
        call.enqueue(new Callback<LoginResponseModel>() {
            @Override
            public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                if (response.isSuccessful()) {
                    LoginResponseModel loginResponse = response.body();
                    String accessToken = loginResponse.getAccess_token();
                    String refresh_token = loginResponse.getRefresh_token();
                    long expires_in = loginResponse.getExpires_in();
                    long refresh_expires_in = loginResponse.getExpires_in();
                    tokenManager.saveToken(accessToken, refresh_token, expires_in, refresh_expires_in);
                    Toast.makeText(LogInActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LogInActivity.this, MapActivity.class);
                    startActivity(intent);
                }
                else {
                    // Authentication failed
                    Toast.makeText(LogInActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                // Handle network or API errors
                Log.d("API CALL", t.getMessage().toString());
                Toast.makeText(LogInActivity.this, "Login Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    //
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