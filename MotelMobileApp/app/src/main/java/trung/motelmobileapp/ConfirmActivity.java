package trung.motelmobileapp;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class ConfirmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_confirm);
    }

    public void clickToYes(View view) {
        setResult(Activity.RESULT_OK);
        finish();
    }

    public void clickToNo(View view) {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}
