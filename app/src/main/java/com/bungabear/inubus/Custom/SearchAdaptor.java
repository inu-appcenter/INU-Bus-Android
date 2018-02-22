package com.bungabear.inubus.Custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Bunga on 2018-01-30.
 */

public class SearchAdaptor extends ArrayAdapter {
    private  LayoutInflater inflater;
    private int layoutResourceId;

    public SearchAdaptor(@NonNull Context context, int resource, LayoutInflater inflater) {
        super(context, resource);
        this.inflater = inflater;
        this.layoutResourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

//        // object item based on the position
//        MyObject objectItem = data[position];
//
//        // get the TextView and then set the text (item name) and tag (item ID) values
//        TextView textViewItem = (TextView) convertView.findViewById(R.id.textViewItem);
//        textViewItem.setText(objectItem.objectName);

        return convertView;
    }
}
