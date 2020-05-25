package com.deepankar.newsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.deepankar.newsapp.R;
import com.deepankar.newsapp.db.AppDatabase;
import com.deepankar.newsapp.db.NewsCategory;
import com.deepankar.newsapp.presenter.ManageHomePresenter;
import com.deepankar.newsapp.utils.SwipeAndDragHelper;

import java.util.List;

public class NewsCategoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        SwipeAndDragHelper.ActionCompletionContract {

    private static final int CATEGORY_TYPE = 1;
    private static final int HEADER_TYPE = 2;
    private List<NewsCategory> newsCategoryList;
    private ItemTouchHelper touchHelper;
    private ManageHomePresenter presenter;
    private int enableSequence;
    private int disableSequence;

    public NewsCategoryListAdapter(ManageHomePresenter presenter) {
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case HEADER_TYPE:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.news_category_list_section_header, parent, false);
                return new NewsCategorySectionHeaderViewHolder(view);
            default:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.news_category_list_item, parent, false);
                return new NewsCategoryViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == CATEGORY_TYPE) {
            ((NewsCategoryViewHolder) holder).categoryName.setText(newsCategoryList.get(position).getNameId());
            ((NewsCategoryViewHolder) holder).reorderView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                        touchHelper.startDrag(holder);
                    }
                    return false;
                }
            });
        } else {
            NewsCategorySectionHeaderViewHolder headerViewHolder = (NewsCategorySectionHeaderViewHolder) holder;
            String text = "";
            if (newsCategoryList.get(position).isEnabled()) {
                text = "Enabled Categories";
                enableSequence = position - 1;
            } else {
                text = "Disabled Categories";
                disableSequence = position - 1;
            }
            headerViewHolder.sectionTitle.setText(text);
        }
    }

    @Override
    public int getItemCount() {
        return newsCategoryList == null ? 0 : newsCategoryList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (newsCategoryList.get(position).getNameId() == -1) {
            return HEADER_TYPE;
        } else {
            return CATEGORY_TYPE;
        }
    }

    public void setNewsCategoryList(List<NewsCategory> newsCategoryList) {
        this.newsCategoryList = newsCategoryList;
        presenter.updateNewsCategoryPreferences(newsCategoryList);
        notifyDataSetChanged();
    }

    @Override
    public void onViewMoved(int oldPosition, int newPosition) {
        NewsCategory targetCategory = newsCategoryList.get(oldPosition);
        targetCategory.setSequence(newPosition - 1);
        if (newPosition >= disableSequence) {
            targetCategory.setEnabled(false);
        } else {
            targetCategory.setEnabled(true);
        }
        NewsCategory category = new NewsCategory(targetCategory);
        newsCategoryList.remove(oldPosition);
        newsCategoryList.add(newPosition, category);
        for (int i=0; i < newsCategoryList.size(); i++){
            newsCategoryList.get(i).setSequence(i);
        }
        presenter.updateNewsCategoryPreferences(newsCategoryList);
        notifyItemMoved(oldPosition, newPosition);
    }

    @Override
    public void onViewSwiped(int position) {
        newsCategoryList.remove(position);
        notifyItemRemoved(position);
    }

    public void setTouchHelper(ItemTouchHelper touchHelper) {
        this.touchHelper = touchHelper;
    }

    public void loadNewsCategoryList() {
        presenter.loadNewsCategoryPreferences(this);
    }

    class NewsCategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        ImageView reorderView;

        public NewsCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.textview_name);
            reorderView = itemView.findViewById(R.id.imageview_reorder);
        }
    }

    class NewsCategorySectionHeaderViewHolder extends RecyclerView.ViewHolder {
        TextView sectionTitle;

        public NewsCategorySectionHeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionTitle = itemView.findViewById(R.id.textview_section_header);
        }
    }
}
