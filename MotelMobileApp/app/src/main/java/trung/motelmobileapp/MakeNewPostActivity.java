package trung.motelmobileapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.body.FilePart;
import com.koushikdutta.async.http.body.Part;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import trung.motelmobileapp.Components.NewPostImageAreaAdapter;
import trung.motelmobileapp.Components.OnAddNewImageClickListener;
import trung.motelmobileapp.MyTools.Constant;

public class MakeNewPostActivity extends AppCompatActivity {

    EditText edtTitle, edtAddress, edtCity, edtDistrict, edtWard, edtPrice, edtArea, edtDescription;
    Button btnAddPost;
    ImageView addingGif;
    String validationError = "";
    SharedPreferences mySession;
    RecyclerView chosenImages;
    NewPostImageAreaAdapter npiaAdapter;
    int currentImage = 0;
    String title, address, city, district, ward, price, area, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_new_post);
        edtTitle = findViewById(R.id.new_post_title);
        edtAddress = findViewById(R.id.new_post_address);
        edtCity = findViewById(R.id.new_post_city);
        edtDistrict = findViewById(R.id.new_post_district);
        edtWard = findViewById(R.id.new_post_ward);
        edtPrice = findViewById(R.id.new_post_price);
        edtArea = findViewById(R.id.new_post_area);
        edtDescription = findViewById(R.id.new_post_description);
        btnAddPost = findViewById(R.id.btnAddPost);
        addingGif = findViewById(R.id.add_new_post_gif);
        chosenImages = findViewById(R.id.rv_new_images);
        mySession = getSharedPreferences(Constant.MY_SESSION, Context.MODE_PRIVATE);
        Glide.with(this).load(R.drawable.loading).into(addingGif);
        addingGif.setVisibility(View.GONE);

        //render image view
        npiaAdapter = new NewPostImageAreaAdapter(getApplicationContext());
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        chosenImages.setLayoutManager(llm);
        chosenImages.setAdapter(npiaAdapter);

        npiaAdapter.setImageListener(new OnAddNewImageClickListener() {

            @Override
            public void onAdd(int currentPosition) {
                currentImage = currentPosition;
                startActivityForResult(new Intent(getApplicationContext(), ChooseImageSourceActivity.class),
                        Constant.REQUEST_ID_FOR_GO_TO_CHOOSE_IMAGE);
            }

            @Override
            public void onRemove(int currentPosition) {
                npiaAdapter.removeImage(currentPosition);
                if (currentPosition == Constant.MAX_POST_IMAGE - 1) {
                    npiaAdapter.getImages().add("");
                }
                npiaAdapter.notifyDataSetChanged();
            }
        });
    }

    public void backToUserPostPage(View view) {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Constant.REQUEST_ID_FOR_GO_TO_CHOOSE_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    npiaAdapter.addImage(data.getStringExtra("Image File"), currentImage);
                    if (currentImage == Constant.MAX_POST_IMAGE - 1) {
                        npiaAdapter.getImages().remove(Constant.MAX_POST_IMAGE);
                    }
                    npiaAdapter.notifyDataSetChanged();
                }
                break;
            case Constant.REQUEST_ID_FOR_ADD_POST:
                if (resultCode == Activity.RESULT_OK) {
                    //Start add post
                    String user_id = mySession.getString("user_id", "");
                    btnAddPost.setVisibility(View.GONE);
                    addingGif.setVisibility(View.VISIBLE);

                    ArrayList<String> uploadImages = new ArrayList<>();
                    for (String imageLink : npiaAdapter.getImages()) {
                        if (!imageLink.isEmpty()){
                            uploadImages.add(imageLink);
                        }
                    }

                    List<Part> images = new ArrayList<>();
                    for (String imageLink : uploadImages) {
                        images.add(new FilePart("room_images" , new File(imageLink)));
                    }

                    Ion.with(getApplicationContext())
                            .load("POST", Constant.WEB_SERVER + "/post/api/make_new_post/")
                            .setMultipartParameter("user_id", user_id)
                            .setMultipartParameter("title", title)
                            .setMultipartParameter("address", address)
                            .setMultipartParameter("city", city)
                            .setMultipartParameter("district", district)
                            .setMultipartParameter("ward", ward)
                            .setMultipartParameter("price", price)
                            .setMultipartParameter("area", area)
                            .setMultipartParameter("description", description)
                            .addMultipartParts(images)
                            .asString()
                            .setCallback(new FutureCallback<String>() {
                                @Override
                                public void onCompleted(Exception e, String result) {
                                    btnAddPost.setVisibility(View.VISIBLE);
                                    addingGif.setVisibility(View.GONE);
                                    if (e != null) {
                                        e.printStackTrace();
                                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        switch (result) {
                                            case "Added new post!":
                                                Toast.makeText(getApplicationContext(), "Bạn đã đăng một " +
                                                        "nhà trọ mới! Chúng tôi sẽ liên hệ để xét duyệt trong " +
                                                        "thời gian sớm nhất.", Toast.LENGTH_LONG).show();
                                                setResult(Activity.RESULT_OK);
                                                finish();
                                                break;
                                            default:
                                                Toast.makeText(getApplicationContext(), "Có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
                                                break;
                                        }
                                    }
                                }
                            });

                }
                break;
        }
    }

    public void clickToMakeNewPost(View view) {
        title = edtTitle.getText().toString().trim();
        address = edtAddress.getText().toString().trim();
        city = edtCity.getText().toString().trim();
        district = edtDistrict.getText().toString().trim();
        ward = edtWard.getText().toString().trim();
        price = edtPrice.getText().toString();
        area = edtArea.getText().toString();
        description = edtDescription.getText().toString();
        if ((isValidated(title, address, city, district, ward, price, area, description))) {
            startActivityForResult(new Intent(getApplicationContext(), ConfirmActivity.class), Constant.REQUEST_ID_FOR_ADD_POST);
        } else {
            Toast.makeText(getApplicationContext(), validationError, Toast.LENGTH_LONG).show();
            validationError = "";
        }
    }

    private boolean isValidated(String title, String address, String city, String district,
                                String ward, String price, String area, String description) {
        return checkTitle(title) && checkAddress(address) && checkCity(city) &&
                checkDistrict(district) && checkWard(ward) && checkPrice(price) &&
                checkArea(area) && checkDescription(description) && checkImage();
    }

    private boolean checkTitle(String title) {
        if (!title.isEmpty()) {
            return true;
        }
        validationError += "Tiêu đề bài đăng không được để trống!";
        return false;
    }

    private boolean checkAddress(String address) {
        if (!address.isEmpty()) {
            return true;
        }
        validationError += "Số nhà và đường không được để trống!";
        return false;
    }

    private boolean checkCity(String city) {
        if (!city.isEmpty()) {
            return true;
        }
        validationError += "Thành phố không được để trống!";
        return false;
    }

    private boolean checkDistrict(String district) {
        if (!district.isEmpty()) {
            return true;
        }
        validationError += "Quận không được để trống!";
        return false;
    }

    private boolean checkWard(String ward) {
        if (!ward.isEmpty()) {
            return true;
        }
        validationError += "Phường không được để trống!";
        return false;
    }

    private boolean checkPrice(String price) {
        if (!price.isEmpty()) {
            if (Integer.parseInt(price) != 0){
                return true;
            }
        }
        validationError += "Giá không được để trống!";
        return false;
    }

    private boolean checkArea(String area) {
        if (!area.isEmpty()) {
            if (Integer.parseInt(area) != 0) {
                return true;
            }
        }
        validationError += "Diện tích không được để trống!";
        return false;
    }

    private boolean checkDescription(String description) {
        if (!description.isEmpty()) {
            return true;
        }
        validationError += "Miêu tả không được để trống!";
        return false;
    }

    private boolean checkImage() {
        for (String image : npiaAdapter.getImages()) {
            if (!image.isEmpty()) {
                return true;
            }
        }
        validationError += "Bạn phải đăng ít nhất 1 hình!";
        return false;
    }
}
