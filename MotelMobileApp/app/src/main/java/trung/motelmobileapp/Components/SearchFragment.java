package trung.motelmobileapp.Components;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
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


public class SearchFragment extends Fragment {

    LinearLayout layout, priceRange, areaRange, searchResult, searchLayout;
    EditText edtMinPrice, edtMaxPrice, edtMinArea, edtMaxArea;
    CheckBox chkPrice, chkArea;
    Button btnSearch, btnSearchAgain;
    RecyclerView rvResult;
    ImageView searchGif;
    boolean validated = true;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_search, container, false);
        searchLayout = layout.findViewById(R.id.search_layout);
        edtMinPrice = layout.findViewById(R.id.filter_min_price);
        edtMaxPrice = layout.findViewById(R.id.filter_max_price);
        edtMinArea = layout.findViewById(R.id.filter_min_area);
        edtMaxArea = layout.findViewById(R.id.filter_max_area);
        chkPrice = layout.findViewById(R.id.chk_following_price);
        chkArea = layout.findViewById(R.id.chk_following_area);
        priceRange = layout.findViewById(R.id.price_range);
        areaRange = layout.findViewById(R.id.area_range);
        btnSearch = layout.findViewById(R.id.btnSearch);
        searchGif = layout.findViewById(R.id.search_gif);
        searchResult = layout.findViewById(R.id.search_result);
        btnSearchAgain = layout.findViewById(R.id.btnSearchAgain);
        rvResult = layout.findViewById(R.id.rv_search_result);
        searchResult.setVisibility(View.GONE);
        Glide.with(getContext()).load(R.drawable.loading).into(searchGif);
        searchGif.setVisibility(View.GONE);
        priceRange.setVisibility(View.GONE);
        areaRange.setVisibility(View.GONE);

        //Events
        chkPrice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton checkBox, boolean isChecked) {
                if (checkBox.isChecked()) {
                    priceRange.setVisibility(View.VISIBLE);
                } else {
                    priceRange.setVisibility(View.GONE);
                }
            }
        });

        chkArea.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton checkBox, boolean isChecked) {
                if (checkBox.isChecked()) {
                    areaRange.setVisibility(View.VISIBLE);
                } else {
                    areaRange.setVisibility(View.GONE);
                }
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean followPrice = chkPrice.isChecked();
                String min_price = edtMinPrice.getText().toString();
                String max_price = edtMaxPrice.getText().toString();
                boolean followArea = chkArea.isChecked();
                String min_area = edtMinArea.getText().toString();
                String max_area = edtMaxArea.getText().toString();

                //validation
                if (followPrice) {
                    if (min_price.isEmpty() && max_price.isEmpty()){
                        Toast.makeText(getContext(), "Phải điền ít nhất một khoảng giá!", Toast.LENGTH_SHORT).show();
                        validated = false;
                    }
                    else {
                        validated = true;
                    }
                    if (!min_price.isEmpty() && !max_price.isEmpty()) {
                        if (Integer.parseInt(min_price) > Integer.parseInt(max_price)) {
                            Toast.makeText(getContext(), "Giá lớn nhất không được thấp hơn giá nhỏ nhất!", Toast.LENGTH_SHORT).show();
                            validated = false;
                        }
                        else {
                            validated = true;
                        }
                    }
                }
                if (followArea) {
                    if (min_area.isEmpty() && max_area.isEmpty()){
                        Toast.makeText(getContext(), "Phải điền ít nhất một khoảng diện tích!", Toast.LENGTH_SHORT).show();
                        validated = false;
                    }
                    else {
                        validated = true;
                    }
                    if (!min_area.isEmpty() && !max_area.isEmpty()) {
                        if (Integer.parseInt(min_area) > Integer.parseInt(max_area)) {
                            Toast.makeText(getContext(), "Diện tích lớn nhất không được thấp hơn diện tích nhỏ nhất!", Toast.LENGTH_SHORT).show();
                            validated = false;
                        }
                        else {
                            validated = true;
                        }
                    }
                }
                if (validated) {
                    searchGif.setVisibility(View.VISIBLE);
                    Ion.with(getContext())
                            .load("GET", Constant.WEB_SERVER + "/post/api/search_post/")
                            .setBodyParameter("follow_price", followPrice + "")
                            .setBodyParameter("min_price", min_price)
                            .setBodyParameter("max_price", max_price)
                            .setBodyParameter("follow_area", followArea + "")
                            .setBodyParameter("min_area", min_area)
                            .setBodyParameter("max_area", max_area)
                            .asJsonArray()
                            .setCallback(new FutureCallback<JsonArray>() {
                                @Override
                                public void onCompleted(Exception e, JsonArray result) {
                                    searchGif.setVisibility(View.GONE);
                                    if (e != null) {
                                        e.printStackTrace();
                                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        searchLayout.setVisibility(View.GONE);
                                        searchResult.setVisibility(View.VISIBLE);
                                        ArrayList<PostDTO> posts = new ArrayList<>();
                                        for (int i = 0; i < result.size(); i++) {
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
                                                            result.get(i).getAsJsonObject().get("room").getAsJsonObject().get("price").getAsInt(),
                                                            result.get(i).getAsJsonObject().get("room").getAsJsonObject().get("area").getAsInt(),
                                                            result.get(i).getAsJsonObject().get("room").getAsJsonObject().get("description").getAsString()
                                                    ),
                                                    DateConverter.getPassedTime(result.get(i).getAsJsonObject().get("request_date").getAsString())
                                            ));
                                        }
                                        if (posts.isEmpty()){
                                            Toast.makeText(getContext(),"Không tìm thấy nhà trọ theo yêu cầu của bạn!", Toast.LENGTH_SHORT).show();
                                        }

                                        //render views
                                        GeneralPostRecyclerViewAdapter searchAdapter = new GeneralPostRecyclerViewAdapter(posts);
                                        LinearLayoutManager llm = new LinearLayoutManager(getContext());
                                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                                        rvResult.setLayoutManager(llm);
                                        rvResult.setAdapter(searchAdapter);

                                        searchAdapter.setItemClickListener(new ItemClickListener<PostDTO>() {
                                            @Override
                                            public void onClick(PostDTO item) {
                                                Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                                                intent.putExtra("Post", item.getId());
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                }
                            });
                }
            }
        });

        btnSearchAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchResult.setVisibility(View.GONE);
                searchLayout.setVisibility(View.VISIBLE);
            }
        });

        return layout;
    }

}
