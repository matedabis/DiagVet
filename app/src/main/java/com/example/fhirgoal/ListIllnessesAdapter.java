package com.example.fhirgoal;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListIllnessesAdapter extends RecyclerView.Adapter<ListIllnessesAdapter.ViewHolder> implements Filterable {

    private static final int SECRET_KEY = 99;
    private ArrayList<Illness> mIllnessData;
    private ArrayList<Illness> mIllnessDataAll;
    private Context mContext;

    ListIllnessesAdapter(Context context, ArrayList<Illness> itemsData) {
        this.mIllnessData = itemsData;
        this.mIllnessDataAll = itemsData;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ListIllnessesAdapter.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.list_goals, parent, false));
    }

    @Override
    public void onBindViewHolder(ListIllnessesAdapter.ViewHolder holder, int position) {
        // Get current sport.
        Illness currentItem = mIllnessData.get(position);

        // Populate the textviews with data.
        holder.bindTo(currentItem);


        if((holder.getAdapterPosition() % 2 == 0)) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
        } else {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_left);
            holder.itemView.startAnimation(animation);
        }
    }


    @Override
    public int getItemCount() {
        return mIllnessData.size();
    }


    /**
     * RecycleView filter
     * **/
    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Illness> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if(charSequence == null || charSequence.length() == 0) {
                results.count = mIllnessDataAll.size();
                results.values = mIllnessDataAll;
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(Illness illness : mIllnessDataAll) {
                    if(illness.getText().toLowerCase().contains(filterPattern)){
                        filteredList.add(illness);
                    }
                }

                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mIllnessData = (ArrayList)filterResults.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder {

        // Member Variables for the TextViews
        private TextView mTitleText;
        private TextView mInfoText;
        private TextView mDue;
        private TextView mCategory;
        private TextView mStatus;


        ViewHolder(View itemView) {
            super(itemView);

            // Initialize the views.
            mTitleText = itemView.findViewById(R.id.GoalText);
            mInfoText = itemView.findViewById(R.id.subTitle);
            mDue = itemView.findViewById(R.id.due);
            mCategory = itemView.findViewById(R.id.category);
            mStatus = itemView.findViewById(R.id.status);

        }

        void bindTo(Illness currentItem){
            mTitleText.setText(currentItem.getText());
            mInfoText.setText(currentItem.getDescription());
            mDue.setText(currentItem.getDueDate());
            mCategory.setText(currentItem.getCategory());
            mStatus.setText(currentItem.getLifecycleStatus());

            itemView.findViewById(R.id.delete).setOnClickListener(view -> ((ListIllnessesActivity)mContext).deleteItem(currentItem));
            itemView.findViewById(R.id.modify).setOnClickListener(view -> ((ListIllnessesActivity)mContext).modifyItem(currentItem));
        }
    }
}
