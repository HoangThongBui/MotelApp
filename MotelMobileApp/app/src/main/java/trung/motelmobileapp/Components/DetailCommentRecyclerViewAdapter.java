package trung.motelmobileapp.Components;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import trung.motelmobileapp.Models.CommentDTO;
import trung.motelmobileapp.R;

public class DetailCommentRecyclerViewAdapter extends RecyclerView.Adapter<DetailCommentRecyclerViewAdapter.RecyclerViewHolder>{

    private ArrayList<CommentDTO> data = new ArrayList<>();

    public DetailCommentRecyclerViewAdapter(ArrayList<CommentDTO> data) {
        this.data = data;
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
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView commentName, commentTime, commentDetail;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            commentName = itemView.findViewById(R.id.comment_name);
            commentTime = itemView.findViewById(R.id.comment_time);
            commentDetail = itemView.findViewById(R.id.comment_detail);
        }
    }
}
