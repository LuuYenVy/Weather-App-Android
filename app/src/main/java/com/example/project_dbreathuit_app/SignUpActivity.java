package com.example.project_dbreathuit_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.*;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.example.project_dbreathuit_app.model.LoginResponseModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity {
    private Button backButton, signupButton;
    private TextView loginButton;
    private TextInputEditText usernameEditText, emailEditText, passEditText, repassEditText;
    //Login for google
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    private ProgressBar progressBar;
    private WebView webView;
    private boolean shouldStopEvaluation = false;
    private SharedPreferences sharedPreferences;
    Context context;
    Resources resources;

    //Validate Format
    private Boolean validateUserName() {
        String val = usernameEditText.getText().toString();
        if (val.isEmpty()) {
            usernameEditText.setError("Field cannot be empty");
            return false;
        }
        else if (val.length() >= 20) {
            usernameEditText.setError("Username too long");
            return false;
        }
        else {
            usernameEditText.setError(null);
            return true;
        }
    }
    private Boolean validateEmail() {
        String val = emailEditText.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()) {
            emailEditText.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            emailEditText.setError("Invalid email address");
            return false;
        } else {
            emailEditText.setError(null);
            return true;
        }
    }
    private Boolean validatePassword() {
        String val = passEditText.getText().toString();
//        String passwordVal = "^" +
//                //"(?=.*[0-9])" +         //at least 1 digit
//                //"(?=.*[a-z])" +         //at least 1 lower case letter
//                //"(?=.*[A-Z])" +         //at least 1 upper case letter
//                "(?=.*[a-zA-Z])" +      //any letter
//                "(?=.*[@#$%^&+=])" +    //at least 1 special character
//                "(?=\\S+$)" +           //no white spaces
//                ".{8,}" +               //at least 4 characters
//                "$";
        if (val.isEmpty()) {
            passEditText.setError("Field cannot be empty");
            return false;
        }
//        else if (val.length() < 8) {
//            passEditText.setError("At least 8 characters");
//            return false;
//        } else if (!val.matches(passwordVal)) {
//            editTextSignUpPassWord.setError("Password is too weak");
//            return false;
        else {
            passEditText.setError(null);
            return true;
        }
    }
    private Boolean validateConfirmPassword() {
        String val = repassEditText.getText().toString();
        String password = passEditText.getText().toString();

        if (val.isEmpty()) {
            repassEditText.setError("Field cannot be empty");
            return false;
        } else if (!val.equals(password)) {
            repassEditText.setError("Passwords do not match");
            return false;
        } else {
            repassEditText.setError(null);
            return true;
        }
    }
    //
    private void login() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://uiot.ixxc.dev/auth/realms/master/protocol/openid-connect/") // Replace with the base URL of your API
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API_SERVICE authService = retrofit.create(API_SERVICE.class);
        String username = usernameEditText.getText().toString();
        String password = passEditText.getText().toString();
        Call<LoginResponseModel> call = authService.login("openremote",username,password,"password");
        call.enqueue(new Callback<LoginResponseModel>() {
            @Override
            public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                if (response.isSuccessful()) {
                    LoginResponseModel loginResponse = response.body();
                    String accessToken = loginResponse.getAccess_token();
                    Log.d("token",accessToken);
                    Toast.makeText(SignUpActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Authentication failed
                }
            }
            @Override
            public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                // Handle network or API errors
                Log.d("API CALL", t.getMessage().toString());
            }
        });
    }
    @SuppressLint("SetJavaScriptEnabled")
    private void loadWebView(String user,String email, String pass,String repass) {
        String myURL = "https://uiot.ixxc.dev/auth/realms/master/protocol/openid-connect/auth?client_id=openremote&redirect_uri=https%3A%2F%2Fuiot.ixxc.dev%2Fmanager%2F&state=7b7ef2b3-64c3-4693-ba35-33e412d3c277&response_mode=fragment&response_type=code&scope=openid&nonce=c6011dc3-ac6e-46c3-9378-33fe07ab9bec";
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookies(null);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(myURL);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.d("Url1",request.getUrl().toString());
                if (request.getUrl().toString().contains("manager/#state")) {
                    Log.d("Url","Go");
                    login();
                    // navigateToLoginMain();
                    return true;
                }
                return false;
            }
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                //Log.d("Url",url);
                return null;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.setVisibility(View.GONE);
                if (url.contains("openid-connect/auth")) {
                    String redirect = "document.getElementsByClassName('btn waves-effect waves-light')[1].click();";
                    view.evaluateJavascript(redirect, null);
                } else if (url.contains("login-actions/registration")){
                    shouldStopEvaluation = false;
                    String helperText = "document.getElementsByClassName('helper-text')[0].getAttribute('data-error');";
                    String redText = "document.getElementsByClassName('red-text')[1].innerText;";
                    view.evaluateJavascript(helperText, s -> {
                        if (s.equals("null")) {
                            view.evaluateJavascript(redText, s1 -> {
                                if (s1.equals("null")){
                                    String userField= "document.getElementById('username').value='" + user + "';";
                                    String emailField= "document.getElementById('email').value='" + email + "';";
                                    String passField= "document.getElementById('password').value='" + pass + "';";
                                    String repassField= "document.getElementById('password-confirm').value='" + repass + "';";

                                    view.evaluateJavascript(userField, null);
                                    view.evaluateJavascript(emailField, null);
                                    view.evaluateJavascript(passField, null);
                                    view.evaluateJavascript(repassField, null);
                                    view.evaluateJavascript("document.getElementsByTagName('form')[0].submit();", null);
                                    Toast.makeText(SignUpActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                } else {
                                    signUpErr(s1);
                                }
                            });
                        } else {
                            signUpErr(s);
                        }
                    });
                }
            }
        });
        webView.setWebChromeClient(new WebChromeClient());
    }
    private void signUpErr(String msg) {
        signupButton.setEnabled(true);
        Toast.makeText(SignUpActivity.this, "Singup Failed" + msg, Toast.LENGTH_SHORT).show();
    }
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        backButton = findViewById(R.id.ButtonBack);
        loginButton = findViewById(R.id.ButtonLogin);
        signupButton =findViewById(R.id.ButtonSignup);
        usernameEditText = findViewById(R.id.usernameEdittext);
        emailEditText = findViewById(R.id.emailEditText);
        passEditText = findViewById(R.id.passEditText);
        repassEditText = findViewById(R.id.repassEditText);
        webView = findViewById(R.id.webView);
        webView.setBackgroundColor(Color.TRANSPARENT);
        progressBar = findViewById(R.id.progressBar);

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
            context = LocaleHelper.setLocale(SignUpActivity.this, savedLanguage);
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
                context = LocaleHelper.setLocale(SignUpActivity.this, language);
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
                context = LocaleHelper.setLocale(SignUpActivity.this, "en");
                resources = context.getResources();
                recursivelySetLanguage(findViewById(android.R.id.content), resources);
            }
        });

        //Function Navigate
        //Back-Navigate
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, OnBoardingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        //Login-Navigate
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        //Signup-Navigate
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!validateUserName() | !validateEmail() | !validatePassword() | !validateConfirmPassword()) {
                    return;
                }
                signupButton.setText("");
                progressBar.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        signupButton.setText("Signup");
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }, 3000);
                signupButton .setEnabled(true);
                final String user = String.valueOf(usernameEditText.getText());
                final String email = String.valueOf(emailEditText.getText());
                final String pass = String.valueOf(passEditText.getText());
                final String repass = String.valueOf(repassEditText.getText());
                loadWebView(user,email,pass,repass);
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
                Toast.makeText(SignUpActivity.this, "Google signed with email :"+email, Toast.LENGTH_SHORT).show();
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign-In failed
                Log.w("TAG", "Google sign in failed" + e.getStatusCode());
                Toast.makeText(SignUpActivity.this, "Google sign in failed", Toast.LENGTH_SHORT).show();
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
        startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
        finish();
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