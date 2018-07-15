package trung.motelmobileapp.Components;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import trung.motelmobileapp.Models.PostDTO;
import trung.motelmobileapp.MyTools.Constant;
import trung.motelmobileapp.R;

public class GeneralPostRecyclerViewAdapter extends RecyclerView.Adapter<GeneralPostRecyclerViewAdapter.RecyclerViewHolder> {

    private ArrayList<PostDTO> data = new ArrayList<>();
    private Context context;
    private ItemClickListener<PostDTO> itemClickListener;


    public GeneralPostRecyclerViewAdapter(ArrayList<PostDTO> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public void setItemClickListener(ItemClickListener<PostDTO> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.general_post_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, final int position) {
        holder.txtTitle.setText(data.get(position).getTitle());
        String address =
                data.get(position).getRoom().getAddress() + ", Phường " +
                data.get(position).getRoom().getWard() + ", Quận " +
                data.get(position).getRoom().getDistrict() + ", " +
                data.get(position).getRoom().getCity();
        holder.txtAddress.setText(address);
        holder.txtTime.setText(data.get(position).getRequest_date());
        Glide.with(context).load(Constant.WEB_SERVER + data.get(position).getRoom().getImages().get(0)).into(holder.firstImage);
        holder.aPostInMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null){
                    itemClickListener.onClick(data.get(holder.getAdapterPosition()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        TextView txtAddress;
        TextView txtTime;
        LinearLayout aPostInMain;
        ImageView firstImage;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.main_title);
            txtAddress = itemView.findViewById(R.id.main_address);
            txtTime = itemView.findViewById(R.id.main_time);
            aPostInMain = itemView.findViewById(R.id.a_post_in_main);
            firstImage = itemView.findViewById(R.id.main_image);
        }
    }
}
