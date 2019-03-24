package trung.motelmobileapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import trung.motelmobileapp.MyTools.Constant;

public class RegisterActivity extends AppCompatActivity {

    EditText edtEmail, edtName, edtPhone, edtPassword, edtConfirm;
    CheckBox chkRule;
    ImageView registerGif;
    Button btnRegister;
    String validateMessage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edtEmail = findViewById(R.id.register_email);
        edtName = findViewById(R.id.register_name);
        edtPhone = findViewById(R.id.register_phone);
        edtPassword = findViewById(R.id.register_password);
        edtConfirm = findViewById(R.id.register_confirm);
        chkRule = findViewById(R.id.chkRule);
        btnRegister = findViewById(R.id.btnRegister);
        registerGif = findViewById(R.id.register_gif);

        Glide.with(this).load(R.drawable.loading).into(registerGif);
        registerGif.setVisibility(View.GONE);
    }

    public void backToLoginPage(View view) {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_ID_FOR_REGISTER) {
            if (resultCode == Activity.RESULT_OK) {

                String txtEmail = edtEmail.getText().toString();
                String txtName = edtName.getText().toString();
                String txtPhone = edtPhone.getText().toString();
                String txtPassword = edtPassword.getText().toString();
                btnRegister.setVisibility(View.GONE);
                registerGif.setVisibility(View.VISIBLE);
                Ion.with(getApplicationContext())
                        .load("POST", Constant.WEB_SERVER + "/user/api/register/")
                        .setBodyParameter("email", txtEmail)
                        .setBodyParameter("name", txtName)
                        .setBodyParameter("phone", txtPhone)
                        .setBodyParameter("password", txtPassword)
                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {
                                btnRegister.setVisibility(View.VISIBLE);
                                registerGif.setVisibility(View.GONE);
                                if (e != null) {
                                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                                } else {
                                    switch (result) {
                                        case "User existed!":
                                            Toast.makeText(getApplicationContext(), "Email này đã tồn tại! Vui lòng chọn email khác.", Toast.LENGTH_LONG).show();
                                            break;
                                        case "Register successfully!":
                                            Toast.makeText(getApplicationContext(), "Tài khoản được đăng ký thành công!", Toast.LENGTH_LONG).show();
                                            finish();
                                            break;
                                        default:
                                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                                            break;
                                    }
                                }
                            }
                        });
            }

        }
    }


    public void clickToRegister(View view) {
        String txtEmail = edtEmail.getText().toString();
        String txtName = edtName.getText().toString();
        String txtPhone = edtPhone.getText().toString();
        String txtPassword = edtPassword.getText().toString();
        String txtConfirm = edtConfirm.getText().toString();
        Boolean isRuleChecked = chkRule.isChecked();
        if (isValidated(txtEmail, txtName, txtPhone, txtPassword, txtConfirm, isRuleChecked)) {
            startActivityForResult(
                    new Intent(getApplicationContext(), ConfirmActivity.class),
                    Constant.REQUEST_ID_FOR_REGISTER);
        } else {
            Toast.makeText(getApplicationContext(), validateMessage, Toast.LENGTH_LONG).show();
            validateMessage = "";
        }
    }

    private boolean isValidated(String email, String name, String phone, String password, String confirm, boolean check) {
        return checkEmail(email) && checkName(name) && checkPhone(phone) && checkPassword(password, confirm) && checkRule(check);
    }

    private boolean checkEmail(String email) {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true;
        }
        validateMessage += "Email không hợp lệ!";
        return false;
    }

    private boolean checkName(String name) {
        if (!name.trim().isEmpty()) {
            return true;
        }
        validateMessage += "Tên không được để trống!";
        return false;
    }

    private boolean checkPhone(String phone) {
        if (!phone.isEmpty()) {
            return true;
        }
        validateMessage += "Số điện thoại không được để trống!";
        return false;
    }

    private boolean checkPassword(String password, String confirm) {
        if (!password.isEmpty() && !confirm.isEmpty()) {
            if (confirm.equals(password)) {
                return true;
            }
        }
        validateMessage += "Password chưa được xác nhận!";
        return false;
    }

    private boolean checkRule(boolean isChecked) {
        if (isChecked) {
            return true;
        }
        validateMessage += "Bạn phải đồng ý điều khoản của chúng tôi!";
        return false;
    }
}
