package com.bungabear.inubus.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bungabear.inubus.Custom.DestinationRecyclerAdapter;
import com.bungabear.inubus.R;

/**
 * Created by Bunga on 2018-01-29.
 */

public class DestinationFragment extends Fragment {

    private Context context;
    private RecyclerView rv;
    public static DestinationFragment newInstance(Context context) {
        Bundle args = new Bundle();
        DestinationFragment fragment = new DestinationFragment();
        fragment.setArguments(args);
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
        View v = inflater.inflate(R.layout.fragment_destination, container, false);
        rv = v.findViewById(R.id.destination_recycler_list);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        rv.setLayoutManager(mLayoutManager);

        DestinationRecyclerAdapter adapter = new DestinationRecyclerAdapter();
        for (int i = 0; i < 30; i++) {
            if (i % 4 == 0) {
                adapter.addSectionHeader(""+i);
            }
            adapter.addItem(new String[]{"8","780","908","780-1"} , i%4 + 1);
        }
        rv.setAdapter(adapter);
        rv.getRecycledViewPool().setMaxRecycledViews(0, 0);
        return v;
    }
}
