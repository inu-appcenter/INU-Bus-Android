package com.bungabear.inubus.Custom;

/**
 * Created by Bunga on 2018-01-29.
 */

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bungabear.inubus.Activity.RouteActivity;
import com.bungabear.inubus.R;

import java.util.ArrayList;

import okhttp3.Route;

public class BusArrivalRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private ArrayList<CustomItem> mDataset = new ArrayList<CustomItem>();


    public class CustomItem {
        public boolean isSectionHeader;
        public boolean checked;
        public String sectionHeader;
        public String busNum;
        public String interval;
        public String arrival;

        public CustomItem(String setctionHeader){
            this.sectionHeader = setctionHeader;
            isSectionHeader = true;
        }

        public CustomItem(String busNum, String interval, String arrival){
            this.busNum = busNum;
            this.interval = interval;
            this.arrival = arrival;
            isSectionHeader = false;
        }
    }

    // 버스 정보용 일반 뷰홀더
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView busNo;
        public View touchView;
        public ItemViewHolder(ConstraintLayout v) {
            super(v);
            busNo = v.findViewById(R.id.recycler_busno);
            touchView = v.findViewById(R.id.recycler_businfo_touchview);
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

    public void addItem(String busNum, String interval, String arrival){
        mDataset.add(new CustomItem(busNum, interval, arrival));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ConstraintLayout v;
        if(viewType == 0){
            v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_businfo, parent, false);
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
        return mDataset.get(position).isSectionHeader ? 1 : 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CustomItem item = mDataset.get(position);
        if(item.isSectionHeader){
            SeperatorHolder mHolder = (SeperatorHolder)holder;
            mHolder.seperator.setText(""+item.sectionHeader );
        }
        else {
            ItemViewHolder mHolder = (ItemViewHolder)holder;
            mHolder.busNo.setText("" + item.busNum);
            mHolder.touchView.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
//        int itemPosition = mRecyclerView.getChildPosition(view);
//        String item = mList.get(itemPosition);
//        Toast.makeText(mContext, item, Toast.LENGTH_LONG).show();
        v.getContext().startActivity(new Intent(v.getContext().getApplicationContext(), RouteActivity.class));
    }

    public void refresh(){

    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}