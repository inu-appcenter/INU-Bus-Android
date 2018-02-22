package com.bungabear.inubus.Custom;

/**
 * Created by Bunga on 2018-01-29.
 */

import android.content.Intent;
import android.content.res.Resources;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bungabear.inubus.Activity.RouteActivity;
import com.bungabear.inubus.R;

import java.util.ArrayList;

public class DestinationRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private ArrayList<CustomItem> mDataset = new ArrayList<CustomItem>();

    public class CustomItem {

        public String sectionHeader;
        public String[] buses;
        // type이 0이면 헤더, 1 간선, 2 간선급생, 3 광역, 4 광역급행
        public int type;

        public CustomItem(String setctionHeader){
            this.sectionHeader = setctionHeader;
            type = 0;
        }

        public CustomItem(String[] buses, int type){
            this.buses = buses;
            this.type = type;
        }
    }

    // 버스 정보용 일반 뷰홀더
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout linearLayout;
        public ItemViewHolder(ConstraintLayout v) {
            super(v);
            linearLayout = v.findViewById(R.id.recycler_destination_linear);
        }
    }
    // 버스 분류를 위한 Seperator뷰홀더
    public static class SeperatorHolder extends RecyclerView.ViewHolder {
        public TextView seperator;
        public SeperatorHolder(ConstraintLayout v) {
            super(v);
            seperator = v.findViewById(R.id.separator);
        }
    }

    public void addSectionHeader(String sectionHeader){
        mDataset.add(new CustomItem(sectionHeader));
    }

    public void addItem(String[] buses, int type){
        mDataset.add(new CustomItem(buses, type));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ConstraintLayout v;
        if(viewType == 0){
            v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_destination, parent, false);
            RecyclerView.ViewHolder vh = new ItemViewHolder(v);
            return vh;
        }
        else {
            v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_businfo_seperator, parent, false);
            RecyclerView.ViewHolder vh = new SeperatorHolder(v);
            return vh;
        }
    }

    @Override
    public int getItemViewType(int position) {
        // 0 : 일반
        // 1 : 섹션 헤더
        return mDataset.get(position).type == 0 ? 1 : 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CustomItem item = mDataset.get(position);
        if(item.type == 0){
            SeperatorHolder mHolder = (SeperatorHolder)holder;
            mHolder.seperator.setText(""+item.sectionHeader );
        }
        else {
            switch(item.type){
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
            }
            ItemViewHolder mHolder = (ItemViewHolder)holder;
            for(String bus : item.buses){
                TextView tv = new TextView(mHolder.linearLayout.getContext());
                tv.setText(bus);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                Resources resources = tv.getContext().getResources();
                DisplayMetrics metrics = resources.getDisplayMetrics();
                params.setMargins(0,0, (int)(8 * (metrics.densityDpi / 160f)),0);
                tv.setLayoutParams(params);
                tv.setTextColor(resources.getColor(R.color.간선급행));
                tv.setTextSize(18);
                mHolder.linearLayout.addView(tv);
            }
        }
    }

    @Override
    public void onClick(View v) {
//        int itemPosition = mRecyclerView.getChildPosition(view);
//        String item = mList.get(itemPosition);
//        Toast.makeText(mContext, item, Toast.LENGTH_LONG).show();
        v.getContext().startActivity(new Intent(v.getContext().getApplicationContext(), RouteActivity.class));
    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}