package trung.motelmobileapp.Components;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import trung.motelmobileapp.MyTools.Constant;
import trung.motelmobileapp.R;

public class PostDetailImageSliderAdapter extends PagerAdapter {

    private ArrayList<String> images;
    private Context context;
    private LayoutInflater layoutInflater;

    public PostDetailImageSliderAdapter(ArrayList<String> images, Context context) {
        this.images = images;
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.image_slider_item, null);
        ImageView image = view.findViewById(R.id.post_an_image);
        Glide.with(context).load(Constant.WEB_SERVER + images.get(position)).into(image);
        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);
        return  view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}
