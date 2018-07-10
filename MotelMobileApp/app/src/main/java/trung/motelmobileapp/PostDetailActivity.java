package trung.motelmobileapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import trung.motelmobileapp.Models.PostDTO;

public class PostDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        TextView detail = findViewById(R.id.post_detail);
        PostDTO dto = (PostDTO) getIntent().getSerializableExtra("Post");
        detail.setText(dto.toString());
    }
}
