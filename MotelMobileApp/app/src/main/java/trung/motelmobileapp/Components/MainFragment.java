package trung.motelmobileapp.Components;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import trung.motelmobileapp.Models.PostDTO;
import trung.motelmobileapp.Models.RoomDTO;
import trung.motelmobileapp.Models.UserDTO;
import trung.motelmobileapp.MyTools.Constant;
import trung.motelmobileapp.MyTools.DateConverter;
import trung.motelmobileapp.PostDetailActivity;
import trung.motelmobileapp.R;

public class MainFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    RecyclerView mainRecyclerView;
    Double lat;
    Double lon;
    LinearLayout mainLayout, mainGreeting;
    TextView serverResult, txtGreeting;
    String api;
    SwipeRefreshLayout refresher;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainLayout = (LinearLayout) inflater.inflate(R.layout.fragment_main, container, false);
        serverResult = mainLayout.findViewById(R.id.serverResult);
        mainGreeting = mainLayout.findViewById(R.id.main_greeting);
        txtGreeting = mainLayout.findViewById(R.id.txt_main_greeting);
        mainRecyclerView = mainLayout.findViewById(R.id.rvMain);
        mainGreeting.setVisibility(View.GONE);

        refresher = mainLayout.findViewById(R.id.refresh_main_page);
        refresher.setOnRefreshListener(this);
        refresher.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorAccent, R.color.motelApp, R.color.colorPrimaryDark);
        refresher.post(new Runnable() {
            @Override
            public void run() {
                refresher.setRefreshing(true);
                loadData();
            }
        });
        return mainLayout;
    }

    private void loadData(){
        //location here
        refresher.setRefreshing(true);
        try {
            lat = getArguments().getDouble("Latitude");
            lon = getArguments().getDouble("Longitude");
            if (lat != 0){
                api = "/post/api/get_posts_nearby/";
            }
            else {
                api = "/post/api/get_newest_posts/";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Ion.with(getContext())
                .load("GET", Constant.WEB_SERVER + api)
                .setBodyParameter("lat", lat + "")
                .setBodyParameter("lon", lon + "")
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    //get reponse json and render the result to view
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        refresher.setRefreshing(false);
                        try {
                            if (e != null) {
                                e.printStackTrace();
                                serverResult.setText("Error while getting data!");
                            } else {
                                if (result.size() == 0){
                                    String res = "Chưa có nhà trọ nào được đăng ở quanh đây " +
                                            "\nĐến mục tìm kiếm để tìm nhà trọ ở những thành phố khác";
                                    serverResult.setText(res);
                                }
                                else {
                                    mainGreeting.setVisibility(View.VISIBLE);
                                    //load newest posts if location undefined
                                    if (lat == 0){
                                        txtGreeting.setText("Nhà trọ mới nhất");
                                    }
                                    //get data from json and put to arraylist
                                    ArrayList<PostDTO> posts = new ArrayList<>();
                                    for (int i = 0; i < result.size(); i++) {
                                        ArrayList<String> images = new ArrayList<>();
                                        JsonArray roomImages = result.get(i).getAsJsonObject().get("room").getAsJsonObject().get("images").getAsJsonArray();
                                        for (JsonElement roomImage : roomImages) {
                                            images.add(roomImage.getAsString());
                                        }
                                        posts.add(new PostDTO(
                                                result.get(i).getAsJsonObject().get("_id").getAsString(),
                                                result.get(i).getAsJsonObject().get("title").getAsString(),
                                                new UserDTO(
                                                        result.get(i).getAsJsonObject().get("user").getAsJsonObject().get("name").getAsString(),
                                                        result.get(i).getAsJsonObject().get("user").getAsJsonObject().get("phone").getAsString()
                                                ),
                                                new RoomDTO(
                                                        result.get(i).getAsJsonObject().get("room").getAsJsonObject().get("address").getAsString(),
                                                        result.get(i).getAsJsonObject().get("room").getAsJsonObject().get("city").getAsString(),
                                                        result.get(i).getAsJsonObject().get("room").getAsJsonObject().get("district").getAsString(),
                                                        result.get(i).getAsJsonObject().get("room").getAsJsonObject().get("ward").getAsString(),
                                                        images
                                                ),
                                                DateConverter.getPassedTime(result.get(i).getAsJsonObject().get("request_date").getAsString())
                                        ));
                                    }

                                    //render view

                                    GeneralPostRecyclerViewAdapter mrvAdapter = new GeneralPostRecyclerViewAdapter(posts, getContext());
                                    LinearLayoutManager llm = new LinearLayoutManager(getContext());
                                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                                    mainRecyclerView.setLayoutManager(llm);
                                    mainRecyclerView.setAdapter(mrvAdapter);

                                    //set item listener
                                    mrvAdapter.setItemClickListener(new ItemClickListener<PostDTO>() {
                                        @Override
                                        public void onClick(PostDTO item) {
                                            Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                                            intent.putExtra("Post", item.getId());
                                            startActivity(intent);
                                        }
                                    });
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            serverResult.setText("Data error!");
                        }
                    }
                });
    }

    @Override
    public void onRefresh() {
        loadData();
    }
}
