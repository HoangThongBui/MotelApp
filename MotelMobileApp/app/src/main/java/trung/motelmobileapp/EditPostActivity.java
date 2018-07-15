package trung.motelmobileapp;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.body.FilePart;
import com.koushikdutta.async.http.body.Part;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;

import java.io.File;
import java.util.ArrayList;

import trung.motelmobileapp.Components.NewPostImageAreaAdapter;
import trung.motelmobileapp.Components.OnAddNewImageClickListener;
import trung.motelmobileapp.Models.PostDTO;
import trung.motelmobileapp.Models.RoomDTO;
import trung.motelmobileapp.Models.UserDTO;
import trung.motelmobileapp.MyTools.Constant;
import trung.motelmobileapp.MyTools.DateConverter;

public class EditPostActivity extends AppCompatActivity {

    EditText edtTitle, edtAddress, edtWard, edtDistrict, edtCity, edtPrice, edtArea, edtDescription;
    ImageView editPostGif;
    Button btnEdit;
    FloatingActionButton btnDelete;
    String postId, validationError = "";
    PostDTO postDetail;
    RecyclerView chosenImages;
    NewPostImageAreaAdapter npiaAdapter;
    int currentImage = 0;
    String title, address, city, district, ward, price, area, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        edtTitle = findViewById(R.id.edit_post_title);
        edtAddress = findViewById(R.id.edit_post_address);
        edtWard = findViewById(R.id.edit_post_ward);
        edtDistrict = findViewById(R.id.edit_post_district);
        edtCity = findViewById(R.id.edit_post_city);
        edtPrice = findViewById(R.id.edit_post_price);
        edtArea = findViewById(R.id.edit_post_area);
        edtDescription = findViewById(R.id.edit_post_description);
        editPostGif = findViewById(R.id.edit_post_gif);
        btnEdit = findViewById(R.id.btnEditPost);
        btnDelete = findViewById(R.id.btnDelete);
        chosenImages = findViewById(R.id.rv_edit_images);
        postId = getIntent().getStringExtra("Post");
        Glide.with(this).load(R.drawable.loading).into(editPostGif);
        editPostGif.setVisibility(View.GONE);

