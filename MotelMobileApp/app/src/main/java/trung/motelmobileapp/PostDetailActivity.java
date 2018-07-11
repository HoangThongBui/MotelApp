package trung.motelmobileapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import trung.motelmobileapp.Components.DetailCommentRecyclerViewAdapter;
import trung.motelmobileapp.Models.CommentDTO;
import trung.motelmobileapp.Models.PostDTO;
import trung.motelmobileapp.Models.UserDTO;
import trung.motelmobileapp.MyTools.Constant;
import trung.motelmobileapp.MyTools.DateConverter;

public class PostDetailActivity extends AppCompatActivity {

    private TextView title, username, time, phone, price, address, detail;
    private RecyclerView commentRecyclerView;
    private ImageView loadingCommentGif;

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
        String displayingPrice = postDetail.getRoom().getPrice() + " VNĐ/tháng";
        price.setText(displayingPrice);
        String displayingAddress =
                postDetail.getRoom().getAddress() + ", P." +
                postDetail.getRoom().getWard() + ", Q." +
                postDetail.getRoom().getDistrict() + ", " +
                postDetail.getRoom().getCity();
        address.setText(displayingAddress);
        detail.setText(postDetail.getRoom().getDetail());

        //loading comments from server
        loadingCommentGif = findViewById(R.id.loading_comment_gif);
        Glide.with(getApplicationContext()).load(R.drawable.loading).into(loadingCommentGif);
        final TextView loadingCommentResult = findViewById(R.id.loading_comment_result);
        Ion.with(getApplicationContext())
           .load("GET","http://" + Constant.WEBSERVER_IP_ADDRESS + ":" + Constant.WEBSERVER_PORT + "/comment/api/get_comments/" + postDetail.getId())
           .asJsonArray()
           .setCallback(new FutureCallback<JsonArray>() {
               @Override
               public void onCompleted(Exception e, JsonArray result) {
                    if (e != null){
                        e.printStackTrace();
                        loadingCommentResult.setText(e.toString());
                    }
                    else {
                        if (result.size() == 0){
                            loadingCommentResult.setText("Chưa có bình luận nào về bài đăng này.");
                        }
                        else {
                            loadingCommentResult.setVisibility(View.GONE);
                            ArrayList<CommentDTO> comments = new ArrayList<>();
                            for (int i = 0; i < result.size(); i++) {
                                comments.add(new CommentDTO(
                                        result.get(i).getAsJsonObject().get("_id").getAsString(),
                                        result.get(i).getAsJsonObject().get("post").getAsString(),
                                        new UserDTO(
                                                result.get(i).getAsJsonObject().get("user").getAsJsonObject().get("name").getAsString()
                                        ),
                                        result.get(i).getAsJsonObject().get("detail").getAsString(),
                                        DateConverter.formattedDate(result.get(i).getAsJsonObject().get("comment_time").getAsString())
                                ));
                            }

                            //render comments
                            commentRecyclerView = findViewById(R.id.detail_comments);
                            DetailCommentRecyclerViewAdapter dcrvAdapter = new DetailCommentRecyclerViewAdapter(comments);
                            LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                            llm.setOrientation(LinearLayoutManager.VERTICAL);
                            commentRecyclerView.setLayoutManager(llm);
                            commentRecyclerView.setAdapter(dcrvAdapter);
                        }

                    }
                    loadingCommentGif.setVisibility(View.GONE);
               }
           });
    }

    public void backToMainPage(View view) {
        finish();
    }
}
