package trung.motelmobileapp.Components;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import trung.motelmobileapp.Models.CommentDTO;
import trung.motelmobileapp.MyTools.Constant;
import trung.motelmobileapp.R;

public class DetailCommentRecyclerViewAdapter extends RecyclerView.Adapter<DetailCommentRecyclerViewAdapter.RecyclerViewHolder>{

    private ArrayList<CommentDTO> data = new ArrayList<>();
    private Context context;

    public DetailCommentRecyclerViewAdapter(ArrayList<CommentDTO> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.comment_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.commentName.setText(data.get(position).getUser().getName());
        holder.commentTime.setText(data.get(position).getCommentTime());
        holder.commentDetail.setText(data.get(position).getDetail());
        Glide.with(context).load(Constant.WEB_SERVER + data.get(position).getUser().getImage()).into(holder.commentImage);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView commentName, commentTime, commentDetail;
        ImageButton commentImage;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            commentName = itemView.findViewById(R.id.comment_name);
            commentTime = itemView.findViewById(R.id.comment_time);
            commentDetail = itemView.findViewById(R.id.comment_detail);
            commentImage = itemView.findViewById(R.id.comment_image);
        }
    }
}
