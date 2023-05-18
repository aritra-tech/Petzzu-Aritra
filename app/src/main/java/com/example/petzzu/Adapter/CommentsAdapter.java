package com.example.petzzu.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petzzu.Model.Comments;
import com.example.petzzu.R;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {
    private Activity context;
    private List<Comments> commentsList;
    public CommentsAdapter(Activity context,List<Comments> commentsList){
        this.context=context;
        this.commentsList=commentsList;
    }
    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.each_comment,parent,false);
        return new CommentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {
        Comments comments=commentsList.get(position);
        holder.setmComment(comments.getComment());
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    public class CommentsViewHolder extends RecyclerView.ViewHolder{
        TextView mComment;
        View mView;
        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }
        public void setmComment(String comment){
            mComment=mView.findViewById(R.id.comment_tv);
            mComment.setText(comment);
        }
    }
}
