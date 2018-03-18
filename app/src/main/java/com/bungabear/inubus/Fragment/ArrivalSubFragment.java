package com.bungabear.inubus.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.bungabear.inubus.Custom.BusArrivalRecyclerAdapter;
import com.bungabear.inubus.R;

/**
 * Created by Bunga on 2018-01-29.
 */

public class ArrivalSubFragment extends Fragment {

    private Context context;
    private TextView test;
    private String st_busStop;
    private FloatingActionButton fab;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView rv;
    private BusArrivalRecyclerAdapter adapter;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch(msg.what){
                case 0:
                    refreshLayout.setRefreshing(true);
                    return true;
                case 1:
                    refreshLayout.setRefreshing(false);
                    return true;
                case 2:
                    Snackbar.make(rv,"네트워크 오류", Snackbar.LENGTH_SHORT).show();
                    return true;
            }
            return false;
        }
    });

    public static ArrivalSubFragment newInstance(Context context, String stopName) {
        Bundle args = new Bundle();
        ArrivalSubFragment fragment = new ArrivalSubFragment();
        fragment.setArguments(args);
        fragment.st_busStop = stopName;
        fragment.context = context;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_arrival_subfragment, container, false);
        // recyclerview test
        rv = v.findViewById(R.id.arrival_sub_recycler_list);
        adapter = new BusArrivalRecyclerAdapter(st_busStop);
        refreshLayout = v.findViewById(R.id.swipeRefreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.refresh(mHandler);
            }
        });
//        refreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                adapter.refresh(mHandler);
//            }
//        });
//        fab = v.findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                adapter.refresh(mHandler);
//            }
//        });
//        rv.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        rv.setLayoutManager(mLayoutManager);

        rv.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(getContext().getResources().getDrawable(R.drawable.bg_arrival_divider));
        rv.addItemDecoration(dividerItemDecoration);
        adapter.refresh(mHandler);
        rv.getRecycledViewPool().setMaxRecycledViews(0, 0);
        return v;
    }

    public void refresh(){
        adapter.refresh(mHandler);
    }
}
