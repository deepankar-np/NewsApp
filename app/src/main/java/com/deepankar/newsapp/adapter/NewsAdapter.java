package com.deepankar.newsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.deepankar.newsapp.R;
import com.deepankar.newsapp.db.AppDatabase;
import com.deepankar.newsapp.model.service.pojo.Article;
import com.deepankar.newsapp.model.service.pojo.NewsAPIResponse;
import com.deepankar.newsapp.utils.StringUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder> {
    private NewsAPIResponse mNewsData;

    private final NewsAdapterOnClickHandler mClickHandler;

    public NewsAdapter(NewsAdapterOnClickHandler mClickHandler) {
        this.mClickHandler = mClickHandler;
    }


    @NonNull
    @Override
    public NewsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.news_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new NewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapterViewHolder holder, int position) {
        List<Article> articles = mNewsData.getArticles();
        if (position < articles.size()) {
            Article article = articles.get(position);
            holder.newsTitle.setText(StringUtils.getNotNullString(article.getTitle()));

            String publishedAt = article.getPublishedAt();
            if (publishedAt != null) {
                holder.newsTime.setText(StringUtils.getDateTimeString(publishedAt));
            }

            holder.newsSource.setText(StringUtils.getNotNullString(article.getSource().getName()));
            holder.newsDescription.setText(StringUtils.getNotNullString(article.getDescription()));
            String imageUrl = article.getUrlToImage();
            if (!StringUtils.isEmpty(imageUrl)) {
                Picasso.get().load(imageUrl).into(holder.newsImage);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (null == mNewsData) return 0;
        return mNewsData.getArticles().size();
    }

    public void setNewsData(NewsAPIResponse newsData) {
        mNewsData = newsData;
        notifyDataSetChanged();
    }

    public NewsAPIResponse getmNewsData() {
        return mNewsData;
    }

    public interface NewsAdapterOnClickHandler {
        void onClick(Article article);
    }

    public class NewsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView newsImage;
        private TextView newsTitle;
        private TextView newsTime;
        private TextView newsDescription;
        private TextView newsSource;
        //private View view;

        public NewsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            newsImage = itemView.findViewById(R.id.news_image);
            newsTitle = itemView.findViewById(R.id.news_title);
            newsTime = itemView.findViewById(R.id.news_time);
            newsDescription = itemView.findViewById(R.id.news_description);
            newsSource = itemView.findViewById(R.id.news_source);
            //view = itemView.findViewById(R.id.dotted_view);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Article article = mNewsData.getArticles().get(adapterPosition);
            mClickHandler.onClick(article);
        }
    }
}
