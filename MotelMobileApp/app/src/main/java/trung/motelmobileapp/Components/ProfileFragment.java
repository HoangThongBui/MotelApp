package trung.motelmobileapp.Components;

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

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import trung.motelmobileapp.EditProfileActivity;
import trung.motelmobileapp.Models.UserDTO;
import trung.motelmobileapp.MyTools.Constant;
import trung.motelmobileapp.R;
import trung.motelmobileapp.UserPostActivity;


public class ProfileFragment extends Fragment {

    LinearLayout layout;
    TextView profileName;
    ImageButton btnEditProfile, btnUserPost;
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
        btnLogout = layout.findViewById(R.id.btnLogout);
        mySession = getActivity().getSharedPreferences(Constant.MY_SESSION, Context.MODE_PRIVATE);
        viewPager = getActivity().findViewById(R.id.view_pager);
        tabAdapter = (TabAdapter) viewPager.getAdapter();

        //Get user data
        final String userId = mySession.getString("user_id", "");
        Ion.with(getContext())
           .load("GET","http://" + Constant.WEBSERVER_IP_ADDRESS + ":" + Constant.WEBSERVER_PORT +
                                    "/user/api/get_user_by_id/" + userId)
           .asJsonObject()
           .setCallback(new FutureCallback<JsonObject>() {
               @Override
               public void onCompleted(Exception e, JsonObject result) {
                   if (e != null){
                       e.printStackTrace();
                       Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                   }
                   else {
                        user = new UserDTO(
                                result.get("_id").getAsString(),
                                result.get("email").getAsString(),
                                result.get("name").getAsString(),
                                result.get("phone").getAsString()
                        );

                        profileName.setText(user.getName());
                   }
               }
           });

        //set events
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mySession.edit().clear().apply();
                Toast.makeText(getContext(), "Bạn đã đăng xuất thành công!", Toast.LENGTH_SHORT).show();
                tabAdapter.replaceFragmentAtPosition(new UnauthorizedProfileFragment(), 0);
                tabAdapter.notifyDataSetChanged();
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });

        btnUserPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UserPostActivity.class);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });
        return layout;
    }

}
