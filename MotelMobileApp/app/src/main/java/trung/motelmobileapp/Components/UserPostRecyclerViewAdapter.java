package trung.motelmobileapp.Components;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import trung.motelmobileapp.Models.PostDTO;
import trung.motelmobileapp.R;

public class UserPostRecyclerViewAdapter extends RecyclerView.Adapter<UserPostRecyclerViewAdapter.RecyclerViewHolder>
                                        implements Comparator<PostDTO> {


    private ArrayList<PostDTO> data = new ArrayList<>();
    private ItemClickListener<PostDTO> itemClickListener;

    public UserPostRecyclerViewAdapter(ArrayList<PostDTO> data) {
        this.data = data;
        Collections.sort(data, this);
    }

    public void setItemClickListener(ItemClickListener<PostDTO> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.user_post_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, int position) {
        holder.txtTitle.setText(data.get(position).getTitle());
        String address =
                data.get(position).getRoom().getAddress() + ", Phường " +
                        data.get(position).getRoom().getWard() + ", Quận " +
                        data.get(position).getRoom().getDistrict() + ", " +
                        data.get(position).getRoom().getCity();
        holder.txtAddress.setText(address);
        holder.txtTime.setText(data.get(position).getRequest_date());
        String status = data.get(position).getStatus();
        TextView txtStatus = holder.txtStatus;
        switch (status) {
            case "u":
                txtStatus.setText("Chưa xác nhận");
                txtStatus.setTextColor(holder.itemView.getResources().getColor(R.color.colorUnconfirmed));
                break;
            case "c":
                txtStatus.setText("Đã xác nhận");
                txtStatus.setTextColor(holder.itemView.getResources().getColor(R.color.motelApp));
                break;
        }
        holder.aUserPost.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public int compare(PostDTO t1, PostDTO t2) {
        return t1.getStatus().compareTo(t2.getStatus());
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{

        TextView txtTitle;
        TextView txtAddress;
        TextView txtTime;
        TextView txtStatus;
        LinearLayout aUserPost;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.user_post_title);
            txtAddress = itemView.findViewById(R.id.user_post_address);
            txtTime = itemView.findViewById(R.id.user_post_time);
            txtStatus = itemView.findViewById(R.id.user_post_status);
            aUserPost = itemView.findViewById(R.id.a_user_post);
        }
    }
}
