package com.bungabear.inubus.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bungabear.inubus.Custom.RouteRecyclerAdapter;
import com.bungabear.inubus.R;

/**
 * Created by Bunga on 2018-02-23.
 */

public class RouteActivity extends AppCompatActivity {

    private RecyclerView rv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        rv = findViewById(R.id.route_recycler_list);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(mLayoutManager);

        RouteRecyclerAdapter adapter = new RouteRecyclerAdapter();
        adapter.addStop("인천대입구" , 1,1);
        for (int i = 0; i < 30; i++) {
            if (i == 15) {
                adapter.addReturn();
            }
            adapter.addStop("인천대입구" , 1 + i/15,3);
        }
        adapter.addStop("인천대입구" , 2,2);
        rv.setAdapter(adapter);
//        rv.getRecycledViewPool().setMaxRecycledViews(0, 0);

    }
}
