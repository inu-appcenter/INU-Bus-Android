package com.bungabear.inubus.model

import android.databinding.ObservableBoolean
import android.databinding.ObservableField

/**
 * Created by Minjae Son on 2018-08-11.
 */

data class InquireModel(var title : ObservableField<String> = ObservableField(""),
                        var contact : ObservableField<String> = ObservableField(""),
                        var message : ObservableField<String> = ObservableField(""),
                        var enabled : ObservableBoolean = ObservableBoolean(false))