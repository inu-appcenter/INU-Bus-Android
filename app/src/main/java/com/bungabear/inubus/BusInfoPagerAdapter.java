package com.bungabear.inubus;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class BusInfoPagerAdapter extends FragmentStatePagerAdapter {
    private final List<BusInfoFragment> mFragments = new ArrayList<>();

    public BusInfoPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void initFragment(Context context) {
        // 컨텍스트와 정류장 이름을 넘겨준다. 정류장 이름은 데이터 파싱에 사용하므로, 일치하는지 꼭 확인해야한다.
        // TODO 프레그먼트 생성을 메인 액티비티에서 하도록 바꾸어준다.
        mFragments.add(BusInfoFragment.newInstance(context,"science"));
        mFragments.add(BusInfoFragment.newInstance(context,"engineer"));
        mFragments.add(BusInfoFragment.newInstance(context,"frontgate"));
    }

    @Override
    public BusInfoFragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size()%Integer.MAX_VALUE;
    }

}

