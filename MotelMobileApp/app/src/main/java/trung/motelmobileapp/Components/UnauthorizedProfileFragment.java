package trung.motelmobileapp.Components;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import trung.motelmobileapp.MyTools.Constant;
import trung.motelmobileapp.R;
import trung.motelmobileapp.RegisterActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class UnauthorizedProfileFragment extends Fragment {

    LinearLayout layout;
    ViewPager viewPager;
    TabAdapter tabAdapter;
    SharedPreferences mySession;
    String userId;
    EditText edtEmail, edtPassword;
    TextView registerLink;
    Button btnLogin;
    ImageView loginGif;

    public UnauthorizedProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_unauthorized_profile, container, false);
        edtEmail = layout.findViewById(R.id.txtEmail);
        edtPassword = layout.findViewById(R.id.txtPassword);
        btnLogin = layout.findViewById(R.id.btnLogin);
        registerLink = layout.findViewById(R.id.registerLink);
        viewPager = getActivity().findViewById(R.id.view_pager);
        tabAdapter = (TabAdapter) viewPager.getAdapter();
        mySession = getActivity().getSharedPreferences(Constant.MY_SESSION, Context.MODE_PRIVATE);
        loginGif = layout.findViewById(R.id.login_gif);
        Glide.with(getContext()).load(R.drawable.loading).into(loginGif);
        loginGif.setVisibility(View.GONE);
        if (mySession.contains("user_id")) {
            userId = mySession.getString("user_id", "");
            Ion.with(getContext())
                    .load("GET", "http://" + Constant.WEBSERVER_IP_ADDRESS + ":" + Constant.WEBSERVER_PORT +
                            "/user/api/check_user_status/" + userId)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            if (e != null) {
                                e.printStackTrace();
                            } else {
                                switch (result) {
                                    case "User is active!":
                                        tabAdapter.replaceFragmentAtPosition(new ProfileFragment(), 0);
                                        tabAdapter.notifyDataSetChanged();
                                        break;
                                    case "User is banned!":
                                        mySession.edit().clear().apply();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    });
        }

        //set login and register event
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), RegisterActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
                if (isValidated(email, password)){
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

                    loginGif.setVisibility(View.VISIBLE);
                    btnLogin.setVisibility(View.GONE);
                    //Login API
                    Ion.with(getContext())
                       .load("POST", "http://" + Constant.WEBSERVER_IP_ADDRESS + ":" + Constant.WEBSERVER_PORT +
                                                    "/user/api/login/")
                       .setBodyParameter("email", email)
                       .setBodyParameter("password", password)
                       .asString()
                       .setCallback(new FutureCallback<String>() {
                           @Override
                           public void onCompleted(Exception e, String result) {
                               loginGif.setVisibility(View.GONE);
                               btnLogin.setVisibility(View.VISIBLE);
                                if (e != null) {
                                    Toast.makeText(getContext(), "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    switch (result){
                                        case "No account!":
                                            Toast.makeText(getContext(), "Tài khoản của bạn không tồn tại!", Toast.LENGTH_SHORT).show();
                                            break;
                                        case "User is banned!":
                                            Toast.makeText(getContext(), "Tài khoản của bạn đã bị khoá!", Toast.LENGTH_SHORT).show();
                                            break;
                                        case "Wrong password!":
                                            Toast.makeText(getContext(), "Thông tin đăng nhập sai!", Toast.LENGTH_SHORT).show();
                                            break;
                                        default:
                                            Toast.makeText(getContext(), "Đăng nhập thành công!", Toast.LENGTH_LONG).show();
                                            mySession.edit().putString("user_id", result).apply();
                                            tabAdapter.replaceFragmentAtPosition(new ProfileFragment(), 0);
                                            tabAdapter.notifyDataSetChanged();
                                            break;
                                    }
                                }
                           }
                       });
                }
                else {
                    Toast.makeText(getContext(),"Yêu cầu email phải đúng định dạng, password không được để trống!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return layout;
    }

    private Boolean isValidated(String email, String password){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() && !password.isEmpty();
    }

}
