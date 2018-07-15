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

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        Ion.with(getApplicationContext())
                .load("GET", Constant.WEB_SERVER)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e == null) {
                            if (result.equals("Server is online!")) {
                                run();
                            } else {
                                goToError();
                            }
                        } else {
                            goToError();
                        }
                    }
                });
    }


    private void goToError() {
        startActivityForResult(new Intent(getApplicationContext(), ErrorActivity.class), Constant.REQUEST_ID_CHECK_CONNECTION_TO_SERVER);
    }

    private void run(){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_ID_CHECK_CONNECTION_TO_SERVER) {
            if (resultCode == Activity.RESULT_OK) {
                run();
                finish();
            }
        }
    }
}
