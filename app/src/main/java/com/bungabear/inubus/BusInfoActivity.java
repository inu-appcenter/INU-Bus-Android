package com.bungabear.inubus;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.graphics.drawable.Animatable2Compat;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// TODO 서버의 갱신 주기와 시간을 비교해 자동으로 정보를 업데이트 하도록 한다.
// TODO 문의, 건의 기능 추가. 가능하면 로그도 첨부
public class BusInfoActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;
    private static final String TAG = "BusInfoActivity";
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private BusInfoPagerAdapter pagerAdapter;
    private int selectedTab = 0;
    private boolean paused = false;
    private Context context = this;
    private Handler mHandler = new Handler();
    private Timer tmr;
    private Runnable updateRemainingTimeRunnable = new Runnable() {
        @Override
        public void run() {
            pagerAdapter.update();
        }
    };
    private SearchView mSearchView;
    private ActionBar actionBar;

    @Override
    protected void onResume() {
        super.onResume();
        // 맨처음 Resume에서는 프래그먼트가 생성이 덜되어 업데이트를 할수 없다.
        // 스플래시 액티비티때문에 한번 pause로 갔다오므로, 이를 이용해 스플래시 이후 업데이트를 수행한다.
        if(paused){
            for(int i = 0 ; i < pagerAdapter.getCount(); i++){
                pagerAdapter.getItem(i).updateData();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        paused = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_businfo);
        sharedPreferences = getSharedPreferences("dialog", MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();


        // 커스텀 액션바 설정
        LinearLayout actionbar = (LinearLayout) getLayoutInflater().inflate(R.layout.custom_actionbar, null);
        mSearchView = actionbar.findViewById(R.id.actionbar_searchview);
//        LinearLayout.LayoutParams llp = (LinearLayout.LayoutParams) mSearchView.getLayoutParams();
//        llp.setMargins(0,0,0,0);
//        mSearchView.setLayoutParams(llp);

        int search_plate = getResources().getIdentifier("search_plate", "id", "android");
        LinearLayout v = (LinearLayout) mSearchView.findViewById(search_plate);
        v.setBackgroundColor(Color.TRANSPARENT);

//        int search_edit_frame = getResources().getIdentifier("search_edit_frame", "id", "android");
//        LinearLayout lllv = (LinearLayout) mSearchView.findViewById(search_edit_frame);
//        LinearLayout.LayoutParams lllvp = (LinearLayout.LayoutParams) lllv.getLayoutParams();
//        lllvp.setMargins(0,0,0,0);
//        lllv.setLayoutParams(lllvp);

        int search_src_text = getResources().getIdentifier("search_src_text", "id", "android");
        EditText mSearchView_et = mSearchView.findViewById(search_src_text);
        mSearchView_et.setTextColor(Color.BLACK);
        mSearchView_et.setBackgroundColor(Color.WHITE);
        mSearchView_et.setHintTextColor(Color.GRAY);
        mSearchView_et.setHint("버스정류장 검색");

        int searchMagIcon = getResources().getIdentifier("search_mag_icon", "id", "android");
        ImageView icon = (ImageView) mSearchView.findViewById(searchMagIcon);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) icon.getLayoutParams();
        params.setMargins(0,0,0,0);
        icon.setLayoutParams(params);

        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionbar);

        Toolbar parent =(Toolbar) actionbar.getParent();
        parent.setContentInsetsAbsolute(0,0);
        // 액션바 설정 끝

        // 탭 추가 및 기본 설정
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        pagerAdapter = new BusInfoPagerAdapter(getSupportFragmentManager());
        pagerAdapter.initFragment(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        tabLayout.setupWithViewPager(viewPager);

        // 커스텀 탭 생성
        View tab0 = getLayoutInflater().inflate(R.layout.custom_businfo_tab, null);
        View tab1 = getLayoutInflater().inflate(R.layout.custom_businfo_tab, null);
        View tab2 = getLayoutInflater().inflate(R.layout.custom_businfo_tab, null);
//        ((TextView)tab0.findViewById(R.id.tabText)).setText("자과대");
//        ((TextView)tab1.findViewById(R.id.tabText)).setText("공대");
//        ((TextView)tab2.findViewById(R.id.tabText)).setText("정문");

        // 복잡한 벡터 애니메이션이 마시멜로우 이하에서는 제대로 표시가안됨.
        if(Build.VERSION.SDK_INT >= 25) {
            // 탭 아이콘 애니메이션 설정
//            ((ImageView)tab0.findViewById(R.id.tabIcon)).setImageResource(R.drawable.ic_anim_science);
            ((ImageView)tab1.findViewById(R.id.tabIcon)).setImageResource(R.drawable.ic_anim_engineer);
            ((ImageView)tab2.findViewById(R.id.tabIcon)).setImageResource(R.drawable.ic_anim_frontgate);

            // v23은 애니메이션에 반복 속성을 적용 못해서 수동으로 반복해준다. Animatable하위 객체대신, AnimatedVectorDrawableCompat을 써야 마시멜로, 누가 둘다 작동 한다.
            final AnimatedVectorDrawableCompat animatedVectorDrawableCompat = AnimatedVectorDrawableCompat.create(context, R.drawable.ic_anim_science);
            ((ImageView)tab0.findViewById(R.id.tabIcon)).setImageDrawable(animatedVectorDrawableCompat);
            animatedVectorDrawableCompat.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                @Override
                public void onAnimationEnd(Drawable drawable) {
                    super.onAnimationEnd(drawable);
                    if(selectedTab == 0) {
                        super.onAnimationEnd(drawable);
                        if(animatedVectorDrawableCompat.isRunning()){
                            animatedVectorDrawableCompat.stop();
                        }
                        else{
                            animatedVectorDrawableCompat.start();
                        }
                    }
                }
            });
        }
        else {
            ((ImageView)tab0.findViewById(R.id.tabIcon)).setImageResource(R.drawable.ic_anim_science);
            ((ImageView)tab1.findViewById(R.id.tabIcon)).setImageResource(R.drawable.ic_anim_engineer);
            ((ImageView)tab2.findViewById(R.id.tabIcon)).setImageResource(R.drawable.ic_anim_frontgate);
        }
        tabLayout.getTabAt(0).setCustomView(tab0);
        tabLayout.getTabAt(1).setCustomView(tab1);
        tabLayout.getTabAt(2).setCustomView(tab2);
        tabLayout.getTabAt(0).setTag(0);
        tabLayout.getTabAt(1).setTag(1);
        tabLayout.getTabAt(2).setTag(2);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ((Animatable)((ImageView)tab.getCustomView().findViewById(R.id.tabIcon)).getDrawable()).start();
                selectedTab = (int)tab.getTag();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ((Animatable)((ImageView)tab.getCustomView().findViewById(R.id.tabIcon)).getDrawable()).stop();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                ((Animatable)((ImageView)tab.getCustomView().findViewById(R.id.tabIcon)).getDrawable()).start();
            }
        });

        // 초기 탭 설정
        tabLayout.getTabAt(1).select();
        selectedTab = 1;

        showNotices();

        Timer tmr = new Timer();
        tmr.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(updateRemainingTimeRunnable);
            }
        }, 1000, 30000);
    }

    private void showNotices(){
        // nodices.txt에 json으로 공지 저장
        RetrofitClass.getInstance().getData("notice.txt").enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
//                Snackbar.make(tabLayout,response.body().toString(),Snackbar.LENGTH_SHORT).show();
                JsonArray data = response.body();

                // 노출순서(중요도, no)가 있으므로 정렬한다.
                List<JsonObject> jsons = new ArrayList<JsonObject>();
                for (int i = 0; i < data.size(); i++) {
                    jsons.add((JsonObject) data.get(i));
                }
                Collections.sort(jsons, new Comparator<JsonObject>() {
                    @Override
                    public int compare(JsonObject lhs, JsonObject rhs) {
                        int lid = lhs.get("no").getAsInt();
                        int rid = rhs.get("no").getAsInt();
                        return lid - rid;
                    }
                });
                // 노출 순서를 위해 역순으로 호출
                for(int i = jsons.size()-1 ; i >= 0; i--){
//                    Log.d(TAG, "onResponse: " + jsons.get(i).toString());
                    showNotice(jsons.get(i));
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }

    private void showNotice(final JsonObject json){
        // 거절 기록에서 millis와 버전을 가져와 비교한다. 버전이 바뀌었으면 무조건 표시한다.
        long rejectDate = sharedPreferences.getLong(json.get("id").getAsString(),0);
        int rejectVerion = sharedPreferences.getInt(json.get("id").getAsString()+"v",0);
        if(rejectDate/(1000*60*24) < System.currentTimeMillis()/(1000*60*24) || rejectVerion != json.get("version").getAsInt()) {
            // 얼럿 다이얼로그를 써야 테마를 지정하기 쉽다.
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(context, R.style.AppTheme_Dialog);

            // 커스텀뷰 생성.
            // 커스텀뷰가 메세지뷰 밑에 추가되므로, 이미지 아래에 메세지를 넣기위해 이미지뷰, 텍스트뷰 순으로 레이아웃을 만듦.
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            LinearLayout dialogLayout = (LinearLayout) layoutInflater.inflate(R.layout.custom_notice_dialog, null);
            builder.setView(dialogLayout);

            // 이미지 url이 있으면 이미지 로드
            ImageView imageView = (ImageView) dialogLayout.findViewById(R.id.dialog_notice_imageview);
            if (!json.get("image").getAsString().equals("")) {
                Bitmap bitmap = getImageUrl(json.get("image").getAsString());
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                    // 기본이 invisible상태
                    imageView.setVisibility(View.VISIBLE);
                }
            }
            TextView content = (TextView)dialogLayout.findViewById(R.id.dialog_notice_content);
            content.setText(json.get("content").getAsString());

            builder.setTitle(json.get("title").getAsString());
            builder.setPositiveButton("확인", null);
            builder.setNeutralButton("하룻동안 보이지 않기", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // SharedPreferences에 선택 저장.
                    // key : ${id}, value : currentmillis
                    // key : ${id}v, value : ${version}
                    sharedPreferencesEditor.putLong(json.get("id").getAsString(), System.currentTimeMillis());
                    sharedPreferencesEditor.putInt(json.get("id").getAsString()+"v", json.get("version").getAsInt());
                    sharedPreferencesEditor.apply();
                }
            });
            builder.show();
        }
    }

    private Bitmap getImageUrl(final String st_url){
        final Bitmap[] bitmap = new Bitmap[1];
        bitmap[0] = null;
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    URL url = new URL(st_url);
                    URLConnection conn = url.openConnection();
                    conn.connect();
                    BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                    bitmap[0] = BitmapFactory.decodeStream(bis);
                    bis.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    Log.e(TAG, "onCreate: Image URL error", e1);
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return bitmap[0];
    }
}
