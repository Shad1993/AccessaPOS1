package com.accessa.ibora.product.menu;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.accessa.ibora.R;

import java.util.zip.Inflater;

public class CustomAdapter extends BaseAdapter {
    Context context;
    String Menulist[];
    int Icons[];
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, String[] Menulist, int[] Icons) {
        this.context = context;
        this.Menulist = Menulist;
        this.Icons = Icons;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return Menulist.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.activity_listview, null);
        TextView country = (TextView) view.findViewById(R.id.textView);
        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        country.setText(Menulist[i]);
        icon.setImageResource(Icons[i]);
        return view;
    }

}