package trung.motelmobileapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import trung.motelmobileapp.MyTools.Constant;

public class MakeNewPostActivity extends AppCompatActivity {

    EditText edtTitle, edtAddress, edtCity, edtDistrict, edtWard, edtPrice, edtArea, edtDescription;
    Button btnAddPost;
    ImageView addingGif;
    String validationError = "";
    SharedPreferences mySession;

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
        mySession = getSharedPreferences(Constant.MY_SESSION, Context.MODE_PRIVATE);
        Glide.with(this).load(R.drawable.loading).into(addingGif);
        addingGif.setVisibility(View.GONE);
    }

    public void backToUserPostPage(View view) {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_ID_FOR_ADD_POST) {
            if (resultCode == Activity.RESULT_OK) {
                String title = edtTitle.getText().toString().trim();
                String address = edtAddress.getText().toString().trim();
                String city = edtCity.getText().toString().trim();
                String district = edtDistrict.getText().toString().trim();
                String ward = edtWard.getText().toString().trim();
                String price = edtPrice.getText().toString();
                String area = edtArea.getText().toString();
                String description = edtDescription.getText().toString();
                String user_id = mySession.getString("user_id", "");

                if ((isValidated(title, address, city, district, ward, price, area, description))) {
                    btnAddPost.setVisibility(View.GONE);
                    addingGif.setVisibility(View.VISIBLE);
                    Ion.with(getApplicationContext())
                            .load("POST", Constant.WEB_SERVER + "/post/api/make_new_post/")
                            .setBodyParameter("user_id", user_id)
                            .setBodyParameter("title", title)
                            .setBodyParameter("address", address)
                            .setBodyParameter("city", city)
                            .setBodyParameter("district", district)
                            .setBodyParameter("ward", ward)
                            .setBodyParameter("price", price)
                            .setBodyParameter("area", area)
                            .setBodyParameter("description", description)
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
                } else {
                    Toast.makeText(getApplicationContext(), validationError, Toast.LENGTH_LONG).show();
                    validationError = "";
                }
            }
        }
    }

    public void clickToMakeNewPost(View view) {
        startActivityForResult(new Intent(getApplicationContext(), ConfirmActivity.class), Constant.REQUEST_ID_FOR_ADD_POST);
    }

    private boolean isValidated(String title, String address, String city, String district,
                                String ward, String price, String area, String description) {
        return checkTitle(title) && checkAddress(address) && checkCity(city) &&
                checkDistrict(district) && checkWard(ward) && checkPrice(price) &&
                checkArea(area) && checkDescription(description);
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
            return true;
        }
        validationError += "Giá không được để trống!";
        return false;
    }

    private boolean checkArea(String area) {
        if (!area.isEmpty()) {
            return true;
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
}
