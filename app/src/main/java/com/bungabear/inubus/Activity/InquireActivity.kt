package com.bungabear.inubus.activity

import android.databinding.DataBindingUtil
import android.databinding.Observable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.bungabear.inubus.R
import com.bungabear.inubus.custom.IconPopUp
import com.bungabear.inubus.databinding.ActivityInquireBinding
import com.bungabear.inubus.model.InquireModel
import com.bungabear.inubus.util.Singleton
import kotlinx.android.synthetic.main.activity_inquire.*

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
        // 전송 버튼 활성화, 비활성화 셋팅
        data.contact.addOnPropertyChangedCallback(mChangeCallbackListener)
        data.title.addOnPropertyChangedCallback(mChangeCallbackListener)
        data.message.addOnPropertyChangedCallback(mChangeCallbackListener)
        blurring_view.blurredView(activity_inquire_root)
    }

    private var mChangeCallbackListener = object : Observable.OnPropertyChangedCallback(){
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            if (data.contact.get() != "")
                if (data.title.get() != "")
                    if (data.message.get() != ""){
                        data.enabled.set(true)
                        return
                    }

            data.enabled.set(false)
        }
    }

    fun onSendButtonClick(data : InquireModel){
        // TODO 문의 보내기
        Singleton.hideKeyboard(this)
        val popupView = IconPopUp(this)
        popupView.setBtnVisible(View.GONE)
                .setIcon(R.drawable.ic_round_check, false)
                .setMessageText("소중한 의견 감사드립니다!.")
                .setDimBlur(blurring_view)
                .setShowDuration(2000)
                .setDismissListener{
                    closeActivity()
                }
                .show()
    }

    fun onCloseButtonClick(){
        closeActivity()
    }

    fun closeActivity(){
        Singleton.hideKeyboard(this)
        finish()
    }

}