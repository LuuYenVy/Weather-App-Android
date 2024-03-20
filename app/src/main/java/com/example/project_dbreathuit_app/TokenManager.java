package com.example.project_dbreathuit_app;
import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {
    private static final String PREF_NAME = "TokenPrefs";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    private static final String KEY_EXPIRES_IN = "expires_in";
    private static final String KEY_REFRESH_EXPIRES_IN = "refresh_expires_in";

    private SharedPreferences sharedPreferences;

    public TokenManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveToken(String accessToken, String refreshToken, long expiresIn, long refreshExpiresIn) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ACCESS_TOKEN, accessToken);
        editor.putString(KEY_REFRESH_TOKEN, refreshToken);
        editor.putLong(KEY_EXPIRES_IN, System.currentTimeMillis() + expiresIn * 1000); // Lưu thời gian hết hạn dưới dạng milliseconds
        editor.putLong(KEY_REFRESH_EXPIRES_IN, System.currentTimeMillis() + refreshExpiresIn * 1000); // Lưu thời gian hết hạn của refresh token
        editor.apply();
    }

    public String getAccessToken() {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null);
    }

    public String getRefreshToken() {
        return sharedPreferences.getString(KEY_REFRESH_TOKEN, null);
    }

    public boolean isTokenExpired() {
        long expiresIn = sharedPreferences.getLong(KEY_EXPIRES_IN, 0);
        return System.currentTimeMillis() >= expiresIn;
    }
    public boolean isRefreshTokenExpired() {
        long refreshExpiresIn = sharedPreferences.getLong(KEY_REFRESH_EXPIRES_IN, 0);
        return System.currentTimeMillis() >= refreshExpiresIn;
    }

    public void clearToken() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_ACCESS_TOKEN);
        editor.remove(KEY_REFRESH_TOKEN);
        editor.remove(KEY_EXPIRES_IN);
        editor.remove(KEY_REFRESH_EXPIRES_IN);
        editor.apply();
    }
}