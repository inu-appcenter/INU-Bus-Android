package com.bungabear.inubus;

import android.content.Context;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by Minjae on 2017-02-27.
 * 커스팀 리스트뷰 어댑터. 커스텀 리스트 데이터를 배열로 담아 관리한다.
 */
// TODO 버스 도착 임박 및 배차 임박을 색상으로 표시한다.
class BusInfoListViewAdapter extends BaseAdapter {
    private Context context = null;
    private ArrayList<CustomListData> mListData = new ArrayList<>();
    private ArrayList<CustomViewHolder> holders = new ArrayList<>();
    private Handler mHandler = new Handler();
    private Timer tmr;
    // TODO 타이머의 시작과 종료를 앱, 액티비티, 프래그먼트 생명주기에 맞춰주어야한다.
    private Runnable updateRemainingTimeRunnable = new Runnable() {
        @Override
        public void run() {
            synchronized (holders) {
                long currentTime = System.currentTimeMillis();
                for (CustomViewHolder holder : holders) {
                    holder.updateTimeRemaining(currentTime);
                }
            }
        }
    };

    BusInfoListViewAdapter(Context context){
        this.context = context;
    }

    public void clear(){
        mListData.clear();
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    public void addItem(JsonObject data){
        CustomListData addInfo = new CustomListData(data.get("no").getAsString(), data.get("arrival").getAsLong(),  data.get("interval").getAsInt(), data.get("start").getAsString(), data.get("end").getAsString(), data.get("type").getAsString());
        mListData.add(addInfo);
//        Log.d(TAG, "addItem: " + " no : " + no + " arrival : " + arrival + " interval : " + interval + " start : " + start + " end : " + end);
    }

    @Override
    public CustomListData getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //아이템에 들어갈 레이아웃을 지정
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        CustomViewHolder holder;
        //화면에 캐쉬가 없으면 새로 생성해줌
        if(convertView == null){
            holder = new CustomViewHolder();
            holders.add(holder);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_businfo_list_item, null);

            holder.tv_busNo = (TextView) convertView.findViewById(R.id.busNo);
            holder.tv_arrival = (TextView) convertView.findViewById(R.id.arrival);
            holder.tv_arrivalText = (TextView) convertView.findViewById(R.id.arrival_text);
            holder.tv_interval = (TextView) convertView.findViewById(R.id.interval);
            holder.tv_busNoM = (TextView) convertView.findViewById(R.id.busNoM);

            convertView.setTag(holder);
        } else {
            //있으면 태그에 저장했던 홀더를 불러옴
            holder = (CustomViewHolder) convertView.getTag();
        }
        CustomListData listData = mListData.get(position);
        holder.tv_busNo.setText(listData.st_busNo);
        holder.tv_interval.setText(" "+listData.i_interval+"분");
        holder.tv_arrivalText.setText((listData.i_arrivalText==1)? context.getString(R.string.arrival_before) + " ": context.getString(R.string.arrival_after) + " ");
        holder.l_arrival = listData.l_arrival;
        if(listData.getType().equals("광역")){
            holder.tv_busNo.setTextColor(ContextCompat.getColor(context, R.color.광역));
        }
        else if(listData.getType().equals("광역급행")){
            // M버스
            holder.tv_busNoM.setVisibility(View.VISIBLE);
            holder.tv_busNo.setTextColor(ContextCompat.getColor(context, R.color.광역급행));
        }
        else if(listData.getType().equals("간선급행")){
            holder.tv_busNo.setTextColor(ContextCompat.getColor(context, R.color.간선급행));
        }
        else {
            // 기본색상
        }
        long arrivaltime = Math.abs(listData.l_arrival - System.currentTimeMillis());
        holder.tv_arrival.setText(String.format("%d 분 %d 초",
                TimeUnit.MILLISECONDS.toMinutes(arrivaltime),
                TimeUnit.MILLISECONDS.toSeconds(arrivaltime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(arrivaltime))
        ));

        return convertView;
    }

    public void startUpdateTimer() {
        tmr = new Timer();
        tmr.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(updateRemainingTimeRunnable);
            }
        }, 1000, 1000);
    }
    public void stopUpdateTimer(){
        if(tmr != null) {
            tmr.cancel();
            tmr = null;
        }
    }

    //ListView Item 뷰를 담는 클래스 ViewHoler
    private class CustomViewHolder {
        private TextView tv_busNo;
        private TextView tv_busNoM;
        private TextView tv_interval;
        private TextView tv_intervalText;
        private TextView tv_arrival;
        private TextView tv_arrivalText;
        private long l_arrival;

        private void updateTimeRemaining(long currentTime){
            long time = Math.abs(l_arrival - currentTime);
            tv_arrival.setText(String.format("%d 분 %d 초",
                    TimeUnit.MILLISECONDS.toMinutes(time),
                    TimeUnit.MILLISECONDS.toSeconds(time) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time))
            ));
            if(l_arrival - currentTime > 0)
            {
                tv_arrivalText.setText(R.string.arrival_before);
            }
            else
            {
                tv_arrivalText.setText(R.string.arrival_after);
            }
            tv_arrivalText.append(" ");
        }
    }

    //ListView Item 객체들이 가질 데이터를 담는 클래스
    public class CustomListData {
        private String st_busNo, st_start, st_end, st_type;
        private int i_interval;
        private long l_arrival;

        // 0 = after, 1 = before
        private int i_arrivalText;

        CustomListData(String no, long arrival, int interval, String start, String end, String type) {
            st_busNo = no;
            l_arrival = arrival;
            i_interval = interval;
            st_start = start;
            st_end = end;
            i_arrivalText = (l_arrival > System.currentTimeMillis()) ? 1 : 0;
            st_type = type;
        }

        public String getBusNo() {
            return st_busNo;
        }

        public String getStart() {
            return st_start;
        }

        public String getEnd(){
            return st_end;
        }

        public String getType() {
            return st_type;
        }
    }

    public void sort(){
        Collections.sort(mListData, myComparator);
    }
    private Comparator<BusInfoListViewAdapter.CustomListData> myComparator = new Comparator<BusInfoListViewAdapter.CustomListData>() {
        @Override
        public int compare(BusInfoListViewAdapter.CustomListData o1, BusInfoListViewAdapter.CustomListData o2) {
            return o1.getBusNo().compareTo(o2.getBusNo());
        }
    };

}

