package trung.motelmobileapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import trung.motelmobileapp.MyTools.Constant;

public class ErrorActivity extends AppCompatActivity {

    private LinearLayout errorLayout;
    private ImageView tryingAgainGif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        errorLayout = findViewById(R.id.error_layout);
        tryingAgainGif = findViewById(R.id.try_again_gif);
        Glide.with(getApplicationContext()).load(R.drawable.loading).into(tryingAgainGif);
        tryingAgainGif.setVisibility(View.GONE);
    }

    public void clickToCheckServer(View view) {
        errorLayout.setVisibility(View.GONE);
        tryingAgainGif.setVisibility(View.VISIBLE);
        Ion.with(getApplicationContext())
                .load("GET",Constant.WEB_SERVER)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e == null) {
                            if (result.equals("Server is online!")){
                                Toast.makeText(getApplicationContext(), "Kết nối thành công!", Toast.LENGTH_SHORT).show();
                                setResult(Activity.RESULT_OK);
                                finish();
                            }
                            else {
                                errorLayout.setVisibility(View.VISIBLE);
                                tryingAgainGif.setVisibility(View.GONE);
                            }
                        }
                        else {
                            errorLayout.setVisibility(View.VISIBLE);
                            tryingAgainGif.setVisibility(View.GONE);
                        }
                    }
                });
    }
}
