package com.inu.bus.model

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.ObservableBoolean
import android.os.Build
import com.inu.bus.BR
import com.inu.bus.util.Singleton

/**
 * Created by Minjae Son on 2018-08-11.
 */

data class InquireModel(
        val device : String =  "${Build.MODEL} : ${Build.VERSION.RELEASE} (${Build.VERSION.SDK_INT})",
        val service : String = Singleton.myPackageName) : BaseObservable(){

    @Bindable
    var title = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.title)
        }
    @Bindable
    var contact = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.contact)
        }
    @Bindable
    var message = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.message)
        }

    var enabled  : ObservableBoolean  = ObservableBoolean(false)
}