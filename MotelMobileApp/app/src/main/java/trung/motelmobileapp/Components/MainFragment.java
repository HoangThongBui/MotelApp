package trung.motelmobileapp.Components;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import trung.motelmobileapp.MyTools.Constant;
import trung.motelmobileapp.R;


public class MainFragment extends Fragment {

    private String city;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final LinearLayout mainLayout = (LinearLayout) inflater.inflate(R.layout.fragment_main, container, false);
        ImageView loadingGif = mainLayout.findViewById(R.id.loadingGif);
        Glide.with(getContext()).load(R.drawable.loading).into(loadingGif);
        try {
            String city = getArguments().getString("City");
//            Ion.with(getContext())
//                    .load("GET", Constant.WEBSERVER_IP_ADDRESS + ":" + Constant.WEBSERVER_PORT + "/post/api/get_posts_by_city/" + city)
//                    .asJsonArray()
//                    .setCallback(new FutureCallback<JsonArray>() {
//                        @Override
//                        public void onCompleted(Exception e, JsonArray result) {
//                            if (e != null) {
//                                e.printStackTrace();
//                            } else {
//
//                            }
//                        }
//                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mainLayout;
    }
}
