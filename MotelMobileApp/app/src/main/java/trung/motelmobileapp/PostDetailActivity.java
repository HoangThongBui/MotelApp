package trung.motelmobileapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import trung.motelmobileapp.Models.PostDTO;

public class PostDetailActivity extends AppCompatActivity {

    private TextView title, username, time, phone, price, address, detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        //Render data
        PostDTO postDetail = (PostDTO) getIntent().getSerializableExtra("Post");
        title = findViewById(R.id.post_title);
        username = findViewById(R.id.post_user);
        time = findViewById(R.id.post_time);
        phone = findViewById(R.id.post_phone);
        price = findViewById(R.id.post_price);
        address = findViewById(R.id.post_address);
        detail = findViewById(R.id.post_detail);

        title.setText(postDetail.getTitle());
        String displayingUsername = "Đăng bởi " + postDetail.getUser().getName();
        username.setText(displayingUsername);
        time.setText(postDetail.getRequest_date());
        phone.setText(postDetail.getUser().getPhone());
        String displayingPrice = postDetail.getPrice() + " VNĐ/tháng";
        price.setText(displayingPrice);
        String displayingAddress =
                postDetail.getAddress() + ", P." +
                postDetail.getWard() + ", Q." +
                postDetail.getDistrict() + ", " +
                postDetail.getCity();
        address.setText(displayingAddress);
        detail.setText(postDetail.getDetail());

        //render comments
    }

    public void backToMainPage(View view) {
        finish();
    }
}
