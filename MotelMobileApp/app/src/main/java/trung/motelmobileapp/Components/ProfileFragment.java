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
                intent.putExtra("user_id", "");
                startActivity(intent);
            }
        });
        return layout;
    }

}
