package trung.motelmobileapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.Date;

import trung.motelmobileapp.MyTools.Constant;

public class ChooseImageSourceActivity extends AppCompatActivity {

    String imageChosen = "";
    Boolean storagePermission = false;
    Intent result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //check permission
        checkStoragePermission();

        setContentView(R.layout.activity_choose_image_source);
        result = new Intent();
    }

    public void clickToGallery(View view) {
        if (storagePermission) {
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
        if (storagePermission) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File picturePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    .getAbsolutePath(), "/SweetMotel");
            if (!picturePath.exists()) {
                picturePath.mkdir();
            }
            imageChosen += picturePath.getAbsolutePath() + new Date().getTime() + ".jpg";
            Uri imageUri = Uri.fromFile(new File(imageChosen));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            makeAccessForFiles();

            //open camera
            startActivityForResult(intent, Constant.REQUEST_ID_FOR_CAMERA);
        } else {
            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, Constant.REQUEST_ID_FOR_STORAGE_PERMISSION);
        }
    }

    //Make URI sharing through intent available in Android >= 7
    private void makeAccessForFiles() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            setResult(Activity.RESULT_CANCELED);
            finish();
        } else {
            switch (requestCode) {
                case Constant.REQUEST_ID_FOR_CAMERA:
                    break;
                case Constant.REQUEST_ID_FOR_GALLERY:
                    imageChosen = getFileFromURI(data.getData()).getAbsolutePath();
                    break;
            }
            result.putExtra("Image File", imageChosen);
            setResult(Activity.RESULT_OK, result);
            finish();
        }
    }

    private File getFileFromURI(Uri uri) {
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
            //open permission dialog
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
                Toast.makeText(getApplicationContext(), "Cần quyền truy cập để lấy hình!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
