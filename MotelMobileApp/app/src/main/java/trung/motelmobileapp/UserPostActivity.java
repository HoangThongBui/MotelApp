package trung.motelmobileapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import trung.motelmobileapp.Components.GeneralPostRecyclerViewAdapter;
import trung.motelmobileapp.Components.ItemClickListener;
import trung.motelmobileapp.Models.PostDTO;
import trung.motelmobileapp.Models.RoomDTO;
import trung.motelmobileapp.Models.UserDTO;
import trung.motelmobileapp.MyTools.Constant;
import trung.motelmobileapp.MyTools.DateConverter;

public class UserPostActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayout userPosts;
    ImageView userPostGif;
    UserDTO user;
    ArrayList<PostDTO> confirmedPosts, unconfirmedPosts;
    Button btnConfirmed,btnUnconfirmed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_post);
        user = (UserDTO) getIntent().getSerializableExtra("User");
        userPostGif = findViewById(R.id.user_posts_gif);
        userPosts = findViewById(R.id.user_posts);
        recyclerView = findViewById(R.id.rv_user_posts);
        btnConfirmed = findViewById(R.id.btnConfirmed);
        btnUnconfirmed = findViewById(R.id.btnUnconfirmed);
        Glide.with(this).load(R.drawable.loading).into(userPostGif);
        userPosts.setVisibility(View.GONE);

        //Load user post
        Ion.with(this)
           .load("GET", "http://" + Constant.WEBSERVER_IP_ADDRESS + ":" + Constant.WEBSERVER_PORT +
                                    "/post/api/get_posts_by_user/" + user.getId())
           .asJsonArray()
           .setCallback(new FutureCallback<JsonArray>() {
               @Override
               public void onCompleted(Exception e, JsonArray result) {
                   userPostGif.setVisibility(View.GONE);
                   userPosts.setVisibility(View.VISIBLE);
                    if (e != null){
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        confirmedPosts = new ArrayList<>();
                        unconfirmedPosts = new ArrayList<>();
                        for (int i = 0 ; i < result.size(); i++){
                            PostDTO post = new PostDTO(
                                    result.get(i).getAsJsonObject().get("_id").getAsString(),
                                    result.get(i).getAsJsonObject().get("title").getAsString(),
                                    user,
                                    new RoomDTO(
                                            result.get(i).getAsJsonObject().get("room").getAsJsonObject().get("address").getAsString(),
                                            result.get(i).getAsJsonObject().get("room").getAsJsonObject().get("city").getAsString(),
                                            result.get(i).getAsJsonObject().get("room").getAsJsonObject().get("district").getAsString(),
                                            result.get(i).getAsJsonObject().get("room").getAsJsonObject().get("ward").getAsString(),
                                            result.get(i).getAsJsonObject().get("room").getAsJsonObject().get("price").getAsInt(),
                                            result.get(i).getAsJsonObject().get("room").getAsJsonObject().get("detail").getAsString()
                                    ),
                                    DateConverter.getPassedTime(result.get(i).getAsJsonObject().get("request_date").getAsString()),
                                    result.get(i).getAsJsonObject().get("status").getAsBoolean()
                            );
                            if (post.isConfirmed()){
                                confirmedPosts.add(post);
                            }
                            else {
                                unconfirmedPosts.add(post);
                            }
                        }

                        //render 2 views
                        btnConfirmed.setTextColor(getResources().getColor(R.color.colorWhite));
                        btnConfirmed.setBackgroundColor(getResources().getColor(R.color.motelApp));
                        btnUnconfirmed.setTextColor(getResources().getColor(R.color.colorBlack));
                        btnUnconfirmed.setBackgroundColor(getResources().getColor(R.color.colorGrey));
                        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                        GeneralPostRecyclerViewAdapter dataAdapter = new GeneralPostRecyclerViewAdapter(confirmedPosts);
                        recyclerView.setLayoutManager(llm);
                        recyclerView.setAdapter(dataAdapter);
                        dataAdapter.setItemClickListener(new ItemClickListener<PostDTO>() {
                            @Override
                            public void onClick(PostDTO item) {
                                Intent intent = new Intent(getApplicationContext(), PostDetailActivity.class);
                                intent.putExtra("Post", item);
                                startActivity(intent);
                            }
                        });
                    }
               }
           });
    }

    public void backToProfilePage(View view) {
        finish();
    }

    public void clickToLoadConfirmedPost(View view) {
        btnConfirmed.setTextColor(getResources().getColor(R.color.colorWhite));
        btnConfirmed.setBackgroundColor(getResources().getColor(R.color.motelApp));
        btnUnconfirmed.setTextColor(getResources().getColor(R.color.colorBlack));
        btnUnconfirmed.setBackgroundColor(getResources().getColor(R.color.colorGrey));
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        GeneralPostRecyclerViewAdapter dataAdapter = new GeneralPostRecyclerViewAdapter(confirmedPosts);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(dataAdapter);
        dataAdapter.setItemClickListener(new ItemClickListener<PostDTO>() {
            @Override
            public void onClick(PostDTO item) {
                Intent intent = new Intent(getApplicationContext(), PostDetailActivity.class);
                intent.putExtra("Post", item);
                startActivity(intent);
            }
        });
    }

    public void clickToLoadUnconfirmedPost(View view) {
        btnUnconfirmed.setTextColor(getResources().getColor(R.color.colorWhite));
        btnUnconfirmed.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        btnConfirmed.setTextColor(getResources().getColor(R.color.colorBlack));
        btnConfirmed.setBackgroundColor(getResources().getColor(R.color.colorGrey));
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        GeneralPostRecyclerViewAdapter dataAdapter = new GeneralPostRecyclerViewAdapter(unconfirmedPosts);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(dataAdapter);
        dataAdapter.setItemClickListener(new ItemClickListener<PostDTO>() {
            @Override
            public void onClick(PostDTO item) {
                Intent intent = new Intent(getApplicationContext(), PostDetailActivity.class);
                intent.putExtra("Post", item);
                startActivity(intent);
            }
        });
    }
}
