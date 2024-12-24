package com.accessa.ibora.sales.Sales;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
import com.accessa.ibora.product.items.Item;

import java.util.List;

public class MultiViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_TITLE = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_SEPARATOR = 2;

    private List<Object> data; // This will hold categories, items, and separators

    public MultiViewAdapter(List<Object> data) {
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {
        if (data.get(position) instanceof Category) {
            return TYPE_TITLE;
        } else if (data.get(position) instanceof Item) {
            return TYPE_ITEM;
        } else {
            return TYPE_SEPARATOR;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_TITLE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title, parent, false);
            return new TitleViewHolder(view);
        } else if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_separator, parent, false);
            return new SeparatorViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Object object = data.get(position);

        if (holder instanceof TitleViewHolder) {
            ((TitleViewHolder) holder).bind((Category) object);
        } else if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).bind((Item) object);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class TitleViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        TitleViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }

        void bind(Category category) {
            title.setText(category.getTitle());
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView itemName;

        ItemViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image);
            itemName = itemView.findViewById(R.id.item_name);
        }

        void bind(Item item) {
            itemName.setText(item.getName());
            // You can load the image here using Glide or Picasso
        }
    }

    static class SeparatorViewHolder extends RecyclerView.ViewHolder {
        SeparatorViewHolder(View itemView) {
            super(itemView);
        }
    }

    // Add your data classes for Category and Item
    public static class Category {
        private String title;

        public Category(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }

    public static class Item {
        private String name;
        private String imageUrl;

        public Item(String name, String imageUrl) {
            this.name = name;
            this.imageUrl = imageUrl;
        }

        public String getName() {
            return name;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }

    // Separator class for visual separation between categories
    public static class Separator {
        // No data necessary for separator
    }
}



