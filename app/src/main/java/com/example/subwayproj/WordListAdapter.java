package com.example.subwayproj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {
    private List<SubwayStation> resList ;

    public WordListAdapter(List<SubwayStation> res){
        resList=res;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.wordlist_item, parent, false);
        WordViewHolder wvh = new WordViewHolder(v);

        return wvh;
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        SubwayStation current = resList.get(position);

        holder.mTextView1.setText(current.getName());
        holder.mTextView2.setText("Magistrala " + current.getMagistrala());

        switch (current.getMagistrala()) {
            case 1:
                holder.mImageView.setImageResource(R.drawable.m1);
                break;
            case 2:
                holder.mImageView.setImageResource(R.drawable.m2);
                break;
            case 3:
                holder.mImageView.setImageResource(R.drawable.m3);
                break;
            case 4:
                holder.mImageView.setImageResource(R.drawable.m4);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return resList.size();
    }

    class WordViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;

        public WordViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView= itemView.findViewById(R.id.img);
            mTextView1=itemView.findViewById(R.id.tv1);
            mTextView2=itemView.findViewById(R.id.tv2);
        }
    }
}
