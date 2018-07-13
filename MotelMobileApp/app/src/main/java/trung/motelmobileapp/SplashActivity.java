package trung.motelmobileapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.List;

import trung.motelmobileapp.MyTools.Constant;

public class SplashActivity extends AppCompatActivity implements LocationListener {

    Location currentLocation;
    LocationManager locationManager;
    String locationProvider;
    LinearLayout locationLoadingDisplay;
    ImageView locationLoadingGif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        locationLoadingDisplay = findViewById(R.id.location_loading_display);
        locationLoadingGif = findViewById(R.id.location_loading_gif);
        Glide.with(this).load(R.drawable.locating).into(locationLoadingGif);
        locationLoadingDisplay.setVisibility(View.GONE);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Ion.with(getApplicationContext())
                .load("GET", Constant.WEB_SERVER)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e == null) {
                            if (result.equals("Server is online!")) {
                                checkPermissionAndRunApp();
                            } else {
                                goToError();
                            }
                        } else {
                            goToError();
                        }
                    }
                });
    }

    private String getBestEnabledLocationProvider() {
        String bestProvider;
        Criteria criteria = new Criteria();
        bestProvider = locationManager.getBestProvider(criteria, true);
        return bestProvider;
    }

    private void goToError() {
        startActivityForResult(new Intent(getApplicationContext(), ErrorActivity.class), Constant.REQUEST_ID_CHECK_CONNECTION_TO_SERVER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_ID_CHECK_CONNECTION_TO_SERVER) {
            if (resultCode == Activity.RESULT_OK) {
                checkPermissionAndRunApp();
            }
        }
    }

    private void checkPermissionAndRunApp() {
        //Ask permission for API >= 23, not ask if permitted
        int accessCoarsePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int accessFinePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (accessCoarsePermission != PackageManager.PERMISSION_GRANTED || accessFinePermission != PackageManager.PERMISSION_GRANTED) {
            //Permissions
            String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
            // Display confirm dialog
            ActivityCompat.requestPermissions(this, permissions, Constant.REQUEST_ID_ACCESS_COURSE_FINE_LOCATION);
            return;
        }
        requestForCurrentLocationAndRunApp();
    }

    private void runApp() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        if (currentLocation != null) {
            intent.putExtra("Latitude", currentLocation.getLatitude());
            intent.putExtra("Longitude", currentLocation.getLongitude());
        }
        startActivity(intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constant.REQUEST_ID_ACCESS_COURSE_FINE_LOCATION: {
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    requestForCurrentLocationAndRunApp();
                } else {
                    Toast.makeText(getApplicationContext(), "Bạn cần cho phép quyền định vị nếu muốn tìm nhà trọ gần đây.", Toast.LENGTH_SHORT).show();
                    runApp();
                }
                break;
            }
        }
    }
    //Get current location (Only call when having permission for location)

    private void requestForCurrentLocationAndRunApp() {
        locationProvider = getBestEnabledLocationProvider();
        if (locationProvider.equals("passive")) {
            locationProvider = null;
        }
        //check the location service is on or not
        if (locationProvider == null) {
            Toast.makeText(getApplicationContext(), "Bạn cần mở định vị để tìm nhà trọ gần đây!", Toast.LENGTH_SHORT).show();
            runApp();
        } else {
            try {
                locationLoadingDisplay.setVisibility(View.VISIBLE);
                locationManager.requestLocationUpdates(locationProvider, 0, 5000, this);
            }
            // Catch Security exception with API >= 23
            catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }
    //Get location service provider enabled(high, medium, low,...)

    @Override
    public void onLocationChanged(Location location) {
        locationLoadingDisplay.setVisibility(View.GONE);
        currentLocation = location;
        runApp();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
