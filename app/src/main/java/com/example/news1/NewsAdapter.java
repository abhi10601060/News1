package com.example.news1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private ArrayList<News> news ;

    public NewsAdapter() {
    }

    public void setNews(ArrayList<News> news) {
        this.news = news;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.title.setText(news.get(position).getTitle());
            holder.desc.setText(news.get(position).getDescription());
            holder.date.setText(news.get(position).getDate());

            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO: 19-05-2022 get o the web view
                }
            });



    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private  TextView title, date , desc ;
        private RelativeLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txt_title);
            date =itemView.findViewById(R.id.txt_date);
            desc = itemView.findViewById(R.id.txt_desc);
            container=itemView.findViewById(R.id.container);
        }
    }
}
