package com.deepankar.newsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private Context context;

    public NewsCategoryListAdapter(ManageHomePresenter presenter) {
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view;
        switch (viewType) {
            case HEADER_TYPE:
                view = LayoutInflater.from(context)
                        .inflate(R.layout.news_category_list_section_header, parent, false);
                return new NewsCategorySectionHeaderViewHolder(view);
            default:
                view = LayoutInflater.from(context)
                        .inflate(R.layout.news_category_list_item, parent, false);
                return new NewsCategoryViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        final NewsCategory newsCategory = newsCategoryList.get(position);
        int nameId = newsCategory.getNameId();
        if (itemViewType == CATEGORY_TYPE) {
            ((NewsCategoryViewHolder) holder).categoryName.setText(nameId);
            if(!newsCategory.isDraggable()){
                ((NewsCategoryViewHolder) holder).reorderView.setVisibility(View.INVISIBLE);
            }else{
                ((NewsCategoryViewHolder) holder).reorderView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                            touchHelper.startDrag(holder);
                        }
                        return false;
                    }
                });
            }
        } else {
            NewsCategorySectionHeaderViewHolder headerViewHolder = (NewsCategorySectionHeaderViewHolder) holder;
            String text = "";

            if (nameId == -1) {
                text = "Enabled Categories";
            } else {
                text = "Disabled Categories";
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
        int nameId = newsCategoryList.get(position).getNameId();
        if (nameId == -1 || nameId == -2) {
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
        boolean moveAllowed = true;
        NewsCategory targetCategory = newsCategoryList.get(oldPosition);
        if(targetCategory.isAlwaysEnabled()){
            int disableHeaderPosition = 0;
            for (int i=0; i < newsCategoryList.size(); i++){
                NewsCategory newsCategory =  newsCategoryList.get(i);
                if(newsCategory.getNameId() == -2){
                    disableHeaderPosition = i;
                    break;
                }
            }
            if(newPosition >= disableHeaderPosition){
                //Can not disable
                moveAllowed = false;
                Toast.makeText(context, "'"+context.getString(targetCategory.getNameId())+"' cannot be disabled!", Toast.LENGTH_SHORT).show();
            }
        }
        if(moveAllowed) {
            NewsCategory category = new NewsCategory(targetCategory);
            newsCategoryList.remove(oldPosition);
            newsCategoryList.add(newPosition, category);
            boolean afterDisabledHeader = false;
            for (int i = 0; i < newsCategoryList.size(); i++) {
                NewsCategory newsCategory = newsCategoryList.get(i);
                if (newsCategory.getNameId() == -2) {
                    afterDisabledHeader = true;
                }
                newsCategory.setSequence(i);
                if (afterDisabledHeader) {
                    newsCategory.setEnabled(false);
                } else {
                    newsCategory.setEnabled(true);
                }
            }
            presenter.updateNewsCategoryPreferences(newsCategoryList);
            notifyItemMoved(oldPosition, newPosition);
        }
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
