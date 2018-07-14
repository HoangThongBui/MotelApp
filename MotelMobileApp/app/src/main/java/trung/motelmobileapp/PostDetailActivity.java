package trung.motelmobileapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import trung.motelmobileapp.Components.DetailCommentRecyclerViewAdapter;
import trung.motelmobileapp.Components.TabAdapter;
import trung.motelmobileapp.Models.CommentDTO;
import trung.motelmobileapp.Models.PostDTO;
import trung.motelmobileapp.Models.RoomDTO;
import trung.motelmobileapp.Models.UserDTO;
import trung.motelmobileapp.MyTools.Constant;
import trung.motelmobileapp.MyTools.DateConverter;

public class PostDetailActivity extends AppCompatActivity {

    TextView title, username, time, phone, price, address, area, detail;
    RecyclerView commentRecyclerView;
    ImageView loadingCommentGif;
    String postId;
    FloatingActionButton btnToEditPost;
    SharedPreferences mySession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        title = findViewById(R.id.post_title);
        username = findViewById(R.id.post_user);
        time = findViewById(R.id.post_time);
        phone = findViewById(R.id.post_phone);
        price = findViewById(R.id.post_price);
        area = findViewById(R.id.post_area);
        address = findViewById(R.id.post_address);
        detail = findViewById(R.id.post_detail);
        btnToEditPost = findViewById(R.id.btnToEditPost);
        mySession = getSharedPreferences(Constant.MY_SESSION, Context.MODE_PRIVATE);

        //Get data
        postId = getIntent().getStringExtra("Post");
        Ion.with(this)
                .load("GET", Constant.WEB_SERVER + "/post/api/get_post_by_id/" + postId)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null) {
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        } else {
                            PostDTO postDetail = null;
                            postDetail = new PostDTO(
                                    result.get("_id").getAsString(),
                                    result.get("title").getAsString(),
                                    new UserDTO(
                                            result.get("user").getAsJsonObject().get("_id").getAsString(),
                                            result.get("user").getAsJsonObject().get("name").getAsString(),
                                            result.get("user").getAsJsonObject().get("phone").getAsString()
                                    ),
                                    new RoomDTO(
                                            result.get("room").getAsJsonObject().get("address").getAsString(),
                                            result.get("room").getAsJsonObject().get("city").getAsString(),
                                            result.get("room").getAsJsonObject().get("district").getAsString(),
                                            result.get("room").getAsJsonObject().get("ward").getAsString(),
                                            result.get("room").getAsJsonObject().get("price").getAsInt(),
                                            result.get("room").getAsJsonObject().get("area").getAsInt(),
                                            result.get("room").getAsJsonObject().get("description").getAsString()
                                    ),
                                    DateConverter.getPassedTime(result.get("request_date").getAsString())
                            );

                            //Render view
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
                            String displayingArea = postDetail.getRoom().getArea() + " m2";
                            area.setText(displayingArea);
                            detail.setText(postDetail.getRoom().getDescription());

                            //Only owner can edit post
                            if (!mySession.getString("user_id", "").equals(postDetail.getUser().getId())) {
                                btnToEditPost.setVisibility(View.GONE);
                            }

                            //loading comments from server
                            loadingCommentGif = findViewById(R.id.loading_comment_gif);
                            Glide.with(getApplicationContext()).load(R.drawable.loading).into(loadingCommentGif);
                            final TextView loadingCommentResult = findViewById(R.id.loading_comment_result);
                            Ion.with(getApplicationContext())
                                    .load("GET", Constant.WEB_SERVER + "/comment/api/get_comments/" + postId)
                                    .asJsonArray()
                                    .setCallback(new FutureCallback<JsonArray>() {
                                        @Override
                                        public void onCompleted(Exception e, JsonArray result) {
                                            if (e != null) {
                                                e.printStackTrace();
                                                loadingCommentResult.setText(e.toString());
                                            } else {
                                                if (result.size() == 0) {
                                                    loadingCommentResult.setText("Chưa có bình luận nào về bài đăng này.");
                                                } else {
                                                    loadingCommentResult.setVisibility(View.GONE);
                                                    ArrayList<CommentDTO> comments = new ArrayList<>();
                                                    for (int i = 0; i < result.size(); i++) {
                                                        comments.add(new CommentDTO(
                                                                result.get(i).getAsJsonObject().get("_id").getAsString(),
                                                                result.get(i).getAsJsonObject().get("post").getAsString(),
                                                                new UserDTO(
                                                                        result.get(i).getAsJsonObject().get("user").getAsJsonObject().get("name").getAsString(),
                                                                        result.get(i).getAsJsonObject().get("user").getAsJsonObject().get("image").getAsString()
                                                                ),
                                                                result.get(i).getAsJsonObject().get("detail").getAsString(),
                                                                DateConverter.formattedDate(result.get(i).getAsJsonObject().get("comment_time").getAsString())
                                                        ));
                                                    }

                                                    //render comments
                                                    commentRecyclerView = findViewById(R.id.detail_comments);
                                                    DetailCommentRecyclerViewAdapter dcrvAdapter = new DetailCommentRecyclerViewAdapter(comments, getApplicationContext());
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
                    }
                });
    }

    public void backToMainPage(View view) {
        finish();
    }

    public void clickToEditPost(View view) {
        Intent intent = new Intent(getApplicationContext(), EditPostActivity.class);
        intent.putExtra("Post", postId);
        startActivityForResult(intent, Constant.REQUEST_ID_FOR_EDIT_POST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_ID_FOR_EDIT_POST) {
            if (resultCode == Activity.RESULT_OK) {
                recreate();
            }
        }
    }
}
