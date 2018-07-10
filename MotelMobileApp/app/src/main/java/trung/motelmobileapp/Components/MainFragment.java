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
        Glide.with(getContext()).load(R.drawable.loading).into(loadingGif);
        try {
            String city = getArguments().getString("City");
            Ion.with(getContext())
                    .load("GET", "http://" + Constant.WEBSERVER_IP_ADDRESS + ":" + Constant.WEBSERVER_PORT + "/post/api/get_posts_by_city/" + city)
                    .asJsonArray()
                    .setCallback(new FutureCallback<JsonArray>() {
                        //get reponse json and render the result to view
                        @Override
                        public void onCompleted(Exception e, JsonArray result) {
                            try {
                                if (e != null) {
                                    e.printStackTrace();
                                    serverResult.setText("Server connection error!");
                                } else {
                                    //get data from json and put to arraylist
                                    ArrayList<PostDTO> posts = new ArrayList<>();
                                    for (int i = 0; i < result.size(); i++) {
                                        posts.add(new PostDTO(
                                                result.get(i).getAsJsonObject().get("_id").getAsString(),
                                                result.get(i).getAsJsonObject().get("title").getAsString(),
                                                new UserDTO(result.get(i).getAsJsonObject().get("user_id").getAsJsonObject().get("fname").getAsString() + " " +
                                                        result.get(i).getAsJsonObject().get("user_id").getAsJsonObject().get("lname").getAsString()),
                                                result.get(i).getAsJsonObject().get("address").getAsString(),
                                                result.get(i).getAsJsonObject().get("city").getAsString(),
                                                result.get(i).getAsJsonObject().get("district").getAsString(),
                                                result.get(i).getAsJsonObject().get("ward").getAsString(),
                                                result.get(i).getAsJsonObject().get("price").getAsInt(),
                                                result.get(i).getAsJsonObject().get("detail").getAsString(),
                                                DateConverter.formattedDate(result.get(i).getAsJsonObject().get("request_date").getAsString())
                                        ));
                                    }

                                    //render view
                                    mainRecyclerView = mainLayout.findViewById(R.id.rvMain);
                                    MainFragmentRecyclerViewAdapter mrvAdapter = new MainFragmentRecyclerViewAdapter(posts);
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
                            } catch (Exception ex) {
                                serverResult.setText(ex.toString());
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
