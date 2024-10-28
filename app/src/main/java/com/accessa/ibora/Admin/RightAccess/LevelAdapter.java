package com.accessa.ibora.Admin.RightAccess;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;

import java.util.List;

public class LevelAdapter extends RecyclerView.Adapter<LevelAdapter.ViewHolder> {
    private Context mContext;
    private List<String> mLevels;  // A list to hold the levels

    // Constructor to pass the levels list
    public LevelAdapter(Context context, List<String> levels) {
        this.mContext = context;
        this.mLevels = levels;
    }

    // Inflate the item layout and create the ViewHolder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.level_layout, parent, false);
        return new ViewHolder(view);
    }

    // Bind the data (levels) to the TextView in the ViewHolder
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String level = mLevels.get(position);  // Get the current level (e.g., "Level 1")
        holder.textView.setText(level);  // Set the level text to the TextView
    }

    // Return the total number of levels
    @Override
    public int getItemCount() {
        return mLevels.size();
    }

    // ViewHolder to hold the TextView for each item
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);  // Reference to the TextView in your item layout
        }
    }
}
