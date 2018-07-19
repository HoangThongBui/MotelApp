package trung.motelmobileapp.Components;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import de.hdodenhof.circleimageview.CircleImageView;
import trung.motelmobileapp.ConfirmActivity;
import trung.motelmobileapp.EditProfileActivity;
import trung.motelmobileapp.Models.UserDTO;
import trung.motelmobileapp.MyTools.Constant;
import trung.motelmobileapp.R;
import trung.motelmobileapp.UserPostActivity;

public class ProfileFragment extends Fragment {

    LinearLayout layout;
    TextView profileName;
    ImageButton btnEditProfile, btnUserPost;
    CircleImageView profileImage;
    Button btnLogout;
    ViewPager viewPager;
    TabAdapter tabAdapter;
    SharedPreferences mySession;
    UserDTO user;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_profile, container, false);
        profileName = layout.findViewById(R.id.profile_name);
        btnEditProfile = layout.findViewById(R.id.btnEditProfile);
        btnUserPost = layout.findViewById(R.id.btnUserPost);
        profileImage = layout.findViewById(R.id.profile_image);
        btnLogout = layout.findViewById(R.id.btnLogout);
        mySession = getActivity().getSharedPreferences(Constant.MY_SESSION, Context.MODE_PRIVATE);
        viewPager = getActivity().findViewById(R.id.view_pager);
        tabAdapter = (TabAdapter) viewPager.getAdapter();

        //Get user data
        final String userId = mySession.getString("user_id", "");
        Ion.with(getContext())
                .load("GET", Constant.WEB_SERVER + "/user/api/get_user_by_id/" + userId)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        } else {
                            user = new UserDTO(
                                    result.get("_id").getAsString(),
                                    result.get("email").getAsString(),
                                    result.get("name").getAsString(),
                                    result.get("phone").getAsString(),
                                    result.get("image").getAsString()
                            );
                            profileName.setText(user.getName());
                            if (!user.getImage().isEmpty()){
                                Glide.with(getContext()).load(Constant.WEB_SERVER + user.getImage())
                                        .into(profileImage);

                            }
                        }
                    }
                });

        //set events
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), ConfirmActivity.class), Constant.REQUEST_ID_FOR_LOGOUT);
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                intent.putExtra("Name", user.getName());
                intent.putExtra("Phone", user.getPhone());
                intent.putExtra("Profile Image", user.getImage());
                startActivityForResult(intent, Constant.REQUEST_ID_FOR_UPDATE_PROFILE);
            }
        });

        btnUserPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), UserPostActivity.class));
            }
        });

        return layout;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Constant.REQUEST_ID_FOR_UPDATE_PROFILE:
                if (resultCode == Activity.RESULT_OK){
                    Toast.makeText(getContext(), "Bạn đã cập nhật hồ sơ thành công!", Toast.LENGTH_SHORT).show();
                    tabAdapter.replaceFragmentAtPosition(new ProfileFragment(), 0);
                    tabAdapter.notifyDataSetChanged();
                }
                break;
            case Constant.REQUEST_ID_FOR_LOGOUT :
                if (resultCode == Activity.RESULT_OK) {
                    mySession.edit().clear().apply();
                    Toast.makeText(getContext(), "Bạn đã đăng xuất thành công!", Toast.LENGTH_SHORT).show();
                    tabAdapter.replaceFragmentAtPosition(new UnauthorizedProfileFragment(), 0);
                    tabAdapter.notifyDataSetChanged();
                }
                break;
        }
    }
}
