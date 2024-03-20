package com.example.project_dbreathuit_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_dbreathuit_app.model.Asset;
import com.google.android.material.textfield.TextInputEditText;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity {
    private MapView mapView;
    private API_SERVICE apiService;
    private TokenManager tokenManager;
    private double longitude=0;
    private double latitude=0;
    private Timer timer;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        tokenManager = new TokenManager(this);
        apiService  = API_CLIENT.getClient("https://uiot.ixxc.dev/",tokenManager.getAccessToken()).create(API_SERVICE.class);
        // Thiết lập thư viện osmdroid
        Configuration.getInstance().setUserAgentValue(getPackageName());

        // Thiết lập MapView
        mapView = findViewById(R.id.map);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.getController().setZoom(20);

        // Hiển thị bản đồ tại vị trí cụ thể
        GeoPoint startPoint = new GeoPoint(10.869778736885038, 106.80280655508835); // Ví dụ vị trí ở TP.HCM
        Marker marker = new Marker(mapView);
        marker.setPosition(startPoint);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(marker);
        InfoWindow infoWindow = new MarkerInfoWindow(R.layout.bonuspack_bubble, mapView);
        marker.setInfoWindow(infoWindow);


        GeoPoint startPoint1 = new GeoPoint(10.869905172970164, 106.80345028525176); // Ví dụ vị trí ở TP.HCM
        Marker marker1 = new Marker(mapView);
        mapView.getController().setCenter(startPoint1);
        marker1.setPosition(startPoint1);
        marker1.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(marker1);
        mapView.invalidate();

        marker1.setInfoWindow(infoWindow);
        // Xử lý sự kiện khi người dùng nhấn vào marker
        marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {

                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.bonuspack_bubble, null);
                handler = new Handler();
                // Lên lịch gọi API để làm mới mỗi 5 giây
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (!isFinishing()) {
                            handler.post(() -> {
                                apiService = API_CLIENT.getClient("https://uiot.ixxc.dev/", tokenManager.getAccessToken()).create(API_SERVICE.class);
                                getAssetInfo(popupView);
                            });
                        }
                    }
                }, 0, 500000);


                Call<Asset> calllasset = apiService.getAsset("5zI6XqkQVSfdgOrZ1MyWEf");
                calllasset.enqueue(new Callback<Asset>() {
                    @Override
                    public void onResponse(Call<Asset> call, Response<Asset> response) {
                        if(response.isSuccessful()){
                            Asset dataasset = response.body();


                        }else{
                            Log.e("API Call", "Failed to retrieve data from the API with accessid 5zI6XqkQVSfdgOrZ1MyWEf");
                        }
                    }
                    @Override
                    public void onFailure(Call<Asset> call, Throwable t) {
                        t.printStackTrace();
                    }
                });

                // Tạo một PopupWindow và hiển thị nó tại vị trí của marker
                PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                popupWindow.setAnimationStyle(R.style.PopupAnimation);
                popupWindow.showAtLocation(mapView, Gravity.BOTTOM, 0, 0);
                // Xử lý sự kiện khi người dùng tương tác với popup (nếu cần)
                // Ví dụ: Đóng popup khi người dùng nhấn vào nút đóng
                Button viewButton = popupView.findViewById(R.id.BtnCloseAndView);
                viewButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        Intent i = new Intent(getApplicationContext(), DashBoardMainActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
                return true;

            }
        });
        marker1.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker1, MapView mapView) {

                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.bonuspack_bubble, null);
                handler = new Handler();
                // Lên lịch gọi API để làm mới mỗi 5 giây
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (!isFinishing()) {
                            handler.post(() -> {
                                apiService = API_CLIENT.getClient("https://uiot.ixxc.dev/", tokenManager.getAccessToken()).create(API_SERVICE.class);
                                getAssetInfo(popupView);
                            });
                        }
                    }
                }, 0, 500000);


                Call<Asset> calllasset = apiService.getAsset("5zI6XqkQVSfdgOrZ1MyWEf");
                calllasset.enqueue(new Callback<Asset>() {
                    @Override
                    public void onResponse(Call<Asset> call, Response<Asset> response) {
                        if(response.isSuccessful()){
                            Asset dataasset = response.body();


                        }else{
                            Log.e("API Call", "Failed to retrieve data from the API with accessid 5zI6XqkQVSfdgOrZ1MyWEf");
                        }
                    }
                    @Override
                    public void onFailure(Call<Asset> call, Throwable t) {
                        t.printStackTrace();
                    }
                });

                // Tạo một PopupWindow và hiển thị nó tại vị trí của marker
                PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                popupWindow.setAnimationStyle(R.style.PopupAnimation);
                popupWindow.showAtLocation(mapView, Gravity.BOTTOM, 0, 0);
                // Xử lý sự kiện khi người dùng tương tác với popup (nếu cần)
                // Ví dụ: Đóng popup khi người dùng nhấn vào nút đóng
                Button viewButton = popupView.findViewById(R.id.BtnCloseAndView);
                viewButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        Intent i = new Intent(getApplicationContext(), DashBoardMainActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
                return true;

            }
        });
    }
    private void getAssetInfo(View popupView){
        Call<Asset> calllasset = apiService.getAsset("6Wo9Lv1Oa1zQleuRVfADP4");
        calllasset.enqueue(new Callback<Asset>() {
            @Override
            public void onResponse(Call<Asset> call, Response<Asset> response) {
                if(response.isSuccessful()){
                    Asset dataasset = response.body();
                    TextView co2 = popupView.findViewById(R.id.TxtCO2Value);
                    TextView no = popupView.findViewById(R.id.TxtNOValue);
                    TextView no2 = popupView.findViewById(R.id.TxtNO2Value);
                    TextView o3 = popupView.findViewById(R.id.TxtO3Value);
                    TextView pm10 = popupView.findViewById(R.id.TxtPM10Value);
                    TextView pm25 = popupView.findViewById(R.id.TxtPM25Value);
                    TextView so2 = popupView.findViewById(R.id.TxtSO2Value);
//                    TextInputEditText IcInputPlace = popupView.findViewById(R.id.IcInputPlace);

                    co2.setText(String.valueOf(dataasset.getAttributes().getCO2().getValue()));
                    no.setText(String.valueOf(dataasset.getAttributes().getNO().getValue()));
                    no2.setText(String.valueOf(dataasset.getAttributes().getNO2().getValue()));
                    o3.setText(String.valueOf(dataasset.getAttributes().getO3().getValue()));
                    pm10.setText(String.valueOf(dataasset.getAttributes().getPM10().getValue()));
                    pm25.setText(String.valueOf(dataasset.getAttributes().getPM25().getValue()));
                    so2.setText(String.valueOf(dataasset.getAttributes().getSO2().getValue()));
                    Log.e("CO2",String.valueOf(dataasset.getAttributes().getCO2().getValue()));

                }else{

                    Log.e("API Call", "Failed to retrieve data from the API with accessID 6Wo9Lv1Oa1zQleuRVfADP4");
                }
            }
            @Override
            public void onFailure(Call<Asset> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
