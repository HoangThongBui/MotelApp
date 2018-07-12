package trung.motelmobileapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import trung.motelmobileapp.MyTools.Constant;

public class SplashActivity extends AppCompatActivity {

    public Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        final Runnable waitingInSplashScene = new Runnable() {
            @Override
            public void run() {
                Ion.with(getApplicationContext())
                   .load("GET", Constant.WEB_SERVER)
                   .asString()
                   .setCallback(new FutureCallback<String>() {
                       @Override
                       public void onCompleted(Exception e, String result) {
                           if (e == null) {
                               if (result.equals("Server is online!")){
                                   runApp();
                               }
                               else {
                                    goToError();
                               }
                           }
                           else {
                               goToError();
                           }
                       }
                   });
            }
        };
        handler.postDelayed(waitingInSplashScene, 2000);
    }

    private void runApp(){
        startActivity(new Intent(getApplicationContext(), AreaChoosingActivity.class));
        finish();
    }

    private void goToError(){
        startActivity(new Intent(getApplicationContext(), ErrorActivity.class));
        finish();
    }
}
