package com.example.fhirgoal;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListGoalsAdapter extends RecyclerView.Adapter<ListGoalsAdapter.ViewHolder> implements Filterable {

    private ArrayList<Goal> mGoalData;
    private ArrayList<Goal> mGoalDataAll;
    private Context mContext;
    private int lastPosition = -1;

    ListGoalsAdapter(Context context, ArrayList<Goal> itemsData) {
        this.mGoalData = itemsData;
        this.mGoalDataAll = itemsData;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ListGoalsAdapter.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.list_goals, parent, false));
    }

    @Override
    public void onBindViewHolder(ListGoalsAdapter.ViewHolder holder, int position) {
        // Get current sport.
        Goal currentItem = mGoalData.get(position);

        // Populate the textviews with data.
        holder.bindTo(currentItem);


        if(holder.getAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }


    @Override
    public int getItemCount() {
        return mGoalData.size();
    }


    /**
     * RecycleView filter
     * **/
    @Override
    public Filter getFilter() {
        return shopingFilter;
    }

    private Filter shopingFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Goal> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if(charSequence == null || charSequence.length() == 0) {
                results.count = mGoalDataAll.size();
                results.values = mGoalDataAll;
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(Goal goal : mGoalDataAll) {
                    if(goal.getText().toLowerCase().contains(filterPattern)){
                        filteredList.add(goal);
                    }
                }

                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mGoalData = (ArrayList)filterResults.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder {

        // Member Variables for the TextViews
        private TextView mTitleText;
        private TextView mInfoText;
        private TextView mPriceText;


        ViewHolder(View itemView) {
            super(itemView);

            // Initialize the views.
            mTitleText = itemView.findViewById(R.id.GoalText);
            mInfoText = itemView.findViewById(R.id.subTitle);
            mPriceText = itemView.findViewById(R.id.category);

            itemView.findViewById(R.id.add_to_cart).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((ListGoalsActivity)mContext).updateAlertIcon();
                }
            });
        }

        void bindTo(Goal currentItem){
            mTitleText.setText(currentItem.getText());
            mInfoText.setText(currentItem.getDescription());
            mPriceText.setText((currentItem.getDueDate().toString()));

        }
    }
}
