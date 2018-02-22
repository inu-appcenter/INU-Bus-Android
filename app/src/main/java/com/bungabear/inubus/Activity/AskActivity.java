package com.bungabear.inubus.Activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bungabear.inubus.R;
import com.bungabear.inubus.RetrofitClass;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Bunga on 2018-02-23.
 */

public class AskActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_title, et_contact, et_message;
    private Button btn_send;
    private static View preView;

    public static void setPreView(View view){
        preView = view;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);
        et_title = findViewById(R.id.ask_et_title);
        et_contact = findViewById(R.id.ask_et_contact);
        et_message = findViewById(R.id.ask_et_msg);
        btn_send = findViewById(R.id.ask_btn_submit);
        btn_send.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String title = et_title.getText().toString();
        String contact = et_contact.getText().toString();
        String msg = et_message.getText().toString();
        String device;
        if(title.length() == 0 || msg.length() == 0){
            Snackbar.make(btn_send,"내용을 입력해 주세요", Snackbar.LENGTH_SHORT).show();
        }
        else {
            device = "Android" + Build.VERSION.SDK_INT + ' ' + android.os.Build.DEVICE + ' ' + android.os.Build.MODEL + ' ' + android.os.Build.PRODUCT;
            RetrofitClass.getInstance().sendErrMsg(title, contact, msg, device).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Snackbar.make(preView,"의견 감사합니다!", Snackbar.LENGTH_SHORT).show();
                    ((DrawerLayout)preView).closeDrawer(Gravity.RIGHT);
                    finish();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                }
            });
        }
    }
}
