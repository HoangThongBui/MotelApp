package trung.motelmobileapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import trung.motelmobileapp.Components.DetailCommentRecyclerViewAdapter;
import trung.motelmobileapp.Components.ItemClickListener;
import trung.motelmobileapp.Components.PostDetailImageSliderAdapter;
import trung.motelmobileapp.Models.CommentDTO;
import trung.motelmobileapp.Models.PostDTO;
import trung.motelmobileapp.Models.RoomDTO;
import trung.motelmobileapp.Models.UserDTO;
import trung.motelmobileapp.MyTools.Constant;
import trung.motelmobileapp.MyTools.DateConverter;

public class PostDetailActivity extends AppCompatActivity {

    ScrollView scroll;
    TextView title, username, time, phone, price, address, area, detail;
    RecyclerView commentRecyclerView;
    ImageView loadingCommentGif;
    String postId;
    PostDTO postDetail;
    FloatingActionButton btnToEditPost;
    SharedPreferences mySession;
    ViewPager postImages;
    LinearLayout commentLayout, commentPart;
    TextView loginRequest;
    EditText commentArea;
    GoogleMap map;
    SupportMapFragment mapFragment;
    LocationManager locationManager;
    String locationProvider;
    LatLng roomLocation, currentLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        scroll = findViewById(R.id.post_detail_scroll);
        title = findViewById(R.id.post_title);
        username = findViewById(R.id.post_user);
        time = findViewById(R.id.post_time);
        phone = findViewById(R.id.post_phone);
        price = findViewById(R.id.post_price);
        area = findViewById(R.id.post_area);
        address = findViewById(R.id.post_address);
        detail = findViewById(R.id.post_detail);
        postImages = findViewById(R.id.post_images);
        btnToEditPost = findViewById(R.id.btnToEditPost);
        commentLayout = findViewById(R.id.comment_layout);
        loginRequest = findViewById(R.id.login_request);
        commentArea = findViewById(R.id.post_detail_comment_editor);
        commentPart = findViewById(R.id.comment_area);
        mySession = getSharedPreferences(Constant.MY_SESSION, Context.MODE_PRIVATE);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (mySession.getString("user_id", "").isEmpty()) {
            commentLayout.setVisibility(View.GONE);
        } else {
            loginRequest.setVisibility(View.GONE);
        }

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
                            if (result.toString().equals("{}")) {
                                Toast.makeText(getApplicationContext(), "Bài đăng đã bị xoá hoặc từ chối!", Toast.LENGTH_SHORT).show();
                                Intent backToMain = new Intent(getApplicationContext(), MainActivity.class);
                                backToMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(backToMain);
                                finish();
                                return;
                            }
                            ArrayList<String> images = new ArrayList<>();
                            JsonArray roomImages = result.get("room").getAsJsonObject().get("images").getAsJsonArray();
                            for (JsonElement roomImage : roomImages) {
                                images.add(roomImage.getAsString());
                            }
                            postDetail = new PostDTO(
                                    result.get("_id").getAsString(),
                                    result.get("title").getAsString(),
                                    new UserDTO(
                                            result.get("user").getAsJsonObject().get("_id").getAsString(),
                                            result.get("user").getAsJsonObject().get("name").getAsString(),
                                            result.get("user").getAsJsonObject().get("phone").getAsString(),
                                            result.get("user").getAsJsonObject().get("image").getAsString()
                                    ),
                                    new RoomDTO(
                                            result.get("room").getAsJsonObject().get("address").getAsString(),
                                            result.get("room").getAsJsonObject().get("city").getAsString(),
                                            result.get("room").getAsJsonObject().get("district").getAsString(),
                                            result.get("room").getAsJsonObject().get("ward").getAsString(),
                                            result.get("room").getAsJsonObject().get("price").getAsInt(),
                                            result.get("room").getAsJsonObject().get("area").getAsInt(),
                                            result.get("room").getAsJsonObject().get("description").getAsString(),
                                            images
                                    ),
                                    DateConverter.getPassedTime(result.get("request_date").getAsString()),
                                    result.get("status").getAsString()
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
                            postImages.setAdapter(new PostDetailImageSliderAdapter(postDetail.getRoom().getImages(), getApplicationContext()));

                            //Only owner can edit post
                            if (!mySession.getString("user_id", "").equals(postDetail.getUser().getId())) {
                                btnToEditPost.setVisibility(View.GONE);
                            }

                            //create map
                            mapFragment.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(GoogleMap googleMap) {
                                    map = googleMap;
                                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                    try {
                                        List<Address> geocodeAddress = geocoder.getFromLocationName(postDetail.getRoom().getFullAddress(), 5);
                                        roomLocation = new LatLng(geocodeAddress.get(0).getLatitude(), geocodeAddress.get(0).getLongitude());
                                        map.addMarker(new MarkerOptions().position(roomLocation).title(postDetail.getTitle())
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.house_marker)));
                                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(roomLocation, 16));
                                        if (checkLocationPermission()) {
                                            if (!getBestEnabledLocationProvider().equals("passive")) {
                                                map.setMyLocationEnabled(true);
                                                map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                                                    @Override
                                                    public boolean onMyLocationButtonClick() {
                                                        currentLocation = new LatLng(map.getMyLocation().getLatitude(), map.getMyLocation().getLongitude());
                                                        map.addMarker(new MarkerOptions().position(currentLocation));
                                                        LatLngBounds area;
                                                        try {
                                                            area = new LatLngBounds(currentLocation, roomLocation);
                                                        } catch (Exception e) {
                                                            area = new LatLngBounds(roomLocation, currentLocation);
                                                        }
                                                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(area.getCenter(), 10));
                                                        return false;
                                                    }
                                                });
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(getApplicationContext(), "Không xác định được vị trí trên bản đồ!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                            if (postDetail.getStatus().equals("u")) {
                                commentPart.setVisibility(View.GONE);
                                return;
                            }
                            loadComments();
                        }
                    }
                });
    }

    private void loadComments() {
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
                                loadingCommentResult.setVisibility(View.VISIBLE);
                                loadingCommentResult.setText("Chưa có bình luận nào về bài đăng này.");
                            } else {
                                loadingCommentResult.setVisibility(View.GONE);
                            }
                            ArrayList<CommentDTO> comments = new ArrayList<>();
                            for (int i = 0; i < result.size(); i++) {
                                comments.add(new CommentDTO(
                                        result.get(i).getAsJsonObject().get("_id").getAsString(),
                                        result.get(i).getAsJsonObject().get("post").getAsString(),
                                        new UserDTO(
                                                result.get(i).getAsJsonObject().get("user").getAsJsonObject().get("_id").getAsString(),
                                                result.get(i).getAsJsonObject().get("user").getAsJsonObject().get("name").getAsString(),
                                                result.get(i).getAsJsonObject().get("user").getAsJsonObject().get("phone").getAsString(),
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

                            //delete button
                            dcrvAdapter.setCommentItemClickListener(new ItemClickListener<CommentDTO>() {
                                @Override
                                public void onClick(CommentDTO item) {
                                    Intent deleteCommentActivity = new Intent(getApplicationContext(), ConfirmActivity.class);
                                    deleteCommentActivity.putExtra("comment_id", item.getId());
                                    startActivityForResult(deleteCommentActivity, Constant.REQUEST_ID_FOR_DELETE_COMMENT);
                                }
                            });
                        }
                        loadingCommentGif.setVisibility(View.GONE);
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
        switch (requestCode) {
            case Constant.REQUEST_ID_FOR_EDIT_POST:
                if (resultCode == Activity.RESULT_OK) {
                    recreate();
                }
                break;
            case Constant.REQUEST_ID_FOR_DELETE_COMMENT:
                if (resultCode == Activity.RESULT_OK) {
                    String comment_id = data.getStringExtra("comment_id");
                    Ion.with(getApplicationContext())
                            .load("DELETE", Constant.WEB_SERVER + "/comment/api/delete_comment/" + comment_id)
                            .asString()
                            .setCallback(new FutureCallback<String>() {
                                @Override
                                public void onCompleted(Exception e, String result) {
                                    if (e != null) {
                                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        switch (result) {
                                            case "Comment deleted!":
                                                loadComments();
                                                scroll.fullScroll(View.FOCUS_DOWN);
                                                break;
                                            default:
                                                Toast.makeText(getApplicationContext(), "Có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
                                                break;
                                        }
                                    }
                                }
                            });
                }
        }
    }

    public void clickToGoToLogin(View view) {
        Intent backToMain = new Intent(getApplicationContext(), MainActivity.class);
        backToMain.putExtra("Login Request", "Login");
        backToMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(backToMain);
    }

    public void clickToComment(View view) {
        String comment = commentArea.getText().toString();
        if (isCommentValidated(comment)) {
            String userId = mySession.getString("user_id", "");
            String postId = postDetail.getId();
            Ion.with(getApplicationContext())
                    .load("POST", Constant.WEB_SERVER + "/comment/api/post_a_comment/")
                    .setBodyParameter("post_id", postId)
                    .setBodyParameter("user_id", userId)
                    .setBodyParameter("detail", comment)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            if (e != null) {
                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                            } else {
                                switch (result) {
                                    case "Comment posted!":
                                        loadComments();
                                        scroll.fullScroll(View.FOCUS_DOWN);
                                        break;
                                    default:
                                        Toast.makeText(getApplicationContext(), "Có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Bình luận không được để trống!", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isCommentValidated(String comment) {
        return !comment.isEmpty();
    }

    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED;
    }

    private String getBestEnabledLocationProvider() {
        String bestProvider;
        Criteria criteria = new Criteria();
        bestProvider = locationManager.getBestProvider(criteria, true);
        return bestProvider;
    }
}
