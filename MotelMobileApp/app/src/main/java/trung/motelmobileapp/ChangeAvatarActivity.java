package trung.motelmobileapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.koushikdutta.async.Util;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;

import trung.motelmobileapp.MyTools.Constant;

public class ChangeAvatarActivity extends AppCompatActivity {

    String profileImage;
    ImageButton newAvatar, btnCamera, btnGallery;
    Boolean storagePermission = false;
    Button btnSave;
    File image;
    SharedPreferences mySession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkStoragePermission();
        setContentView(R.layout.activity_change_avatar);
        profileImage = getIntent().getStringExtra("Profile Image");
        newAvatar = findViewById(R.id.change_avatar_image);
        btnCamera = findViewById(R.id.btnCamera);
        btnGallery = findViewById(R.id.btnGallery);
        btnSave = findViewById(R.id.btnChange);
        mySession = getSharedPreferences(Constant.MY_SESSION, Context.MODE_PRIVATE);
        Glide.with(this).load(Constant.WEB_SERVER + profileImage).into(newAvatar);
        btnSave.setVisibility(View.GONE);
    }

    public void backToProfilePage(View view) {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    public void clickToGallery(View view) {
        if (storagePermission){
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, Constant.REQUEST_ID_FOR_GALLERY);
        } else {
            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, Constant.REQUEST_ID_FOR_STORAGE_PERMISSION);
        }
    }

    public void clickToCamera(View view) {
        if (storagePermission){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File picturePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    .getAbsolutePath(), "/SweetMotel");
            if (!picturePath.exists()){
                picturePath.mkdir();
            }
            image = new File(picturePath, "user_avatar.jpg");
            Uri imageUri = Uri.fromFile(image);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            makeAccessForFiles();
            startActivityForResult(intent, Constant.REQUEST_ID_FOR_CAMERA);
        } else {
            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, Constant.REQUEST_ID_FOR_STORAGE_PERMISSION);
        }
    }

    //Make URI sharing through intent available in Android >= 7
    private void makeAccessForFiles(){
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        btnSave.setVisibility(View.VISIBLE);
        switch (requestCode){
            case Constant.REQUEST_ID_FOR_CAMERA:
                switch (resultCode){
                    case Activity.RESULT_OK:
                        Glide.with(getApplicationContext()).load(image).into(newAvatar);
                        break;
                    case Activity.RESULT_CANCELED:
                        btnSave.setVisibility(View.GONE);
                        break;
                }
                break;
            case Constant.REQUEST_ID_FOR_GALLERY:
                switch (resultCode){
                    case Activity.RESULT_OK:
                        image =  getFileFromURI(data.getData());
                        Glide.with(getApplicationContext()).load(image).into(newAvatar);
                        break;
                    case Activity.RESULT_CANCELED:
                        btnSave.setVisibility(View.GONE);
                        break;
                }
        }
    }

    private File getFileFromURI(Uri uri){
        String filePath;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            filePath = uri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            filePath = cursor.getString(idx);
            cursor.close();
        }
        return new File(filePath);
    }

    private void checkStoragePermission() {
        int readPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, Constant.REQUEST_ID_FOR_STORAGE_PERMISSION);
            return;
        }
        storagePermission = true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.REQUEST_ID_FOR_STORAGE_PERMISSION) {
            if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                storagePermission = true;
            } else {
                Toast.makeText(getApplicationContext(), "Cần quyền truy cập để cập nhật hình đại diện!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void clickToChangeAvatar(View view) {
        if (image != null){
            Intent imageLink = new Intent();
            imageLink.putExtra("Image Link", image.getAbsolutePath());
            setResult(Activity.RESULT_OK, imageLink);
            finish();
        }
        else {
            Toast.makeText(getApplicationContext(), "Có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
        }
    }
}
