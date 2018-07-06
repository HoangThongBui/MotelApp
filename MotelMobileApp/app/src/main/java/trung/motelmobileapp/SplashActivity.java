package trung.motelmobileapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

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
                startActivity(new Intent(getApplicationContext(), AreaChoosingActivity.class));
                finish();
            }
        };
        handler.postDelayed(waitingInSplashScene, 2000);
    }
}
