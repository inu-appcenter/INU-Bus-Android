package com.bungabear.inubus.Custom;

/**
 * Created by Bunga on 2018-01-29.
 */

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bungabear.inubus.R;

import java.util.ArrayList;

public class RouteRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<CustomItem> mDataset = new ArrayList<CustomItem>();
    public class CustomItem {
        // 가운데 선을 위한 값. 1이 시작점이라 아래선, 2이 끝점이라 윗선, 3은 중간점들로 전체 4는 회차점 표시
        public int state;

        // 1이면 오른쪽, 2면 왼쪽 표시
        public int direction;
        public String stopName;

        public CustomItem(String stopName, int direction, int state){
            this.stopName = stopName;
            this.direction = direction;
            this.state = state;
        }

        public CustomItem(){
            this.state = 4;
        }
    }

    // 정류소 표시용 뷰홀더
    public static class StopHolder extends RecyclerView.ViewHolder {
        public TextView left, right;
        public View start, middle, end;
        public StopHolder(ConstraintLayout v) {
            super(v);
            left = v.findViewById(R.id.recycler_route_tv_left);
            right = v.findViewById(R.id.recycler_route_tv_right);
            start = v.findViewById(R.id.route_line_start);
            middle = v.findViewById(R.id.route_line_middle);
            end = v.findViewById(R.id.route_line_end);
        }
        public void setLine(int state){
            start.setVisibility(View.INVISIBLE);
            end.setVisibility(View.INVISIBLE);
            middle.setVisibility(View.INVISIBLE);
            switch(state){
                case 1:
                    start.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    end.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    middle.setVisibility(View.VISIBLE);
                    break;
            }
        }
        public void setDirection(int direction){
            if(direction == 1){
                right.setVisibility(View.VISIBLE);
                left.setVisibility(View.INVISIBLE);
            }
            else {
                left.setVisibility(View.VISIBLE);
                right.setVisibility(View.INVISIBLE);
            }
        }
    }
    // 회차지 뷰홀더
    public static class ReturnHolder extends RecyclerView.ViewHolder {
        public ReturnHolder(ConstraintLayout v) {
            super(v);
        }
    }

    public void addStop(String stopName, int direction, int state){
        mDataset.add(new CustomItem(stopName, direction, state));
    }

    public void addReturn(){
        mDataset.add(new CustomItem());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ConstraintLayout v;
        if(viewType == 0){
            v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_route, parent, false);
            RecyclerView.ViewHolder vh = new StopHolder(v);
            return vh;
        }
        else {
            v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_route_return, parent, false);
            RecyclerView.ViewHolder vh = new ReturnHolder(v);
            return vh;
        }
    }

    @Override
    public int getItemViewType(int position) {
        // 0 : 정류소
        // 1 : 회차지
        return mDataset.get(position).state == 4 ? 1 : 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CustomItem item = mDataset.get(position);
        if(item.state == 4){
            ReturnHolder mHolder = (ReturnHolder)holder;
        }
        else {
            StopHolder mHolder = (StopHolder)holder;
            mHolder.setLine(item.state);
            mHolder.setDirection(item.direction);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}