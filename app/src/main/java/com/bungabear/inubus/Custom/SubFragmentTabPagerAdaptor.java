package com.bungabear.inubus.Custom;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bungabear.inubus.Fragment.ArrivalSubFragment;

import java.util.ArrayList;

/**
 * Created by Bunga on 2018-01-29.
 */

public class SubFragmentTabPagerAdaptor extends FragmentStatePagerAdapter {

    // Count number of tabs
    private ArrayList<ArrivalSubFragment> fragments = new ArrayList<>();

    public SubFragmentTabPagerAdaptor(FragmentManager fm, Context context) {
        super(fm);
        initFragment(context);
    }

    // ChildFragmentManager crashes at android.support.v4.app.FragmentManagerImpl.getFragment(FragmentManager.java:902)
    // when reattached
    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
//        super.restoreState(state, loader);
    }

    public void initFragment(Context context) {
        // 컨텍스트와 정류장 이름을 넘겨준다. 정류장 이름은 데이터 파싱에 사용하므로, 일치하는지 꼭 확인해야한다.
        fragments.add(ArrivalSubFragment.newInstance(context,"engineer"));
        fragments.add(ArrivalSubFragment.newInstance(context,"science"));
        fragments.add(ArrivalSubFragment.newInstance(context,"frontgate"));
        fragments.add(ArrivalSubFragment.newInstance(context,"BITZon"));
    }

    public void refresh(){
        for(ArrivalSubFragment fragment : fragments){
            fragment.refresh();
        }
    }

    public void refreshEstimate(){
        for(ArrivalSubFragment fragment : fragments){
            fragment.refreshEstimate();
        }
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
