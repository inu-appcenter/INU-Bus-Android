package com.bungabear.inubus;

/**
 * Created by Bunga on 2018-01-29.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class BusArrivalRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<CustomItem> mDataset = new ArrayList<CustomItem>();
    public class CustomItem {
        public boolean isSectionHeader;
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
        public ItemViewHolder(LinearLayout v) {
            super(v);
            busNo = v.findViewById(R.id.recycler_busno);
        }
    }
    // 버스 분류를 위한 Seperator뷰홀더
    public static class SeperatorHolder extends RecyclerView.ViewHolder {
        public TextView seperator;
        public SeperatorHolder(LinearLayout v) {
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
        LinearLayout v;
        if(viewType == 0){
            v = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_businfo, parent, false);
            RecyclerView.ViewHolder vh = new ItemViewHolder(v);
            return vh;
        }
        else {
            v = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_seperator, parent, false);
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
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}