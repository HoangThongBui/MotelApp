package trung.motelmobileapp.Components;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import trung.motelmobileapp.MyTools.Constant;
import trung.motelmobileapp.R;

public class NewPostImageAreaAdapter extends RecyclerView.Adapter<NewPostImageAreaAdapter.RecyclerViewHolder>{

    private ArrayList<String> images;
    private Context context;
    private OnAddNewImageClickListener imageListener;

    public NewPostImageAreaAdapter(Context context) {
        this.images = new ArrayList<>();
        images.add("");
        this.context = context;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void addImage(String image, int position){
        this.images.set(position, image);
        this.images.add("");
    }

    public void removeImage(int position){
        this.images.remove(position);
    }

    public void setImageListener(OnAddNewImageClickListener imageListener) {
        this.imageListener = imageListener;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.new_image_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, int position) {
        if (images.get(position).isEmpty()){
            holder.btnAdd.setVisibility(View.VISIBLE);
            holder.chosenImage.setVisibility(View.GONE);
        }
        else {
            holder.btnAdd.setVisibility(View.GONE);
            holder.chosenImage.setVisibility(View.VISIBLE);
            Glide.with(context).load(images.get(position)).into(holder.chosenImage);
        }

        //event add image
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageListener != null){
                    imageListener.onAdd(holder.getAdapterPosition());
                }
            }
        });

        //event clear image
        holder.chosenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageListener != null){
                    imageListener.onRemove(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageButton btnAdd, chosenImage;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            btnAdd = itemView.findViewById(R.id.new_image_request);
            chosenImage = itemView.findViewById(R.id.chose_new_image);
        }
    }
}
