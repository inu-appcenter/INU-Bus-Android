package com.bungabear.inubus.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bungabear.inubus.R;
import com.bungabear.inubus.Custom.SubFragmentTabPagerAdaptor;
import com.bungabear.inubus.Custom.SwipeRemovableViewPager;

/**
 * Created by Bunga on 2018-01-29.
 */

public class ArrivalFragment extends Fragment {

    private FloatingActionButton fab;
    private TabLayout tabLayout;
    private SwipeRemovableViewPager viewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_arrival, container, false);
        tabLayout = (TabLayout) v.findViewById(R.id.arrival_tablayout);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_tab_spanner);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_tab_flask).getIcon().setAlpha(100);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_tab_gate).getIcon().setAlpha(100);

        viewPager = (SwipeRemovableViewPager) v.findViewById(R.id.arrival_viewpager);
        viewPager.setSwipeable(false);
        viewPager.setOffscreenPageLimit(3);
        // TODO Context 액티비티에서 받아오기.
        final SubFragmentTabPagerAdaptor pagerAdapter = new SubFragmentTabPagerAdaptor(getChildFragmentManager(), getContext());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                tab.getIcon().setAlpha(255);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setAlpha(100);
           }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        fab = v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return v;
    }
}
