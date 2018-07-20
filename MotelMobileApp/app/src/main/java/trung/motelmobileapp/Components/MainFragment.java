package trung.motelmobileapp.Components;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

public class MainFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, LocationListener {

    RecyclerView mainRecyclerView;
    Double lat = 0.0, lon = 0.0;
    LinearLayout mainLayout, mainGreeting;
    TextView txtGreeting;
    String api;
    SwipeRefreshLayout refresher;
    Boolean locationPermission = false;
    LocationManager locationManager;
    String locationProvider = "";

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainLayout = (LinearLayout) inflater.inflate(R.layout.fragment_main, container, false);
        mainGreeting = mainLayout.findViewById(R.id.main_greeting);
        txtGreeting = mainLayout.findViewById(R.id.txt_main_greeting);
        mainRecyclerView = mainLayout.findViewById(R.id.rvMain);
        mainGreeting.setVisibility(View.GONE);
        refresher = mainLayout.findViewById(R.id.refresh_main_page);
        refresher.setOnRefreshListener(this);
        refresher.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorAccent, R.color.motelApp, R.color.colorPrimaryDark);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        checkLocationAccessPermission();
        return mainLayout;
    }

    private void checkLocationServiceAndFetchData() {
        if (locationPermission) {
            if (usingLocationService()) {
                refresher.setRefreshing(true);
            } else {
                //location service turn off
                api = "/post/api/get_newest_posts/";
                loadDataFromServer();
            }
        } else {
            //location permission denied
            api = "/post/api/get_newest_posts/";
            loadDataFromServer();
        }
    }

    private boolean usingLocationService() {
        locationProvider = getBestEnabledLocationProvider();
        if (locationProvider.equals("passive")) {
            Toast.makeText(getContext(), "Mở dịch vụ định vị để tìm nhà trọ gần đây.", Toast.LENGTH_SHORT).show();
            return false;
        }
        try {
            locationManager.requestLocationUpdates(locationProvider, 0, 5000, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void checkLocationAccessPermission() {
        int accessCoarsePermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        int accessFinePermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (accessCoarsePermission != PackageManager.PERMISSION_GRANTED || accessFinePermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    Constant.REQUEST_ID_ACCESS_COURSE_FINE_LOCATION);
            return;
        }
        locationPermission = true;
        checkLocationServiceAndFetchData();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constant.REQUEST_ID_ACCESS_COURSE_FINE_LOCATION:
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    locationPermission = true;
                } else {
                    Toast.makeText(getContext(), "Bạn cần cho phép quyền định vị nếu muốn tìm nhà trọ gần đây.", Toast.LENGTH_SHORT).show();
                }
                checkLocationServiceAndFetchData();
                break;
        }
    }

    private String getBestEnabledLocationProvider() {
        String bestProvider;
        Criteria criteria = new Criteria();
        bestProvider = locationManager.getBestProvider(criteria, true);
        return bestProvider;
    }

    private void loadDataFromServer() {
        //location here
        refresher.setRefreshing(true);
        switch (api) {
            case "/post/api/get_posts_nearby/":
                txtGreeting.setText("Nhà trọ gần đây");
                break;
            case "/post/api/get_newest_posts/":
                txtGreeting.setText("Nhà trọ mới đăng");
                break;
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
                        mainGreeting.setVisibility(View.VISIBLE);
                        if (e != null) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
                        } else {
                            if (result.size() == 0) {
                                String res = "Chưa có nhà trọ nào được đăng ở quanh đây " +
                                        "\nĐến mục tìm kiếm để tìm nhà trọ ở những thành phố khác";
                                txtGreeting.setText(res);
                            } else {

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
                    }
                });
    }

    @Override
    public void onRefresh() {
        checkLocationServiceAndFetchData();
    }

    @Override
    public void onLocationChanged(Location location) {
        api = "/post/api/get_posts_nearby/";
        lat = location.getLatitude();
        lon = location.getLongitude();
        loadDataFromServer();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
