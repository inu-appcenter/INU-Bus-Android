package com.bungabear.inubus.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bungabear.inubus.Fragment.ArrivalFragment;
import com.bungabear.inubus.Fragment.DestinationFragment;
import com.bungabear.inubus.R;
import com.bungabear.inubus.RetrofitClass;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// TODO 서버의 갱신 주기와 시간을 비교해 자동으로 정보를 업데이트 하도록 한다.
public class MainActiviry extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;
    private ImageView infoButton;
    private static final String TAG = "MainActiviry";
    private Fragment arrivalFragment, destinationFragment;
    private Context context = this;
    private ToggleButton fragmenetToggle;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private AutoCompleteTextView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        fragmenetToggle = (ToggleButton)findViewById(R.id.fragment_toggle);

        setSupportActionBar(toolbar);
        setNavigationDrawer();
        setFragmentToggle();

        String[] items = { "SM3", "SM5", "SM7", "SONATA", "AVANTE", "SOUL", "K5",
                "K7" };
        searchView = (AutoCompleteTextView) findViewById(R.id.search_view);
        searchView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, items));

        sharedPreferences = getSharedPreferences("dialog", MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();

        showNotices();
    }

    private void setFragmentToggle() {
        arrivalFragment = new ArrivalFragment();
        destinationFragment = DestinationFragment.newInstance(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_view, arrivalFragment);
        fragmentTransaction.commit();

        fragmenetToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                Fragment f = isChecked ? destinationFragment : arrivalFragment;
                fragmentTransaction.replace(R.id.fragment_view, f);
                fragmentTransaction.commit();
            }
        });
    }

    private void setNavigationDrawer() {
        infoButton = (ImageView) toolbar.findViewById(R.id.actionbar_btn_info);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean opened = drawer.isDrawerOpen(Gravity.RIGHT);
                if(opened){
                    drawer.closeDrawer(Gravity.RIGHT);
                }
                else {
                    drawer.openDrawer(Gravity.RIGHT);
                }
            }
        });

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {}
            @Override
            public void onDrawerOpened(View drawerView) {}
            @Override
            public void onDrawerClosed(View drawerView) {}
            @Override
            public void onDrawerStateChanged(int newState) {
                if(newState == 2){
                    int id = drawer.isDrawerOpen(Gravity.RIGHT) ? R.drawable.ic_toolbar_close_to_info : R.drawable.ic_toolbar_info_to_close;
                    AnimatedVectorDrawableCompat animatedVectorDrawableCompat = AnimatedVectorDrawableCompat.create(context, id);
                    infoButton.setImageDrawable(animatedVectorDrawableCompat);
                    animatedVectorDrawableCompat.start();
                }
            }
        });

        TextView askButton = drawer.findViewById(R.id.drawer_btn_ask);
        askButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AskActivity.class);
                startActivity(intent);
                AskActivity.setPreView(drawer);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(Gravity.RIGHT)){
            drawer.closeDrawer(Gravity.RIGHT);
        }
        else {
            super.onBackPressed();
        }
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
//        long rejectDate = sharedPreferences.getLong(json.get("id").getAsString(),0);
        int rejectVerion = sharedPreferences.getInt(json.get("id").getAsString()+"v",0);
//        if(rejectDate/(1000*60*24) < System.currentTimeMillis()/(1000*60*24) || rejectVerion != json.get("version").getAsInt()) {
        if(rejectVerion != json.get("version").getAsInt()){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            LinearLayout dialogLayout = (LinearLayout) layoutInflater.inflate(R.layout.custom_notice_dialog, null);
//            AlertDialog dialog = new AlertDialog.Builder(context).create();
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(dialogLayout);
            TextView title = dialogLayout.findViewById(R.id.dialog_title);
            ImageView imageView = dialogLayout.findViewById(R.id.dialog_imageview);
            TextView content = dialogLayout.findViewById(R.id.dialog_content);
            Button leftButton = dialogLayout.findViewById(R.id.dialog_leftButton);
            Button rightButton = dialogLayout.findViewById(R.id.dialog_rightButton);
            leftButton.setText("닫기");
            leftButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });
            rightButton.setText("다시보지않기");
            rightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sharedPreferencesEditor.putLong(json.get("id").getAsString(), System.currentTimeMillis());
                    sharedPreferencesEditor.putInt(json.get("id").getAsString()+"v", json.get("version").getAsInt());
                    sharedPreferencesEditor.apply();
                    dialog.cancel();
                }
            });
            title.setText(json.get("title").getAsString());
            content.setText(json.get("content").getAsString());

            // 이미지 url이 있으면 이미지 로드
            if (!json.get("image").getAsString().equals("")) {
                Bitmap bitmap = getImageUrl(json.get("image").getAsString());
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                    // 기본이 invisible상태
                    imageView.setVisibility(View.VISIBLE);
                }
            }
            dialog.show();
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
