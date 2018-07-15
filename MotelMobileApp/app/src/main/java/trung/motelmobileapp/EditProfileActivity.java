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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;

import java.io.File;

import trung.motelmobileapp.MyTools.Constant;

public class EditProfileActivity extends AppCompatActivity {

    EditText edtName,edtPhone;
    Button btnSave;
    ImageButton profileImage;
    ImageView saveProfileGif;
    String validationError = "";
    SharedPreferences mySession;
    File newAvatar;
    String currentImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        edtName = findViewById(R.id.edit_user_name);
        edtPhone = findViewById(R.id.edit_user_phone);
        profileImage = findViewById(R.id.edit_profile_image);
        btnSave = findViewById(R.id.btnSaveProfile);
        saveProfileGif = findViewById(R.id.save_profile_gif);
        mySession = getSharedPreferences(Constant.MY_SESSION, Context.MODE_PRIVATE);
        Glide.with(this).load(R.drawable.loading).into(saveProfileGif);
        saveProfileGif.setVisibility(View.GONE);
        edtName.setText(getIntent().getStringExtra("Name"));
        edtPhone.setText(getIntent().getStringExtra("Phone"));
        currentImage = getIntent().getStringExtra("Profile Image");
        Glide.with(this).load(Constant.WEB_SERVER + currentImage).into(profileImage);
    }

    public void backToUserProfile(View view) {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    public void clickToSaveProfile(View view) {
        startActivityForResult(new Intent(getApplicationContext(), ConfirmActivity.class), Constant.REQUEST_ID_FOR_UPDATE_PROFILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Constant.REQUEST_ID_FOR_UPDATE_PROFILE:
                if (resultCode == Activity.RESULT_OK){
                    String name = edtName.getText().toString();
                    String phone = edtPhone.getText().toString();
                    String userId = mySession.getString("user_id", "");
                    if (isValidated(name, phone)){
                        Builders.Any.B builder = Ion.with(getApplicationContext())
                                .load("PUT", Constant.WEB_SERVER + "/user/api/update_profile/" + userId);
                        Builders.Any.M multipartBuilder = builder.setMultipartParameter("name", name)
                                                                 .setMultipartParameter("phone", phone);
                        if (newAvatar != null){
                            multipartBuilder.setMultipartFile("avatar",newAvatar);
                        }
                        multipartBuilder
                                .asString()
                                .setCallback(new FutureCallback<String>() {
                                    @Override
                                    public void onCompleted(Exception e, String result) {
                                        if (e != null){
                                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            switch (result){
                                                case "Profile updated!":
                                                    setResult(Activity.RESULT_OK);
                                                    finish();
                                                    break;
                                                default:
                                                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                                    break;
                                            }
                                        }
                                    }
                                });
                    }
                    else {
                        Toast.makeText(getApplicationContext(), validationError, Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case Constant.REQUEST_ID_FOR_GO_TO_CHOOSE_IMAGE:
                //update avatar link
                if (resultCode == Activity.RESULT_OK){
                    if (data != null){
                        newAvatar = new File(data.getStringExtra("Image File"));
                        Glide.with(getApplicationContext()).load(newAvatar).into(profileImage);
                    }
                }
                break;
        }
    }

    private boolean isValidated(String name, String phone){
        return isNameChecked(name) && isPhoneChecked(phone);
    }

    private boolean isNameChecked(String name){
        if (name.isEmpty()){
            validationError += "Tên không được để trống!";
            return false;
        }
        return true;
    }

    private boolean isPhoneChecked(String phone){
        if (phone.isEmpty()){
            validationError += "Số điện thoại không được để trống!";
            return false;
        }
        return true;
    }

    public void clickToChooseNewAvatar(View view) {
        startActivityForResult(new Intent(getApplicationContext(), ChooseImageSourceActivity.class), Constant.REQUEST_ID_FOR_GO_TO_CHOOSE_IMAGE);
    }
}
