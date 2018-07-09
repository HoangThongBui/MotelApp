package trung.motelmobileapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AreaChoosingActivity extends AppCompatActivity {

    Spinner spnCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_choosing);
        spnCity = findViewById(R.id.spnCity);
        final String[] cities = {"TPHCM", "Hà Nội"};
        ArrayAdapter<String> spnAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, cities);
        spnCity.setAdapter(spnAdapter);
    }

    public void clickToMainPage(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("City", (String) spnCity.getSelectedItem());
        startActivity(intent);
        finish();
    }
}
