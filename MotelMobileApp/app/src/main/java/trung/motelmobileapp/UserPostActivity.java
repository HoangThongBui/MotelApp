package trung.motelmobileapp;

import android.app.Activity;
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
import trung.motelmobileapp.Components.UserPostRecyclerViewAdapter;
import trung.motelmobileapp.Models.PostDTO;
import trung.motelmobileapp.Models.RoomDTO;
import trung.motelmobileapp.Models.UserDTO;
import trung.motelmobileapp.MyTools.Constant;
import trung.motelmobileapp.MyTools.DateConverter;

public class UserPostActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView userPostGif;
    UserDTO user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_post);
        user = (UserDTO) getIntent().getSerializableExtra("User");
        userPostGif = findViewById(R.id.user_posts_gif);
        recyclerView = findViewById(R.id.rv_user_posts);
        Glide.with(this).load(R.drawable.loading).into(userPostGif);

        //Load user post
        Ion.with(this)
           .load("GET", Constant.WEB_SERVER + "/post/api/get_posts_by_user/" + user.getId())
           .asJsonArray()
           .setCallback(new FutureCallback<JsonArray>() {
               @Override
               public void onCompleted(Exception e, JsonArray result) {
                   userPostGif.setVisibility(View.GONE);
                    if (e != null){
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        ArrayList<PostDTO> userPosts = new ArrayList<>();
                        for (int i = 0 ; i < result.size(); i++){
                            userPosts.add( new PostDTO(
                                    result.get(i).getAsJsonObject().get("_id").getAsString(),
                                    result.get(i).getAsJsonObject().get("title").getAsString(),
                                    user,
                                    new RoomDTO(
                                            result.get(i).getAsJsonObject().get("room").getAsJsonObject().get("address").getAsString(),
                                            result.get(i).getAsJsonObject().get("room").getAsJsonObject().get("city").getAsString(),
                                            result.get(i).getAsJsonObject().get("room").getAsJsonObject().get("district").getAsString(),
                                            result.get(i).getAsJsonObject().get("room").getAsJsonObject().get("ward").getAsString(),
                                            result.get(i).getAsJsonObject().get("room").getAsJsonObject().get("price").getAsInt(),
                                            result.get(i).getAsJsonObject().get("room").getAsJsonObject().get("area").getAsInt(),
                                            result.get(i).getAsJsonObject().get("room").getAsJsonObject().get("description").getAsString()
                                    ),
                                    DateConverter.getPassedTime(result.get(i).getAsJsonObject().get("request_date").getAsString()),
                                    result.get(i).getAsJsonObject().get("status").getAsString()
                            ));
                        }

                        if(userPosts.isEmpty()){
                            Toast.makeText(getApplicationContext(), "Bạn chưa có bài đăng nào!",Toast.LENGTH_SHORT).show();
                        }

                        //render 2 views
                        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                        UserPostRecyclerViewAdapter dataAdapter = new UserPostRecyclerViewAdapter(userPosts);
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

    public void clickToMakeNewPost(View view) {
        startActivityForResult(new Intent(getApplicationContext(), MakeNewPostActivity.class), 1);
    }

    //re-render if make new post
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (resultCode == Activity.RESULT_OK){
                recreate();
            }
        }
    }
}
