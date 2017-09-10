package com.bungabear.inubus;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by minjae on 2017-08-09.
 */

public class BusInfoFragment extends Fragment {
    private static final ArrayList<BusInfoFragment> fragments = new ArrayList<>();
    private String busstop;
    private Context context;
    private ListView listView;
    private BusInfoListViewAdapter listViewAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static Retrofit retrofit;
    private static final String TAG = "INUBus";
    private RetrofitService retrofitService;
    private TextView footer;
    private long updateTime;
    private Handler mHandler = new Handler();
    private Timer tmr;
    // TODO 타이머의 시작과 종료를 앱, 액티비티, 프래그먼트 생명주기에 맞춰주어야한다.
    private Runnable updateFooterTime = new Runnable() {
        @Override
        public void run() {
                long currentTime = System.currentTimeMillis();
                footer.setText((currentTime - updateTime)/1000 + "초전 업데이트.");
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_listview, container, false);
        listView = (ListView) rootView.findViewById(R.id.listview);
        listViewAdapter = new BusInfoListViewAdapter(getContext());
        listView.setAdapter(listViewAdapter);

        retrofitService =  RetrofitClass.getInstance();

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                for(int i = 0; i < fragments.size(); i++){
                    fragments.get(i).updateData();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BusInfoListViewAdapter.CustomListData data = listViewAdapter.getItem(position);
                String start = data.getStart();
                String end = data.getEnd();
                if(start.length()==3) start= "0"+start;
                start = "첫차 : " + start.substring(0,2) + ':' + start.substring(2);
                if(end.length()==3) end= "0"+end;
                end = "막차 : " + end.substring(0,2) + ':' + end.substring(2);

                AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AppTheme_Dialog);
                TextView title = new TextView(context);
                title.setText(data.getBusNo());
                title.setTextColor(ResourcesCompat.getColor(getResources(), R.color.main_color, null));
                title.setTextSize(TypedValue.COMPLEX_UNIT_DIP,50);
                title.setGravity(Gravity.CENTER);
                builder.setCustomTitle(title);
                builder.setMessage(start + "\n" + end);
                builder.setPositiveButton("확인",null);
                builder.show();
//                AlertDialog dialog = builder.create();
//                dialog.show();
            }
        });
        footer = new TextView(context);
        footer.setGravity(Gravity.CENTER);
        updateTime = System.currentTimeMillis();
        listView.addFooterView(footer);
        tmr = new Timer();
        tmr.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(updateFooterTime);
            }
        }, 1000, 1000);
        return rootView;
    }

    public void setFooterText(String str){
        if(footer != null){
            footer.setText(str);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        listViewAdapter.startUpdateTimer();
    }

    @Override
    public void onPause() {
        super.onPause();
        listViewAdapter.stopUpdateTimer();
    }


    // TODO 데이터를 없애고 새로 넣는게 아니라, 추가하고 갱신하는 방식으로 변겅하고, 리싸이클러뷰를 이용해 애니메이션 강조를 사용 하도록 한다.
    public void updateData()
    {
        if(swipeRefreshLayout == null) {
            return;
        }
        try {
            swipeRefreshLayout.setRefreshing(true);
            Call<JsonArray> call = retrofitService.getData("arrivalinfo");
//            Call<JsonArray> call = retrofitService.getData("fakedata.txt");
            call.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                    listViewAdapter.clear();
                    JsonParser jsonParser = new JsonParser();
                    JsonArray data = (JsonArray) jsonParser.parse(response.body().toString());
                    setListview(data);
                    updateTime = System.currentTimeMillis();
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    swipeRefreshLayout.setRefreshing(false);
                    if (Build.VERSION.SDK_INT > 19) {
                        // 젤리빈 이하에서 view parent를 못찾는 문제가 있음.
                        final Snackbar snackbar = Snackbar.make(listView, "서버와의 연결에 실패했습니다.", Snackbar.LENGTH_SHORT);
                        snackbar.setActionTextColor(Color.RED);
                        snackbar.setAction("Dismiss", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                snackbar.dismiss();
                            }
                        }).show();
                    } else {
                        Toast.makeText(context, "서버와의 연결에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        catch(Exception e){
            Log.e(TAG, "updateData: ", e);
        }
    }

    // TODO 버스 특징대로 정렬
    private void setListview(JsonArray data){
        for(int i = 0 ; i < data.size(); i++){
//            Log.d(TAG, "onResponse: " + data.get(i).getAsJsonObject().get("name").getAsString() + " : " + busstop);
            if(data.get(i).getAsJsonObject().get("name").getAsString().equals(busstop))
            {
                JsonArray arrivalArray = data.get(i).getAsJsonObject().get("data").getAsJsonArray();
                for(int j = 0; j < arrivalArray.size(); j++)
                {
                    JsonObject arrival = arrivalArray.get(j).getAsJsonObject();
                    listViewAdapter.addItem(arrival);
                    listViewAdapter.sort();
                    listViewAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    // TODO 데이터 수신을 백그라운드로 전환해 중복수신을 없애야한다.
    public static BusInfoFragment newInstance(Context context, String value){
        BusInfoFragment fragment = new BusInfoFragment();
        fragment.context = context;
        fragment.busstop = value;
        fragments.add(fragment);
//        Log.d(TAG, "Fragment: created");
        return fragment;
    }

    public void setValue(String value){
        this.busstop = value;
    }
}
