package trung.motelmobileapp.Components;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import trung.motelmobileapp.Models.PostDTO;
import trung.motelmobileapp.R;

public class GeneralPostRecyclerViewAdapter extends RecyclerView.Adapter<GeneralPostRecyclerViewAdapter.RecyclerViewHolder> {

    private ArrayList<PostDTO> data = new ArrayList<>();
    private ItemClickListener<PostDTO> itemClickListener;


    public GeneralPostRecyclerViewAdapter(ArrayList<PostDTO> data) {
        this.data = data;
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
                data.get(position).getAddress() + ", Phường " +
                data.get(position).getWard() + ", Quận " +
                data.get(position).getDistrict() + ", " +
                data.get(position).getCity();
        holder.txtAddress.setText(address);
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
        LinearLayout aPostInMain;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.main_title);
            txtAddress = itemView.findViewById(R.id.main_address);
            aPostInMain = itemView.findViewById(R.id.a_post_in_main);

        }
    }
}
