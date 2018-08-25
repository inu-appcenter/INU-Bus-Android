package com.inu.bus.activity

import android.databinding.DataBindingUtil
import android.databinding.Observable
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.inu.bus.R
import com.inu.bus.custom.IconPopUp
import com.inu.bus.databinding.ActivityInquireBinding
import com.inu.bus.model.InquireModel
import com.inu.bus.util.Singleton
import kotlinx.android.synthetic.main.activity_inquire.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Minjae Son on 2018-08-11.
 */
class InquireActivity : AppCompatActivity() {

    private val data = InquireModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityInquireBinding>(this, R.layout.activity_inquire)
        binding.data = data
        binding.listener = this

        data.addOnPropertyChangedCallback(mChangeCallbackListener)
//        data.contact.addOnPropertyChangedCallback(mChangeCallbackListener)
//        data.title.addOnPropertyChangedCallback(mChangeCallbackListener)
//        data.message.addOnPropertyChangedCallback(mChangeCallbackListener)
        blurring_view.blurredView(activity_inquire_root)
    }

    // 내용이 비어있지 않으면 전송 버튼 활성화
    private var mChangeCallbackListener = object : Observable.OnPropertyChangedCallback(){
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            if (data.contact != "")
                if (data.title != "")
                    if (data.message != ""){
//                        data.enabled = true
                        data.enabled.set(true)
                        return
                    }

            data.enabled.set(false)
//            data.enabled = false
        }
    }

    fun onSendButtonClick(data : InquireModel){
        Singleton.hideKeyboard(this)
        Singleton.msgRetrofit.postErrorMsg(data).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Snackbar
                        .make(blurring_view, "전송에 실패했습니다", Snackbar.LENGTH_LONG)
                        .setAction("다시시도") { onSendButtonClick(data) }
                        .show()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val popupView = IconPopUp(this@InquireActivity)
                        .setBtnVisible(View.GONE)
                        .setIcon(R.drawable.ic_round_check, false)
                        .setMessageText("소중한 의견 감사드립니다!.")
                        .setDimBlur(blurring_view)
                        .setShowDuration(2000)
                        .setDismissListener{
                            closeActivity()
                        }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    popupView.setWindow(window)
                }
                popupView.show()
            }
        })
    }

    fun onCloseButtonClick(){
        closeActivity()
    }

    private fun closeActivity(){
        Singleton.hideKeyboard(this)
        finish()
    }

}