        //Load post
        Ion.with(this)
                .load("GET", Constant.WEB_SERVER + "/post/api/get_post_by_id/" + postId)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null) {
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        } else {
                            postDetail = new PostDTO(
                                    result.get("_id").getAsString(),
                                    result.get("title").getAsString(),
                                    new UserDTO(
                                            result.get("user").getAsJsonObject().get("_id").getAsString(),
                                            result.get("user").getAsJsonObject().get("name").getAsString(),
                                            result.get("user").getAsJsonObject().get("phone").getAsString()
                                    ),
                                    new RoomDTO(
                                            result.get("room").getAsJsonObject().get("_id").getAsString(),
                                            result.get("room").getAsJsonObject().get("address").getAsString(),
                                            result.get("room").getAsJsonObject().get("city").getAsString(),
                                            result.get("room").getAsJsonObject().get("district").getAsString(),
                                            result.get("room").getAsJsonObject().get("ward").getAsString(),
                                            result.get("room").getAsJsonObject().get("price").getAsInt(),
                                            result.get("room").getAsJsonObject().get("area").getAsInt(),
                                            result.get("room").getAsJsonObject().get("description").getAsString()
                                    ),
                                    DateConverter.getPassedTime(result.get("request_date").getAsString()),
                                    result.get("status").getAsString()
                            );

                            //disable these fields if confirmed post
                            if (postDetail.getStatus().equals("c")) {
                                edtAddress.setEnabled(false);
                                edtWard.setEnabled(false);
                                edtDistrict.setEnabled(false);
                                edtCity.setEnabled(false);
                                edtArea.setEnabled(false);
                            }

                            //set data to view
                            edtTitle.setText(postDetail.getTitle());
                            edtAddress.setText(postDetail.getRoom().getAddress());
                            edtWard.setText(postDetail.getRoom().getWard());
                            edtDistrict.setText(postDetail.getRoom().getDistrict());
                            edtCity.setText(postDetail.getRoom().getCity());
                            String displayingPrice = postDetail.getRoom().getPrice() + "";
                            edtPrice.setText(displayingPrice);
                            String displayingArea = postDetail.getRoom().getArea() + "";
                            edtArea.setText(displayingArea);
                            edtDescription.setText(postDetail.getRoom().getDescription());

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
                    }
                });
    }

    public void backToPostDetailPage(View view) {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    public void clickToSavePost(View view) {
        title = edtTitle.getText().toString().trim();
        address = edtAddress.getText().toString().trim();
        city = edtCity.getText().toString().trim();
        district = edtDistrict.getText().toString().trim();
        ward = edtWard.getText().toString().trim();
        price = edtPrice.getText().toString();
        area = edtArea.getText().toString();
        description = edtDescription.getText().toString();
        if ((isValidated(title, address, city, district, ward, price, area, description))) {
            startActivityForResult(new Intent(getApplicationContext(), ConfirmActivity.class), Constant.REQUEST_ID_FOR_UPDATE_POST);
        } else {
            Toast.makeText(getApplicationContext(), validationError, Toast.LENGTH_LONG).show();
            validationError = "";
        }
    }

    public void clickToDelete(View view) {
        startActivityForResult(new Intent(getApplicationContext(), ConfirmActivity.class), Constant.REQUEST_ID_FOR_DELETE_POST);
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
            case Constant.REQUEST_ID_FOR_UPDATE_POST:
                if (resultCode == Activity.RESULT_OK) {
                    btnEdit.setVisibility(View.GONE);
                    btnDelete.setVisibility(View.GONE);
                    editPostGif.setVisibility(View.VISIBLE);
                    Builders.Any.M multipartBuilder = Ion.with(getApplicationContext())
                            .load("PUT", Constant.WEB_SERVER + "/post/api/edit_post_by_id/" + postId)
                            .setMultipartParameter("room_id", postDetail.getRoom().getId())
                            .setMultipartParameter("title", title)
                            .setMultipartParameter("address", address)
                            .setMultipartParameter("city", city)
                            .setMultipartParameter("district", district)
                            .setMultipartParameter("ward", ward)
                            .setMultipartParameter("price", price)
                            .setMultipartParameter("area", area)
                            .setMultipartParameter("description", description);

                    ArrayList<String> editImages = new ArrayList<String>();
                    for (String imageLink : npiaAdapter.getImages()) {
                        if (!imageLink.isEmpty()) {
                            editImages.add(imageLink);
                        }
                    }
                    if (!editImages.isEmpty()) {
                        ArrayList<Part> images = new ArrayList<>();
                        for (String imageLink : editImages) {
                            images.add(new FilePart("room_images", new File(imageLink)));
                        }
                        multipartBuilder.addMultipartParts(images);
                    }
                    multipartBuilder.asString()
                            .setCallback(new FutureCallback<String>() {
                                @Override
                                public void onCompleted(Exception e, String result) {
                                    editPostGif.setVisibility(View.GONE);
                                    btnEdit.setVisibility(View.VISIBLE);
                                    btnDelete.setVisibility(View.VISIBLE);
                                    if (e != null) {
                                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        switch (result) {
                                            case "Updated!":
                                                Toast.makeText(getApplicationContext(), "Bạn đã cập nhật bài đăng thành công!",
                                                        Toast.LENGTH_SHORT).show();
                                                setResult(Activity.RESULT_OK);
                                                finish();
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                }
                            });
                }
                break;
            case Constant.REQUEST_ID_FOR_DELETE_POST:
                if (resultCode == Activity.RESULT_OK) {
                    btnEdit.setVisibility(View.GONE);
                    btnDelete.setVisibility(View.GONE);
                    editPostGif.setVisibility(View.VISIBLE);
                    Ion.with(getApplicationContext())
                            .load("DELETE", Constant.WEB_SERVER + "/post/api/delete_post_by_id/" + postId)
                            .asString()
                            .setCallback(new FutureCallback<String>() {
                                @Override
                                public void onCompleted(Exception e, String result) {
                                    btnEdit.setVisibility(View.VISIBLE);
                                    btnDelete.setVisibility(View.VISIBLE);
                                    editPostGif.setVisibility(View.GONE);
                                    if (e != null) {
                                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        switch (result) {
                                            case "Deleted!":
                                                Toast.makeText(getApplicationContext(), "Bạn đã xoá bài đăng thành công!",
                                                        Toast.LENGTH_SHORT).show();
                                                Intent backToMain = new Intent(getApplicationContext(), MainActivity.class);
                                                backToMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(backToMain);
                                                finish();
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                }
                            });
                }
                break;
        }
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
            if (Integer.parseInt(price) != 0) {
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
}
