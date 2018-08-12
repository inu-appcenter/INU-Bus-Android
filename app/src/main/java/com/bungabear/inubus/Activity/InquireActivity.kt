package com.bungabear.inubus.activity

import android.databinding.DataBindingUtil
import android.databinding.Observable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.bungabear.inubus.R
import com.bungabear.inubus.databinding.ActivityInquireBinding
import com.bungabear.inubus.model.InquireModel
import com.bungabear.inubus.util.Singleton.LOG_TAG

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
        data.contact.addOnPropertyChangedCallback(changeCallbackListener)
        data.title.addOnPropertyChangedCallback(changeCallbackListener)
        data.message.addOnPropertyChangedCallback(changeCallbackListener)
//        binding.presenter = InquirePresenter()
    }

    var changeCallbackListener = object : Observable.OnPropertyChangedCallback(){
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
    }
}