package com.bungabear.inubus.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
    private PullRefreshLayout refreshLayout;

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
        RecyclerView rv = v.findViewById(R.id.arrival_sub_recycler_list);
        refreshLayout = v.findViewById(R.id.swipeRefreshLayout);
        fab = v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshLayout.setRefreshing(true);
                // refresh
            }
        });
        rv.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        rv.setLayoutManager(mLayoutManager);

        BusArrivalRecyclerAdapter adapter = new BusArrivalRecyclerAdapter();
        for (int i = 0; i < 30; i++) {
            if (i % 4 == 0) {
                adapter.addSectionHeader(""+i);
            }
            adapter.addItem("" + i,"","");
        }
        rv.setAdapter(adapter);
        rv.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        return v;
    }
}
