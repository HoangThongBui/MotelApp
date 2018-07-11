package trung.motelmobileapp.Components;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import trung.motelmobileapp.Models.PostDTO;
import trung.motelmobileapp.Models.RoomDTO;
import trung.motelmobileapp.Models.UserDTO;
import trung.motelmobileapp.MyTools.Constant;
import trung.motelmobileapp.MyTools.DateConverter;
import trung.motelmobileapp.PostDetailActivity;
import trung.motelmobileapp.R;

public class MainFragment extends Fragment {

    private RecyclerView mainRecyclerView;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final LinearLayout mainLayout = (LinearLayout) inflater.inflate(R.layout.fragment_main, container, false);
        final ImageView loadingGif = mainLayout.findViewById(R.id.loadingGif);
        final TextView serverResult = mainLayout.findViewById(R.id.serverResult);
        final LinearLayout mainGreeting = mainLayout.findViewById(R.id.main_greeting);
        mainGreeting.setVisibility(View.GONE);
        TextView mainGreetingCity = mainLayout.findViewById(R.id.main_greeting_city);
        Glide.with(getContext()).load(R.drawable.loading).into(loadingGif);
        try {
            final String city = getArguments().getString("City");
            mainGreetingCity.setText(city);
            Ion.with(getContext())
                    .load("GET", "http://" + Constant.WEBSERVER_IP_ADDRESS + ":" + Constant.WEBSERVER_PORT + "/post/api/get_posts_by_city/")
                    .setBodyParameter("city", city)
                    .asJsonArray()
                    .setCallback(new FutureCallback<JsonArray>() {
                        //get reponse json and render the result to view
                        @Override
                        public void onCompleted(Exception e, JsonArray result) {
                            try {
                                if (e != null) {
                                    e.printStackTrace();
                                    serverResult.setText("Error while getting data!");
                                } else {
                                    if (result.size() == 0){
                                        String res = "Chưa có nhà trọ nào được đăng ở " + city + "." +
                                                "\nĐến mục tìm kiếm để tìm nhà trọ ở những thành phố khác";
                                        serverResult.setText(res);
                                    }
                                    else {
                                        mainGreeting.setVisibility(View.VISIBLE);
                                        //get data from json and put to arraylist
                                        ArrayList<PostDTO> posts = new ArrayList<>();
                                        for (int i = 0; i < result.size(); i++) {
                                            posts.add(new PostDTO(
                                                    result.get(i).getAsJsonObject().get("_id").getAsString(),
                                                    result.get(i).getAsJsonObject().get("title").getAsString(),
                                                    new UserDTO(result.get(i).getAsJsonObject().get("user").getAsJsonObject().get("fname").getAsString() + " " +
                                                            result.get(i).getAsJsonObject().get("user").getAsJsonObject().get("lname").getAsString(),
                                                            result.get(i).getAsJsonObject().get("user").getAsJsonObject().get("phone").getAsString()),
                                                    new RoomDTO(
                                                            result.get(i).getAsJsonObject().get("room").getAsJsonObject().get("address").getAsString(),
                                                            result.get(i).getAsJsonObject().get("room").getAsJsonObject().get("city").getAsString(),
                                                            result.get(i).getAsJsonObject().get("room").getAsJsonObject().get("district").getAsString(),
                                                            result.get(i).getAsJsonObject().get("room").getAsJsonObject().get("ward").getAsString(),
                                                            result.get(i).getAsJsonObject().get("room").getAsJsonObject().get("price").getAsInt(),
                                                            result.get(i).getAsJsonObject().get("room").getAsJsonObject().get("detail").getAsString()
                                                    ),
                                                    DateConverter.formattedDate(result.get(i).getAsJsonObject().get("request_date").getAsString())
                                            ));

                                        }

                                        //render view
                                        mainRecyclerView = mainLayout.findViewById(R.id.rvMain);
                                        GeneralPostRecyclerViewAdapter mrvAdapter = new GeneralPostRecyclerViewAdapter(posts);
                                        LinearLayoutManager llm = new LinearLayoutManager(getContext());
                                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                                        mainRecyclerView.setLayoutManager(llm);
                                        mainRecyclerView.setAdapter(mrvAdapter);

                                        //set item listener
                                        mrvAdapter.setItemClickListener(new ItemClickListener<PostDTO>() {
                                            @Override
                                            public void onClick(PostDTO item) {
                                                Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                                                intent.putExtra("Post", item);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                serverResult.setText("Data error!");
                            }
                            loadingGif.setVisibility(View.GONE); // hide loading gif
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mainLayout;
    }
}
