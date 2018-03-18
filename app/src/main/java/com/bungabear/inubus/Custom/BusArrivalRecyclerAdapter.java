package com.bungabear.inubus.Custom;

/**
 * Created by Bunga on 2018-01-29.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bungabear.inubus.Activity.RouteActivity;
import com.bungabear.inubus.R;
import com.bungabear.inubus.RetrofitClass;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import android.os.Handler;

import okhttp3.Route;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusArrivalRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private ArrayList<CustomItem> mDataset = new ArrayList<CustomItem>();
    private String stopName;
    public BusArrivalRecyclerAdapter(String stopName){
        this.stopName = stopName;
    }

    public class CustomItem {
        public boolean isHeader = false;
        public boolean isSectionHeader = false;
        public boolean checked;
        public String sectionHeader;
        public String busNum;
        public String interval;
        public String arrival;
        public String type;

        public CustomItem(){
            this.isHeader = true;
        }

        public CustomItem(String setctionHeader){
            this.sectionHeader = setctionHeader;
            isSectionHeader = true;
        }

        public CustomItem(String busNum, String interval, String arrival, String type){
            this.busNum = busNum;
            this.interval = interval;
            this.arrival = arrival;
            this.type = type;
            isSectionHeader = false;
        }
    }

    // 버스 정보용 일반 뷰홀더
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView busNo, arrival, interval;
        public View touchView;
        public ItemViewHolder(ConstraintLayout v) {
            super(v);
            arrival = v.findViewById(R.id.recycler_arrival);
            interval = v.findViewById(R.id.recycler_interval);
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

    // 버스 분류를 위한 Seperator뷰홀더
    public static class HeaderHolder extends RecyclerView.ViewHolder {
        public HeaderHolder(ConstraintLayout v) {
            super(v);
        }
    }
    public void addHeader(){
        mDataset.add(new CustomItem());
    }

    public void addSectionHeader(String sectionHeader){
        mDataset.add(new CustomItem(sectionHeader));
    }

    public void addItem(String busNum, String interval, String arrival, String type){
        mDataset.add(new CustomItem(busNum, interval, arrival, type));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ConstraintLayout v;
        if(viewType == -1){
            v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_businfo_header, parent, false);
            RecyclerView.ViewHolder vh = new HeaderHolder(v);
            return vh;
        }
        else if(viewType == 0){
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
        if(mDataset.get(position).isHeader){
            return -1;
        }
        return mDataset.get(position).isSectionHeader ? 1 : 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CustomItem item = mDataset.get(position);
        if(item.isSectionHeader){
            SeperatorHolder mHolder = (SeperatorHolder)holder;
            mHolder.seperator.setText(""+item.sectionHeader );
        }
        else if(!item.isHeader){
            ItemViewHolder mHolder = (ItemViewHolder)holder;
            mHolder.busNo.setText("" + item.busNum);
            mHolder.interval.setText("" + item.interval);
            mHolder.arrival.setText("" + item.arrival);
            int color;
            if(item.type.equals("간선")){
                color = R.color.간선;
            } else if(item.type.equals("간선급행")){
                color = R.color.간선급행;
            } else if(item.type.equals("광역")){
                color = R.color.광역;
            } else if(item.type.equals("광역급행")){
                color = R.color.광역급행;
            } else {
                color = R.color.순환;
            }
            Context context = mHolder.busNo.getContext();
            mHolder.busNo.setTextColor(ContextCompat.getColor(context, color));
            mHolder.touchView.setOnClickListener(this);
        }
        else {

        }
    }

    @Override
    public void onClick(View v) {
//        int itemPosition = mRecyclerView.getChildPosition(view);
//        String item = mList.get(itemPosition);
//        Toast.makeText(mContext, item, Toast.LENGTH_LONG).show();
        v.getContext().startActivity(new Intent(v.getContext().getApplicationContext(), RouteActivity.class));
    }

    public void refresh(final Handler handler){
        Message msg = new Message();
        msg.what = 0;
        handler.sendMessage(msg);

        RetrofitClass.getInstance().getData("arrivalinfo").enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                mDataset.clear();
                JsonArray array = response.body().getAsJsonArray();
                for(JsonElement ele : array){
                    String name = ele.getAsJsonObject().get("name").getAsString();
                    if(stopName.equals(name)){
                        sortData(ele.getAsJsonObject().get("data").getAsJsonArray());
                    }
                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Message msg = new Message();
                msg.what=2;
                handler.sendMessage(msg);
            }
        });

        Message msg2 = new Message();
        msg2.what = 1;
        handler.sendMessage(msg2);
    }

    private void sortData(JsonArray datas) {
        JsonArray one = new JsonArray();
        JsonArray two = new JsonArray();
        JsonArray three = new JsonArray();
        JsonArray four = new JsonArray();
        for (int i = 0; i < datas.size(); i++) {
            JsonObject arrival = datas.get(i).getAsJsonObject();
            String type = arrival.get("type").getAsString();
            if (type.equals("간선")) {
                one.add(arrival);
            } else if (type.equals("간선급행")) {
                two.add(arrival);
            } else if (type.equals("급행")) {
                three.add(arrival);
            } else if (type.equals("급행간선")) {
                four.add(arrival);
            }
        }
        addHeader();
        if(one.size() > 0){
            addData("간선", one);
        }
        if(two.size() > 0){
            addData("간선급행", two);
        }
        if(three.size() > 0){
            addData("급행", three);
        }
        if(four.size() > 0){
            addData("급행간선", four);
        }
        notifyDataSetChanged();
    }

    private void addData(String section, JsonArray datas){
        addSectionHeader(section);
        for(JsonElement arrival : datas){
            JsonObject obj = arrival.getAsJsonObject();
            String busno = obj.get("no").getAsString();
            String interval = obj.get("interval").getAsString() + "분";
            String type = section;
            String estimate;
            long arrivalmillis = obj.get("arrival").getAsLong() - System.currentTimeMillis();
            while(arrivalmillis < 0){
                arrivalmillis += obj.get("interval").getAsInt()*60*1000;
            }
            if(arrivalmillis < 1000*60){
                estimate = "곧 도착";
            }
            else {
                estimate = "" + arrivalmillis/(1000*60) + "분 " + (arrivalmillis/1000)%60 + "초";
            }
            addItem(busno, interval, estimate, type);
        }
    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}